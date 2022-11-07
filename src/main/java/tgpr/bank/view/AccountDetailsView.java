package tgpr.bank.view;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.bank.controller.AccountDetailsController;
import tgpr.bank.model.Account;
import tgpr.bank.model.Transfer;
import tgpr.framework.ColumnSpec;
import tgpr.framework.ObjectTable;
import tgpr.framework.Tools;
import tgpr.framework.ViewManager;

import javax.sound.sampled.Line;
import java.util.List;

public class AccountDetailsView extends DialogWindow {
    private final AccountDetailsController controller;
    private final Account account;

    private ObjectTable<Transfer> historyTable;

    private final Label lblIban = new Label("");
    private final Label lblTitle = new Label("");
    private final Label lblType = new Label("");
    private final Label lblSaldo = new Label("");

    private final TextBox txtFilter = new TextBox();

    public AccountDetailsView(AccountDetailsController controller, Account account) {
        super("Account Details");
        this.controller = controller;
        this.account = account;
        setHints(List.of(Hint.CENTERED, Hint.FIXED_SIZE));
        setCloseWindowWithEscape(true);
        setFixedSize(new TerminalSize(115, 20));

        Panel root = new Panel().setLayoutManager(new LinearLayout(Direction.VERTICAL).setSpacing(1));
        setComponent(root);

        new EmptySpace().addTo(root);

        accountDetailPanel().addTo(root);
        historyPanel().addTo(root);
        favorites_categoryPanel().addTo(root);
        buttonPanel().addTo(root);
        refresh();

    }
    

    private Panel accountDetailPanel(){
        Panel panel = new Panel().setLayoutManager(new GridLayout(4).setHorizontalSpacing(1));
        panel.addComponent(new Label("IBAN:"));
        lblIban.addTo(panel).addStyle(SGR.BOLD);

        panel.addComponent(new Label("Type:"));
        lblType.addTo(panel).addStyle(SGR.BOLD);

        panel.addComponent(new Label("Title:"));
        lblTitle.addTo(panel).addStyle(SGR.BOLD);

        panel.addComponent(new Label("Saldo:"));
        lblSaldo.addTo(panel).addStyle(SGR.BOLD);

        return panel;

    }

    private Border historyPanel() {
        int accountID = account.getId();
        var panel = new Panel().setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));
        new Label("Filter:").addTo(panel);
        txtFilter.addTo(panel).takeFocus().setTextChangeListener((txt,client)->reloadFiltered());
        Border border = panel.withBorder(Borders.singleLine("History"));
        historyTable = new ObjectTable<>(
                        new ColumnSpec<>("Effect_Date", m-> Tools.ifNull(m.getEffectiveAt(),m.getCreatedAt())),
                        new ColumnSpec<>("Description", Transfer::getDescription),
                        new ColumnSpec<>("From/To", m -> m.getSourceAccountID() == accountID ? m.getTargetAccount().getIban()+" - "+m.getTargetAccount().getTitle() : m.getSourceAccount().getIban()+" - "+m.getSourceAccount().getTitle()),
                        new ColumnSpec<>("Category", m -> Tools.ifNull(m.getCategory(accountID,m.getId()), "")),
                        new ColumnSpec<>("Amount", m ->accountID == m.getSourceAccountID() ? "-"+m.getAmount()+" €" : "+"+m.getAmount()+" €"),
                        new ColumnSpec<>("Saldo", m->m.getSourceSaldo() == 0 ? "" : m.getSourceSaldo()+" €"),
                        new ColumnSpec<>("State", Transfer::getState)
                );
        historyTable.addTo(panel);
        historyTable.setSelectAction(this::displayTransfer);

        reloadData();

        return border;
    }

    public void reloadData() {
       historyTable.clear();
       var transfers = controller.getTransfers();
       historyTable.add(transfers);
       historyTable.invalidate();
    }

    public void reloadFiltered(){
        historyTable.clear();
        var transfers = controller.getTransfers();
        if(txtFilter.getText().isEmpty()){
            historyTable.add(transfers);
        }else{
            for (Transfer transfer : transfers) {
                if(transfer.getTargetAccount().getIban().contains(txtFilter.getText().toUpperCase())
                        || transfer.getSourceAccount().getIban().contains(txtFilter.getText().toUpperCase())
                        || transfer.getSourceAccount().getTitle().contains(txtFilter.getText().toUpperCase())
                        || transfer.getTargetAccount().getTitle().contains(txtFilter.getText().toUpperCase())
                        || transfer.toString().toLowerCase().contains(txtFilter.getText().toLowerCase())){
                    historyTable.add(transfer);
                }
                if(transfer.getCategory(account.getId(),transfer.getId()) != null){
                    if(transfer.getCategory(account.getId(),transfer.getId()).getName().toLowerCase().contains(txtFilter.getText().toLowerCase())){
                        historyTable.add(transfer);
                    }
                }
            }
        }



    }


    private Panel favorites_categoryPanel(){
        Panel panel = new Panel().setLayoutManager(new LinearLayout(Direction.HORIZONTAL).setSpacing(1));
        panel.addComponent(categoryPanel());
        panel.addComponent(favoritePanel());
        return panel;
    }


    private Border categoryPanel(){
        var panel = new Panel().setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));
        Border border = panel.withBorder(Borders.singleLine("Category"));

        return  border;
    }

    private Border favoritePanel() {
        var panel = new Panel().setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));
        Border border = panel.withBorder(Borders.singleLine("Favorite"));

        return border;
    }

    private Panel buttonPanel() {
        Panel panel = new Panel().setLayoutManager(new LinearLayout(Direction.HORIZONTAL).setSpacing(1));
        Button btnNewTransfer = new Button("New Transfer").addTo(panel);
        Button btnExit = new Button("Exit",this::close).addTo(panel);
        return panel;
    }

    public void refresh(){
        lblIban.setText(account.getIban());
        lblTitle.setText(account.getTitle());
        lblType.setText(account.getType());
        lblSaldo.setText(Tools.toString(account.getSaldo()));
    }

    private void displayTransfer() {
        var transfer = historyTable.getSelected();
        if (transfer == null) return;
        if (controller.displayTransfer(transfer, account) == null)
            refresh();
        reloadData();
    }
}

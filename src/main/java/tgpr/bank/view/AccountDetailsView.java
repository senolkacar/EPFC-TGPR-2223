package tgpr.bank.view;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.bank.controller.AccountDetailsController;
import tgpr.bank.model.*;
import tgpr.framework.ColumnSpec;
import tgpr.framework.ObjectTable;
import tgpr.framework.Tools;
import tgpr.framework.ViewManager;
import java.util.List;
import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;



public class AccountDetailsView extends DialogWindow {
    private ObjectTable<Account> table;
    private final AccountDetailsController controller;
    private ObjectTable<Category> categoryTable;

    private final Account account;
    private ObjectTable<Transfer> historyTable;

    private final Label lblIban = new Label("");
    private final Label lblTitle = new Label("");
    private final Label lblType = new Label("");
    private final Label lblSaldo = new Label("");
    private ComboBox<String> cboFavorite;

    private List<Account> favoritesList;


    private TextBox txtNewCategory;


    private final TextBox txtFilter = new TextBox();

    public AccountDetailsView(AccountDetailsController controller, Account account) {
        super("Account Details");
        this.controller = controller;
        this.account = account;
        setHints(List.of(Window.Hint.CENTERED, Window.Hint.FIXED_SIZE));
        setCloseWindowWithEscape(true);
        setFixedSize(new TerminalSize(113, 24));

        Panel root = new Panel().setLayoutManager(new LinearLayout(Direction.VERTICAL).setSpacing(1))
                .setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));
        setComponent(root);

        accountDetailPanel().addTo(root);
        historyPanel().addTo(root);
        favorites_categoryPanel().addTo(root);
        buttonPanel().addTo(root);
        refresh();

    }


    private Panel accountDetailPanel() {
        Panel panel = new Panel().setLayoutManager(new GridLayout(4).setTopMarginSize(0));
        ;
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
        Border border = panel.withBorder(Borders.singleLine("History"));
        var filterpanel = new Panel().setLayoutManager(new GridLayout(2).setHorizontalSpacing(1).setLeftMarginSize(0));
        new Label("Filter:").addTo(filterpanel);
        txtFilter.addTo(filterpanel).takeFocus().setTextChangeListener((txt, client) -> reloadFiltered());
        txtFilter.setPreferredSize(new TerminalSize(20, 1));
        filterpanel.addTo(panel);
        new EmptySpace().addTo(panel);

        historyTable = new ObjectTable<>(
//                        new ColumnSpec<>("Effect_Date", m-> Tools.ifNull(m.getEffectiveAtLDT().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),m.getCreatedAtLDT().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))))
                new ColumnSpec<>("Effect_Date", Transfer::getStringEffectiveAtLDT),
                new ColumnSpec<>("Description", Transfer::getDescription),
                new ColumnSpec<>("From/To", this::AccountToDisplay).setWidth(30).setOverflowHandling(ColumnSpec.OverflowHandling.Wrap),
                new ColumnSpec<>("Category", this::CategoryToDisplay).setWidth(11),
                new ColumnSpec<>("Amount", this::getAmount).setWidth(10),
                new ColumnSpec<>("Saldo",  this::getSourceSaldo).setWidth(10),
                new ColumnSpec<>("State", Transfer::getState));
        historyTable.setPreferredSize(new TerminalSize(115, 5));
        historyTable.addTo(panel);
        historyTable.setSelectAction(this::displayTransfer);



        reloadDataHistory();

        return border;
    }

    private String CategoryToDisplay(Transfer m) {
        if (Transfer.getCategory(account.getId(), m.getId()) == null) {
            return "";
        } else {
            return Transfer.getCategory(account.getId(), m.getId()).getName();
        }
    }

    private String AccountToDisplay(Transfer m) {
        if (m.getSourceAccountID() == account.getId()) {
            return m.getTargetAccount().getIban() + " - " + m.getTargetAccount().getTitle();
        } else {
            return m.getSourceAccount().getIban() + " - " + m.getSourceAccount().getTitle();
        }
    }

    private String getSourceSaldo(Transfer m) {
        List<Transfer> transfers = Transfer.getTransfersForLabel(account);
        double somme = 0;
        for (Transfer transferr : transfers) {
            if (!transferr.getState().equals("rejected")) {
                if (transferr.getSourceAccountID() == account.getId()) {
                    somme -= transferr.getAmount();
                } else {
                    somme += transferr.getAmount();
                }
            }
            if ((m.getId() == (transferr.getId())) && (!transferr.getState().equals("future"))) {
                return (String.valueOf(transferr.transformInEuro(somme)));
            }
        }
        return  "";

    }

    private String getAmount(Transfer m) {
        if(m.getSourceAccountID() == account.getId()){
            return "-"+account.transformInEuro(m.getAmount());
        }else{
            return "+"+account.transformInEuro(m.getAmount());
        }
    }

    public void reloadDataHistory() {
        historyTable.clear();
       var transfers = controller.getTransfers();
       historyTable.add(transfers);
       historyTable.invalidate();
       }


    public void reloadFiltered(){
        historyTable.clear();
        var transfers = controller.getTransfers();

            if (txtFilter.getText().isEmpty()) {
                historyTable.add(transfers);
            } else {
                for (Transfer transfer : transfers) {
                    Account account = transfer.getSourceAccountID() == this.account.getId() ? transfer.getTargetAccount() : transfer.getSourceAccount();
                    if(account.getIban().contains(txtFilter.getText().toUpperCase())
                            || account.getTitle().toUpperCase().contains(txtFilter.getText().toUpperCase())
                            || transfer.toString().toUpperCase().contains(txtFilter.getText().toUpperCase())
                            || (Transfer.getCategory(this.account.getId(),transfer.getId()) != null && Transfer.getCategory(this.account.getId(),transfer.getId()).getName().toUpperCase().contains(txtFilter.getText().toUpperCase()))
                    ){

                        historyTable.add(transfer);
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
        categoryTable = new ObjectTable<>(

                new ColumnSpec<>("Name", Category::getName).setWidth(22),
                new ColumnSpec<Category>("Type", c -> c.isSystem() ? "local" : "system"),
                new ColumnSpec<>("Uses", c-> controller.getCategoryUses(c).size())
        );
        categoryTable.setPreferredSize(new TerminalSize(50,10));
        categoryTable.addTo(panel);
        categoryTable.setSelectAction(() -> {
            var category = categoryTable.getSelected();
            if(!category.isSystem()){
                controller.showError();
            }else{
                controller.editCategory(category);
                reloadData();
                categoryTable.setSelected(category);

            }

        });

        new EmptySpace().addTo(panel);
        Panel root = new Panel();
        root.setLayoutManager(new LinearLayout(Direction.HORIZONTAL).setSpacing(1));

        txtNewCategory = new TextBox().setPreferredSize(new TerminalSize(22,1)).addTo(root);
        Button btnAddCatrgoty = new Button("Add",this::addCategory).addTo(root);
        Button btnResetCatrgoty = new Button("Reset", this::resetCategory).addTo(root);
        root.addTo(panel);
        reloadData();

        return  border;
    }
    public void resetCategory(){
        txtNewCategory.setText("");
    }




    private void addCategory(){
       controller.add(txtNewCategory.getText(),account.getId());
        txtNewCategory.setText("");
        reloadData();
        }
    public void reloadData() {

     categoryTable.clear();
     var Category  = controller.getCategory();
     categoryTable.add(Category);
    }



    private Border favoritePanel() {
        var panel = new Panel().setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));
        table = new ObjectTable<>(
                new ColumnSpec<>("IBAN ",this::displayFavorite).setWidth(28),
                new ColumnSpec<>("Type", Account::getType).setWidth(10)
        );
        reloadDataFav();
        panel.addComponent(table);
        table.setPreferredSize(new TerminalSize(ViewManager.getTerminalColumns(),15));

        Border border = panel.withBorder(Borders.singleLine("Favorite"));
        panel.setPreferredSize(new TerminalSize(58,10));
        var buttons = new Panel().addTo(panel).setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
        panel.addComponent(buttons,LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));
        favoritesList = controller.getFavoritesNotListed();
        var favoritesListToString = favoritesList.stream().map(m->m.getIban()+" - "+m.getTitle()).collect(Collectors.toList());
        favoritesListToString.add(0,"");
        cboFavorite = new ComboBox<String>(favoritesListToString).addTo(buttons).setPreferredSize(new TerminalSize(28,1))
                .addListener((newIndex, oldIndex, byUser) -> reloadDataFav());
        Button add = new Button("Add", this::addFavourite).addTo(buttons);
        Button btnReset = new Button("Reset", this::reset).addTo(buttons);


        table.setPreferredSize(new TerminalSize(ViewManager.getTerminalColumns(), 15));

        table.setSelectAction(() ->{
            var account = table.getSelected();
            table.setSelected(account);
            controller.DeleteFavouriteAccount(account);
        });

        reloadDataFav();

        return border;
    }

    private String displayFavorite(Account account){
        return account.getIban()+" - "+account.getTitle();
    }

    public void reset(){
        cboFavorite.setSelectedIndex(0);

    }
    public void reloadInfo(){
        favoritesList = controller.getFavoritesNotListed();
        var favoritesListToString = favoritesList.stream().map(m->m.getIban()+" - "+m.getTitle()).collect(Collectors.toList());
        int size = cboFavorite.getItemCount();
        for (int i = size; i>0; --i) {
            cboFavorite.removeItem(i - 1);
        }
        cboFavorite.addItem(0,"");
        for (String s : favoritesListToString) {
            cboFavorite.addItem(s);
        }
    }


        public void addFavourite(){
            if (cboFavorite.getSelectedIndex()!=0) {
                controller.addFavourite(favoritesList.get(cboFavorite.getSelectedIndex() - 1).getId());
                reloadInfo();
                reloadDataFav();
            }

    }

    public void reloadDataFav(){
        table.clear();
        var ListFavorites = controller.getFavorites();
        table.add(ListFavorites);
    }


    private Panel buttonPanel() {
        Panel panel = new Panel().setLayoutManager(new LinearLayout(Direction.HORIZONTAL))
                .setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        new Button("New Transfer").addTo(panel);
        new Button("Close",this::close).addTo(panel);
        return panel;
    }

    public void refresh(){
        lblIban.setText(account.getIban());
        lblTitle.setText(account.getTitle());
        lblType.setText(account.getType());
        lblSaldo.setText((account.transformInEuro(account.getSaldo())));
    }

    private void displayTransfer() {
        var transfer = historyTable.getSelected();
        if (transfer != null){
            controller.displayTransfer(transfer,account);
        }
        reloadDataHistory();
        reloadData();
    }
}

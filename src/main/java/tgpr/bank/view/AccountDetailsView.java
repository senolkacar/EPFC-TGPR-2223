package tgpr.bank.view;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.bank.controller.AccountDetailsController;
import tgpr.bank.model.Account;
import tgpr.bank.model.Favourite;
import tgpr.bank.model.Security;
import tgpr.bank.model.User;
import tgpr.framework.ColumnSpec;
import tgpr.framework.ObjectTable;
import tgpr.framework.Tools;
import tgpr.framework.ViewManager;

import javax.swing.text.Position;
import java.util.List;
import java.util.stream.Collectors;

public class AccountDetailsView extends DialogWindow {
    private  ObjectTable <Account> table;
    private final AccountDetailsController controller;
    private final Account account;



    private final Label lblIban = new Label("");
    private final Label lblTitle = new Label("");
    private final Label lblType = new Label("");
    private final Label lblSaldo = new Label("");
    private ComboBox <String> cboFavorite;

    private List<Account> favoritesList;


    public AccountDetailsView(AccountDetailsController controller, Account account) {
        super("Account Details");
        this.controller = controller;
        this.account = account;
        User current = Security.getLoggedUser();
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
        Panel panel = new Panel().setLayoutManager(new GridLayout(4).setHorizontalSpacing(2));
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
        var panel = new Panel().setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));
        Border border = panel.withBorder(Borders.doubleLine("History"));
        return border;
    }


    private Panel favorites_categoryPanel(){
        Panel panel = new Panel().setLayoutManager(new LinearLayout(Direction.HORIZONTAL).setSpacing(1));
        panel.addComponent(categoryPanel());
        panel.addComponent(favoritePanel());

        return panel;
    }


    private Border categoryPanel(){
        var panel = new Panel().setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));
        Border border = panel.withBorder(Borders.doubleLine("Category"));

        return  border;
    }

    private Border favoritePanel() {
        var panel = new Panel().setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));
        table = new ObjectTable<>(
                new ColumnSpec<>("IBAN ",a -> a.getIban()+" - "+a.getTitle()),
                new ColumnSpec<>("Type", Account::getType)
        );
        reloadData();
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
                .addListener((newIndex, oldIndex, byUser) -> reloadData());
        Button add = new Button("Add", this::addFavourite).addTo(buttons);
        Button btnReset = new Button("Reset", this::reset).addTo(buttons);


        table.setPreferredSize(new TerminalSize(ViewManager.getTerminalColumns(), 15));

        table.setSelectAction(() ->{
            var account = table.getSelected();
            table.setSelected(account);
            controller.DeleteFavouriteAccount(account);
        });
        // charge les données dans la table
        reloadData();



        return border;
    }
    public void reset(){
        cboFavorite.setSelectedIndex(0);

    }
    public void reloadData(){
        table.clear();
        var ListFavorites = controller.getFavorites();
        table.add(ListFavorites);

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
                reloadData();
            }

    }



    private Panel buttonPanel() {
        Panel panel = new Panel().setLayoutManager(new LinearLayout(Direction.HORIZONTAL).setSpacing(1));
        Button btnNewTransfer = new Button("New Transfer").addTo(panel);
        Button btnExit = new Button("Exit").addTo(panel);
        return panel;
    }

    public void refresh(){
        lblIban.setText(account.getIban());
        lblTitle.setText(account.getTitle());
        lblType.setText(account.getType());
        lblSaldo.setText(Tools.toString(account.getSaldo()));
    }
}

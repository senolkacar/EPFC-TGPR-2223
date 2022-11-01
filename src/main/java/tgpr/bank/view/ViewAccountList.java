package tgpr.bank.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import tgpr.bank.controller.ControllerAccountList;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.Panel;
import tgpr.bank.model.Account;
import tgpr.bank.model.User;
import tgpr.framework.ColumnSpec;
import tgpr.framework.ObjectTable;
import tgpr.framework.Tools;
import tgpr.framework.ViewManager;
import com.googlecode.lanterna.gui2.menu.Menu;
import com.googlecode.lanterna.gui2.menu.MenuBar;
import com.googlecode.lanterna.gui2.menu.MenuItem;
import tgpr.bank.model.Security;


import java.util.List;

public class ViewAccountList extends BasicWindow {
    private final ControllerAccountList controller;
    private final ObjectTable<Account> table;
    private final Menu menuFile;

    public ViewAccountList(ControllerAccountList controller) {
        this.controller = controller;

        setTitle(getTitleWithUser());
        setHints(List.of(Hint.EXPANDED));

        // Le panel 'root' est le composant racine de la fenêtre (il contiendra tous les autres composants)
        Panel root = new Panel();
        setComponent(root);

        MenuBar menuBar = new MenuBar().addTo(root);
        menuFile = new Menu("File");
        menuBar.add(menuFile);
        MenuItem menuLogout = new MenuItem("Logout", controller::logout);
        menuFile.add(menuLogout);
        MenuItem menuExit = new MenuItem("Exit", controller::exit);
        menuFile.add(menuExit);

        // ajoute une ligne vide
        new EmptySpace().addTo(root);

        // crée un tableau de données pour l'affichage des comptes
        table = new ObjectTable<>(
                new ColumnSpec<>("IBAN", Account::getIban),
                new ColumnSpec<>("Title", Account::getTitle),
                new ColumnSpec<Account>("Floor", m -> Tools.ifNull(m.getFloor(), "")),
                new ColumnSpec<>("Type", Account::getType),
                new ColumnSpec<Account>("Saldo", m -> Tools.ifNull(m.getSaldo(), ""))

        );
        root.addComponent(table);
        new EmptySpace().addTo(root);
        Button btnNewTransfer = new Button("New Transfer").addTo(root);
        table.setPreferredSize(new TerminalSize(ViewManager.getTerminalColumns(), 15));
        reloadData();
    }

    public void reloadData() {
        // vide le tableau
        table.clear();
        // demande au contrôleur la liste des membres
        var accounts = controller.getAccounts();
        // ajoute l'ensemble des membres au tableau
        table.add(accounts);
    }

    private String getTitleWithUser() {

        // à implementer use system date/time
        // on implémentera le use system quand on va créer le back to the future
        return "Welcome to MyBank (" + Security.getLoggedUser().getEmail() + " - " + (Security.getLoggedUser().getType()) + ")";
    }



}

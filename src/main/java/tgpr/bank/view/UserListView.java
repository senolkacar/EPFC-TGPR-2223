package tgpr.bank.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.BasicWindow;
import tgpr.bank.controller.UserListController;
import tgpr.bank.view.UserListView;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.table.Table;
import tgpr.framework.ColumnSpec;
import tgpr.framework.ObjectTable;
import tgpr.framework.Tools;
import tgpr.framework.ViewManager;
import tgpr.bank.model.User;
import com.googlecode.lanterna.gui2.menu.Menu;
import com.googlecode.lanterna.gui2.menu.MenuBar;
import com.googlecode.lanterna.gui2.menu.MenuItem;
import com.googlecode.lanterna.input.KeyStroke;
import tgpr.bank.model.Security;


import java.util.List;

public class UserListView extends BasicWindow {

    private final UserListController controller;
    private final ObjectTable<User> table;

    private final Menu menuFile;


    public UserListView(UserListController controller) {
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

        // crée un tableau de données pour l'affichage des membres
        table = new ObjectTable<>(
                new ColumnSpec<>("id", User::getId),
                new ColumnSpec<>("email", User::getEmail),
                new ColumnSpec<>("last_name", m -> Tools.ifNull(m.getLast_name(), "")),
                new ColumnSpec<>("first_name", m -> Tools.ifNull(m.getFirst_name(), "")),
                new ColumnSpec<User>("Birth Date", m -> Tools.toString(m.getBirthdate())),
                new ColumnSpec<User>("Type", m -> Tools.ifNull(m.getType(), "")),
                new ColumnSpec<>("Agency", m -> Tools.ifNull(m.getAgency(), ""))
        );
        // ajoute le tableau au root panel
        root.addComponent(table);
        // spécifie que le tableau doit avoir la même largeur quee le terminal et une hauteur de 15 lignes
        table.setPreferredSize(new TerminalSize(ViewManager.getTerminalColumns(), 15));
        // charge les données dans la table
        reloadData();
    }

    public void reloadData() {
        // vide le tableau
        table.clear();
        // demande au contrôleur la liste des membres
        var users = controller.getUsers();
        // ajoute l'ensemble des membres au tableau
        table.add(users);
    }

    private String getTitleWithUser() {

        // à implementer use system date/time
        return "Welcome to MyBank (" + Security.getLoggedUser().getEmail() + " - " + (Security.getLoggedUser().getType()) + ")";
    }



}

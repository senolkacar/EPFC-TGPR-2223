package tgpr.bank.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.menu.Menu;
import com.googlecode.lanterna.gui2.menu.MenuBar;
import com.googlecode.lanterna.gui2.menu.MenuItem;
import tgpr.bank.controller.ControllerAccountList;
import tgpr.bank.controller.EditClientController;
import tgpr.bank.controller.LoginController;
import tgpr.bank.controller.ManagerController;
import tgpr.bank.model.Account;
import tgpr.bank.model.Agency;
import tgpr.bank.model.Security;
import tgpr.framework.ColumnSpec;
import tgpr.framework.ObjectTable;
import tgpr.framework.Tools;
import tgpr.framework.ViewManager;

import java.util.List;

import static tgpr.framework.Controller.navigateTo;

public class ManagerView extends BasicWindow {
    private final ManagerController controller;
    private ObjectTable<Agency> table;

    private final Menu menuFile;


    public ManagerView(ManagerController controller) {
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

        new EmptySpace().addTo(root);

        table = new ObjectTable<>(
                new ColumnSpec<>("Name", Agency::getName));
        root.addComponent(table);
        new EmptySpace().addTo(root);
        Button btnNewClient = new Button("New Client",this::createClient).addTo(root);
        table.setPreferredSize(new TerminalSize(ViewManager.getTerminalColumns(), 15));
        table.setSelectAction(() ->{
            var agency = table.getSelected();
            table.setSelected(agency);
            controller.showAgencyDetails(agency);
        });

        reloadData();
    }

    public void reloadData() {
        table.clear();
        var agencies = controller.getAgency();
        table.add(agencies);
    }

    private String getTitleWithUser() {

        // à implementer use system date/time
        // on implémentera le use system quand on va créer le back to the future
        return "Welcome to MyBank (" + Security.getLoggedUser().getEmail() + " - " + (Security.getLoggedUser().getType()) + ")";
    }

    private void createClient() {
        navigateTo(new EditClientController());
    }
}

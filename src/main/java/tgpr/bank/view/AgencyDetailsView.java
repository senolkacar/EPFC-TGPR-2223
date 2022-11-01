package tgpr.bank.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import com.googlecode.lanterna.input.KeyStroke;
import org.w3c.dom.Text;
import tgpr.bank.controller.AgencyDetailsController;
import tgpr.bank.model.Agency;
import tgpr.bank.model.Security;
import tgpr.bank.model.User;
import tgpr.framework.ColumnSpec;
import tgpr.framework.ObjectTable;
import tgpr.framework.ViewManager;

import java.util.List;

public class AgencyDetailsView extends DialogWindow {
    private final AgencyDetailsController controller;
    private final Agency agency;

    private final TextBox txtFilter = new TextBox();

    private ObjectTable<User> table;

    public AgencyDetailsView(AgencyDetailsController controller, Agency agency) {
        super("Agency Details");
        this.controller = controller;
        this.agency = agency;
        setHints(List.of(Hint.CENTERED,Hint.FIXED_SIZE));
        setCloseWindowWithEscape(true);
        setFixedSize(new TerminalSize(55,10));
        Panel root = new Panel().setLayoutManager(new LinearLayout(Direction.VERTICAL).setSpacing(0));
        setComponent(root);
        new EmptySpace().addTo(root);

        agencyDetailPanel().addTo(root);
        buttonPanel().addTo(root);


    }

    private Panel agencyDetailPanel(){
        Panel panel = new Panel().setLayoutManager(new LinearLayout(Direction.VERTICAL)).setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));
        clientsPanel().addTo(panel);

        return panel;
    }

    private Border clientsPanel(){
        var panel = new Panel().setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));
        new Label("Filter:").addTo(panel);
        txtFilter.addTo(panel).takeFocus().setTextChangeListener((txt,client)->reloadClients());
        new  EmptySpace().addTo(panel);
        Border border = panel.withBorder(Borders.singleLine("Clients"));
        table = new ObjectTable<>(new ColumnSpec<>("Firstname", u -> u.getFirst_name()),
                new ColumnSpec<>("Lastname", u -> u.getLast_name()),
                new ColumnSpec<>("Birthdate", u-> u.getBirthdate()),
                new ColumnSpec<>("Email",u-> u.getEmail())
        );
        table.setSelectAction(()-> {
            var client = table.getSelected();
            controller.editClient(client);
            reloadClients();
            table.setSelected(client);
        });
        table.addTo(panel);
        reloadClients();
        return border;
    }

    private Panel buttonPanel() {
        Panel panel = new Panel().setLayoutManager(new LinearLayout(Direction.HORIZONTAL)).setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        if(Security.isManager()){
            var btnNewClient = new Button("New Client",()->{
                User u = controller.addClient();
                if(u!=null){
                    reloadClients();
                }
            }).addTo(panel);
            ViewManager.addShortcut(this,btnNewClient, KeyStroke.fromString("<A-a>"));
        }
        Button btnExit = new Button("Close",this::close).addTo(panel);
        return panel;
    }

    public void reloadClients(){
        table.clear();
        var clients = controller.getClients(txtFilter.getText());
        table.add(clients);
    }



}


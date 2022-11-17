package tgpr.bank.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import com.googlecode.lanterna.input.KeyStroke;
import tgpr.bank.controller.EditClientController;
import tgpr.bank.model.Account;
import tgpr.bank.model.Agency;
import tgpr.bank.model.Client;
import tgpr.bank.model.User;
import tgpr.framework.Tools;
import tgpr.framework.ViewManager;

import java.util.List;
import java.util.regex.Pattern;

public class EditClientView extends DialogWindow {
    private final EditClientController controller;
    private final User client;

    private final Agency agency;

    private ComboBox<String> cboagency;
    private TextBox txtfirstName;
    private TextBox txtlastName;
    private TextBox txtbirthdate;
    private TextBox txtemail;
    private TextBox txtpassword;
    private final Label errFirstName = new Label("");
    private final Label errLastName = new Label("");
    private final Label errBirthdate = new Label("");
    private final Label errEmail = new Label("");
    private final Label errPassword = new Label("");

    private Button btnCreateUpdate;

    public EditClientView(EditClientController controller, User client,Agency agency) {
        super(client == null ? "Create client" : "Edit client");
        this.controller = controller;
        this.client = client;
        this.agency = agency;


        setHints(List.of(Hint.CENTERED,Hint.FIXED_SIZE));
        setCloseWindowWithEscape(true);
        setFixedSize(new TerminalSize(50,15));
        Panel root = new Panel().setLayoutManager(new LinearLayout(Direction.VERTICAL).setSpacing(1));
        setComponent(root);

        createFields().addTo(root);
        createButtonsPanel().addTo(root);
        refresh();

    }

    private Panel createFields(){
        var panel = new Panel().setLayoutManager(new GridLayout(2).setTopMarginSize(1));
        new Label("Agency:").addTo(panel);
        var agencies = Agency.getAllAgency();
        cboagency = new ComboBox<>(agencies.stream().map(Agency::getName).toArray(String[]::new)).addTo(panel);
        cboagency.setSelectedItem(agency.getName());
        cboagency.addListener((selectedIndex,previousSelection,changedByUserInteraction)->validate());
        new EmptySpace().addTo(panel);
        new EmptySpace().addTo(panel);
        new Label("First Name:").addTo(panel);
        txtfirstName = new TextBox(new TerminalSize(20,1)).addTo(panel)
                .setTextChangeListener((txt,client)->validate());
        new EmptySpace().addTo(panel);
        errFirstName.addTo(panel).setForegroundColor(TextColor.ANSI.RED);
        new Label("Last Name:").addTo(panel);
        txtlastName = new TextBox(new TerminalSize(20,1)).addTo(panel)
                .setTextChangeListener((txt,client)->validate());
        new EmptySpace().addTo(panel);
        errLastName.addTo(panel).setForegroundColor(TextColor.ANSI.RED);
        new Label("Birthdate:").addTo(panel);
        txtbirthdate = new TextBox(new TerminalSize(20,1)).addTo(panel)
                .setValidationPattern(Pattern.compile("[\\d/]{0,10}"))
                .setTextChangeListener((txt,client)->validate());
        new EmptySpace().addTo(panel);
        errBirthdate.addTo(panel).setForegroundColor(TextColor.ANSI.RED);
        new Label("Email:").addTo(panel);
        txtemail = new TextBox(new TerminalSize(20,1)).addTo(panel)
                .setTextChangeListener((txt,client)->validate());
        new EmptySpace().addTo(panel);
        errEmail.addTo(panel).setForegroundColor(TextColor.ANSI.RED);
        new Label("Password:").addTo(panel);
        txtpassword = new TextBox(new TerminalSize(20,1)).addTo(panel)
                .setMask('*')
                .setTextChangeListener((txt,client)->validate());
        new EmptySpace().addTo(panel);
        errPassword.addTo(panel).setForegroundColor(TextColor.ANSI.RED);

        return panel;

    }

    private Panel createButtonsPanel(){
        var panel = new Panel().setLayoutManager(new LinearLayout(Direction.HORIZONTAL))
                .setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

        new EmptySpace().addTo(panel);
        var btnaccessaccount = new Button("Account Access",()->{
            List<Account> c=controller.showAccountAccess(client);
        }).addTo(panel);

        if(client!=null){
            var btnDelete = new Button("Delete",this::delete).addTo(panel);
            ViewManager.addShortcut(this,btnDelete, KeyStroke.fromString("<A-u>"));
        }
        btnCreateUpdate = new Button(client == null ? "Create" : "Save",this::add).addTo(panel).setEnabled(false);
        new Button("Close",this::close).addTo(panel);
        ViewManager.addShortcut(this,btnCreateUpdate, KeyStroke.fromString(client==null?"<A-a>":"<A-u>"));

        return panel;
    }


    public void add(){
        controller.save(
                txtemail.getText(),
                txtpassword.getText(),
                txtlastName.getText(),
                txtfirstName.getText(),
                txtbirthdate.getText(),
                cboagency.getText()
        );
    }
    private void validate(){
        var errors = controller.validate(
                txtemail.getText(),
                txtpassword.getText(),
                txtlastName.getText(),
                txtfirstName.getText(),
                txtbirthdate.getText(),
                cboagency.getText()
        );

        errFirstName.setText(errors.getFirstErrorMessage(User.Fields.first_name));
        errLastName.setText(errors.getFirstErrorMessage(User.Fields.last_name));
        errBirthdate.setText(errors.getFirstErrorMessage(User.Fields.birth_date));
        errEmail.setText(errors.getFirstErrorMessage(User.Fields.email));
        errPassword.setText(errors.getFirstErrorMessage(User.Fields.Password));

        btnCreateUpdate.setEnabled(errors.isEmpty());
    }

    public void delete(){
        controller.delete();
    }

    private void refresh(){
        if(client != null){
            txtemail.setText(client.getEmail());
            txtpassword.setText(client.getPassword());
            txtlastName.setText(client.getLast_name());
            txtfirstName.setText(client.getFirst_name());
            txtbirthdate.setText(Tools.toString(client.getBirth_date()));
            cboagency.setSelectedItem(controller.getAgency().getName());
        }
    }
}

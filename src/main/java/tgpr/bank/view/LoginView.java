package tgpr.bank.view;


import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import tgpr.bank.controller.LoginController;
import tgpr.framework.Configuration;
import tgpr.framework.Layouts;
import tgpr.framework.Panels;
import tgpr.framework.Tools;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LoginView extends BasicWindow {

    private final LoginController controller;
    private final TextBox txtEmail;
    private final TextBox txtPassword;
    private final Button btnLogin;
    private final CheckBox useSystemDate;
    private final TextBox date;
    private final Label errDateTime = new Label("");

    private boolean isChecked;




    public LoginView(LoginController controller) {
        this.controller = controller;

        setTitle("Login");
        setHints(List.of(Hint.CENTERED));

        Panel root = new Panel();
        setComponent(root);

        Panel panel = new Panel().setLayoutManager(new GridLayout(2).setTopMarginSize(1).setVerticalSpacing(1))
                .setLayoutData(Layouts.LINEAR_CENTER).addTo(root);
        panel.addComponent(new Label("Email:"));
        txtEmail = new TextBox(new TerminalSize(20,1)).addTo(panel);
        panel.addComponent(new Label("Password:"));
        txtPassword = new TextBox(new TerminalSize(20,1)).setMask('*').addTo(panel);
        panel.addComponent(new Label("Use System Date/Time:"));
        useSystemDate = new CheckBox().addTo(panel);
        panel.addComponent(new Label("Custom System Date/Time:"));
        date = new TextBox(new TerminalSize(20, 1)).addTo(panel);
        errDateTime.setForegroundColor(TextColor.ANSI.RED).addTo(panel);
        date.setText("17/01/2022 23:59:59");

        date.setTextChangeListener((txt, byUser) -> {

            if (!Tools.isValidDateTime(date.getText())) {
                errDateTime.setText("invalid date");
            }
            else{
                errDateTime.setText("");
            }
        });

        useSystemDate.addListener((isChecked) -> {
            if (!useSystemDate.isChecked()) {
                date.setReadOnly(false);
                date.setTextChangeListener((txt, byUser) -> {
                    if (!Tools.isValidDateTime(date.getText()))
                        errDateTime.setText ("invalid date");
                    else{
                        errDateTime.setText("");
                    }
                });
            }
            else {
                date.setReadOnly(true);
            }
        }).addTo(panel);


        new EmptySpace().addTo(root);

        Panel buttons = new Panel().setLayoutManager(new LinearLayout(Direction.HORIZONTAL))
                .setLayoutData(Layouts.LINEAR_CENTER).addTo(root);
        btnLogin = new Button("Login", this::login).addTo(buttons);
        Button btnExit = new Button("Exit", this::exit).addTo(buttons);

        new EmptySpace().addTo(root);

        Button btnSeedData = new Button("Reset Database", this::seedData);
        Panel debug = Panels.verticalPanel(LinearLayout.Alignment.Center,
                new Button("Login as default admin", this::logAsDefaultAdmin),
                new Button("Login as default manager", this::logAsDefaultManager),
                new Button("Login as default client", this::logAsDefaultClient),
                btnSeedData
        );
        debug.withBorder(Borders.singleLine(" For debug purpose ")).setLayoutData(Layouts.LINEAR_CENTER).addTo(root);

        txtEmail.takeFocus();
    }

    private void seedData() {
        controller.seedData();
        btnLogin.takeFocus();
    }

    private void exit() {
        controller.exit();
    }

    private void login() {
        var errors = controller.login(txtEmail.getText(), txtPassword.getText(),date.getText(), useSystemDate);
        if (!errors.isEmpty()) {
            txtEmail.takeFocus();
        }
    }

    private void logAsDefaultAdmin() {
        controller.login(Configuration.get("default.admin.email"), Configuration.get("default.admin.password"),date.getText(), useSystemDate);
    }

    private void logAsDefaultClient() {
        controller.login(Configuration.get("default.client.email"), Configuration.get("default.client.password"),date.getText(), useSystemDate);
    }

    private void logAsDefaultManager() {
        controller.login(Configuration.get("default.manager.email"), Configuration.get("default.manager.password"),date.getText(), useSystemDate);
    }
}

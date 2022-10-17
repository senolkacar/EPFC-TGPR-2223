package tgpr.bank.view;


import com.googlecode.lanterna.gui2.*;
import tgpr.bank.controller.LoginController;
import tgpr.framework.Configuration;
import tgpr.framework.Layouts;
import tgpr.framework.Panels;

import java.util.List;

public class LoginView extends BasicWindow {

    private final LoginController controller;
    private final TextBox txtEmail;
    private final TextBox txtPassword;
    private final Button btnLogin;

    public LoginView(LoginController controller) {
        this.controller = controller;

        setTitle("Login");
        setHints(List.of(Hint.CENTERED));

        Panel root = new Panel();
        setComponent(root);

        Panel panel = new Panel().setLayoutManager(new GridLayout(2).setTopMarginSize(1).setVerticalSpacing(1))
                .setLayoutData(Layouts.LINEAR_CENTER).addTo(root);
        panel.addComponent(new Label("Email:"));
        txtEmail = new TextBox().addTo(panel);
        panel.addComponent(new Label("Password:"));
        txtPassword = new TextBox().setMask('*').addTo(panel);

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
        debug.withBorder(Borders.singleLine(" For debug purpose ")).addTo(root);

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
        var errors = controller.login(txtEmail.getText(), txtPassword.getText());
        if (!errors.isEmpty()) {
            txtEmail.takeFocus();
        }
    }

    private void logAsDefaultAdmin() {
        controller.login(Configuration.get("default.admin.email"), Configuration.get("default.admin.password"));
    }

    private void logAsDefaultClient() {
        controller.login(Configuration.get("default.client.email"), Configuration.get("default.client.password"));
    }

    private void logAsDefaultManager() {
        controller.login(Configuration.get("default.manager.email"), Configuration.get("default.manager.password"));
    }
}

package tgpr.bank.controller;

import com.googlecode.lanterna.gui2.Window;
import tgpr.bank.BankApp;
import tgpr.bank.model.User;
import tgpr.bank.model.UserValidator;
import tgpr.bank.model.Security;
import tgpr.bank.view.LoginView;
import tgpr.framework.Controller;
import tgpr.framework.Error;
import tgpr.framework.ErrorList;
import tgpr.framework.Model;

import java.util.List;

public class LoginController extends Controller {
    public void exit() {
        System.exit(0);
    }

    public List<Error> login(String email, String password) {
        var errors = new ErrorList();
        errors.add(UserValidator.isValidEmail(email));
        errors.add(UserValidator.isValidPassword(password));

        if (errors.isEmpty()) {
            var user = User.checkCredentials(email, password);
            if (user != null) {
                Security.login(user);
                navigateTo(new UserListController());
            } else
                showError(new Error("invalid credentials"));
        } else
            showErrors(errors);

        return errors;
    }

    public void seedData() {
        Model.seedData(BankApp.DATABASE_SCRIPT_FILE);
    }

    @Override
    public Window getView() {
        return new LoginView(this);
    }
}

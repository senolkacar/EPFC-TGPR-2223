package tgpr.bank.controller;

import com.googlecode.lanterna.gui2.CheckBox;
import com.googlecode.lanterna.gui2.Window;
import tgpr.bank.BankApp;
import tgpr.bank.model.*;
import tgpr.bank.view.LoginView;
import tgpr.framework.Controller;
import tgpr.framework.Error;
import tgpr.framework.ErrorList;
import tgpr.framework.Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LoginController extends Controller {

    public void exit() {
        System.exit(0);
    }

    public List<Error> login(String email, String password, String date, CheckBox checkbox) {
        var errors = new ErrorList();
        errors.add(UserValidator.isValidEmail(email));
        errors.add(UserValidator.isValidPassword(password));
        errors.add(UserValidator.isValidDate(date));
        if (errors.isEmpty()) {
            var user = User.checkCredentials(email, password);
            if (user != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                LocalDateTime datee = LocalDateTime.parse(date,formatter);
                if (checkbox.isChecked()) {
                    Security.login(user);
                    Date.changeDateOnDB(LocalDateTime.now());
                    DateInterface.date(LocalDateTime.now());
                    DateInterface.hasChanged(false);
                } else {
                    Security.login(user);
                    DateInterface.date(datee);
                    Date.changeDateOnDB(datee);
                    DateInterface.hasChanged(true);
                }
                List<Account> list = Account.getAll();
                    Transfer.deleteEverything();
                    for (Account account: list) {
                        List<Transfer> transfers = Transfer.getAllTransfersForBTTF();
                        Transfer.updateEverything(transfers, account);
//                        for (Transfer trans : transfers
//                        ) {
//                            Transfer.updateDatabase(account, trans);
//                        }
                    }

                if (Security.getLoggedUser().getType().equals("manager")) {
                    navigateTo(new ManagerController());
                } else {
                    navigateTo(new ControllerAccountList());
                }
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

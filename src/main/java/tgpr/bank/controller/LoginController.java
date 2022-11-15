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

import javax.tools.Tool;
import javax.xml.crypto.dsig.TransformService;
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
                if (checkbox.isChecked()){
                    Security.login(user);
                    DateInterface.date(Date.changeFormatToEn(Date.getSysDateParsed(String.valueOf(Date.getSysDate()))));
                    List<Account> list = Account.getAll();
                    for (Account account: list) {
                        List<Transfer> transfers = Transfer.getTransfers(account);
                        Transfer.updateEverything(transfers);
                    }
                    navigateTo(new ControllerAccountList());
                }
                else {
                    Security.login(user);
                    DateInterface.date(Date.changeFormatToEn(Date.getSysDateParsed(date)));
                    DateInterface.hasChanged(true);
                    List<Account> list = Account.getAll();
                    for (Account account: list) {
                        List<Transfer> transfers = Transfer.getTransfers(account);
                        Transfer.updateEverything(transfers);
                    }
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

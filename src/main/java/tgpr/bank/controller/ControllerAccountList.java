package tgpr.bank.controller;

import com.googlecode.lanterna.gui2.Window;
import tgpr.bank.model.Account;
import tgpr.bank.view.ViewAccountList;
import tgpr.framework.Controller;
import tgpr.bank.model.Security;

import java.util.List;

public class ControllerAccountList extends Controller {

    @Override
    public Window getView() {
        return new ViewAccountList(this);
    }

    public List<Account> getAccounts() {
        return Account.getAll();
    }

    public void logout() {
        Security.logout();
        navigateTo(new LoginController());
    }

    public void exit() {
        System.exit(0);
    }


    public void newTransfer(){
        NewTransferController.previousController = this;
        navigateTo(new NewTransferController());
    }

    public void showAccountDetails(Account account) {
        navigateTo(new AccountDetailsController(account));
    }


}

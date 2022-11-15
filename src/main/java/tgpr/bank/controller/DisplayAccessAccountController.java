package tgpr.bank.controller;

import com.googlecode.lanterna.gui2.Window;
import tgpr.bank.model.Account;
import tgpr.bank.model.User;
import tgpr.bank.view.DisplayAccessAccountView;
import tgpr.framework.Controller;

public class DisplayAccessAccountController extends Controller {
    private final DisplayAccessAccountView view;
    private User user;
    private Account account;
    public DisplayAccessAccountController(Account account) {
        this.account=account;
        view = new DisplayAccessAccountView(this,account);
    }

    @Override
    public Window getView() {
        return null;
    }
}

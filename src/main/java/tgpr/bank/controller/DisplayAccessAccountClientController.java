package tgpr.bank.controller;

import com.googlecode.lanterna.gui2.Window;
import tgpr.bank.model.Account;
import tgpr.bank.model.User;
import tgpr.bank.view.DisplayAccessAccountView;
import tgpr.framework.Controller;

public class DisplayAccessAccountClientController extends Controller {
    private final DisplayAccessAccountView view;
    private User user;
   // private Account account;

    public DisplayAccessAccountClientController(DisplayAccessAccountView view, User user) {
        this.view = view;
        this.user = user;
        //this.account = account;
    }


    @Override
    public Window getView() {
        return null;
    }
    public void update() {

    }
}

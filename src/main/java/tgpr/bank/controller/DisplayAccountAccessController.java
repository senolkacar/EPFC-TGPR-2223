package tgpr.bank.controller;

import com.googlecode.lanterna.gui2.Window;
import tgpr.bank.model.Account;
import tgpr.bank.view.DisplayAccountAccessView;
import tgpr.framework.Controller;

public class DisplayAccountAccessController extends Controller {
    private final DisplayAccountAccessView view;
    private Account account;

    public DisplayAccountAccessController(Account account){
        this.account=account;
        view = new DisplayAccountAccessView(this,account);
    }
    @Override
    public Window getView() {
        return view;
    }
}

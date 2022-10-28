package tgpr.bank.controller;

import com.googlecode.lanterna.gui2.Window;
import tgpr.bank.model.Account;
import tgpr.bank.view.NewTransferView;
import tgpr.framework.Controller;

import java.util.List;

public class NewTransferController extends Controller {

    @Override
    public Window getView() {
        return new NewTransferView(this);
    }

    public List<Account> getAccounts() {
        return Account.getAll();
    }
}

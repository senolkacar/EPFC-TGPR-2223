package tgpr.bank.controller;


import tgpr.bank.model.Account;
import tgpr.bank.model.Transfer;
import tgpr.bank.model.User;
import tgpr.bank.view.AccountDetailsView;
import tgpr.framework.Controller;
import com.googlecode.lanterna.gui2.Window;

import java.util.List;


public class AccountDetailsController extends Controller {
    private final AccountDetailsView view;
    private Account account;

    // peut etre non necessaire
    public AccountDetailsController() {
        this(null);
    }

    public AccountDetailsController(Account account) {
        this.account = account;
        view = new AccountDetailsView(this, account);
    }

    @Override
    public Window getView() {
        return view;
    }

    public Account getAccount() {
        return account;
    }

    public List<Transfer> getTransfers() {
        return Transfer.getTransfers(getAccount());
    }

    public Transfer getTransfer(int id) {
        return Transfer.getTransfer(id);
    }

    public List<Transfer> getFilteredTransfer(String filter) {
        return Transfer.getTransfersFilter(getAccount(), filter);
    }
}


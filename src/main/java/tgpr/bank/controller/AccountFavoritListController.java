package tgpr.bank.controller;
import com.googlecode.lanterna.gui2.Window;
import tgpr.bank.model.Account;
import tgpr.bank.view.AccountFavoritListView;
import tgpr.framework.Controller;

import java.util.List;

public class AccountFavoritListController extends Controller  {
    private List<Account> favouritAccounts;

    @Override
    public Window getView() {
        return new AccountFavoritListView(this);
    }

    public List<Account> getFavouritAccounts() {
        return Account.getAll();
    }
}



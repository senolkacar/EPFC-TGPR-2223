package tgpr.bank.controller;

import com.googlecode.lanterna.gui2.Window;
import tgpr.bank.model.Access;
import tgpr.bank.model.Account;
import tgpr.bank.model.User;
import tgpr.bank.view.AccessAccountClientView;
import tgpr.framework.Controller;

import java.util.List;

public class AccessAccountClientController extends Controller {
    private final AccessAccountClientView view;
    private User user;

    public AccessAccountClientController(User user) {

        this.user = user;
        view=new AccessAccountClientView(this,user);
    }

    @Override
    public Window getView() {
        return view;
    }
    public List<Account> getAccount(){
        return Account.getAllAccount(user.getEmail());
    }
    public List<Account> getAccountNoAccess(){
        return Account.getAccountNoAccess(user.getEmail());
    }

    public Access isHolder(int userid,int accountid){
        return Account.isHolder(userid,accountid);
    }
}

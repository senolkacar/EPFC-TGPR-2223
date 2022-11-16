package tgpr.bank.controller;

import com.googlecode.lanterna.gui2.Window;
import tgpr.bank.model.Access;
import tgpr.bank.model.Account;
import tgpr.bank.model.User;
import tgpr.bank.view.DisplayAccountAccessView;
import tgpr.framework.Controller;

import java.util.List;

public class DisplayAccountAccessController extends Controller {
    private final DisplayAccountAccessView view;
    private Account account;
    private User user;

    public DisplayAccountAccessController(Account account){
        this.account=account;
        view = new DisplayAccountAccessView(this,account);
    }
    public void delete(int userId,int accountId){
        account.deleteAccess(userId,accountId);
    }

    public void  delete(int accountID,String type){
        Account.getById(accountID).addAccess(accountID,user.getEmail(),type);
    }
    public Access isHolder(int userid,int accountid){
        return Account.isHolder(userid,accountid);
    }
    public void  update(int account ,String type){
        Account.getById(account).updateAccess(account,user.getId(),type);

    }
    @Override
    public Window getView() {
        return view;
    }
}

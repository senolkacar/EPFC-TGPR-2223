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
    private Account account;

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
    public void DeleteAccess(Account account){

        askConfirmation("Do you want to remove this account from your Acces? " + account.getIban(), "Remove favourite");
            account.deleteAccess(user.getId(),account.getId());
            account.reload();
            view.reloadData();
            view.reloadInfo();

    }
    public void showAccountAccess(Account account){
       navigateTo(new DisplayAccessAccountController(account));
    }


    public void addAccess(int accountID,String type){

        Account.getById(accountID).addAccess(accountID,user.getEmail(),type);
    }
    public Access isHolder(int userid,int accountid){
        return Account.isHolder(userid,accountid);
    }
}

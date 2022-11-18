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
    public static Controller previousController;

    public DisplayAccountAccessController(Account account,User user){
        this.account=account;
        this.user=user;
        view = new DisplayAccountAccessView(this,account);
    }
    public void delete(int accountId,String type){

            account.deleteAccess(user.getId(), accountId,type);
            view.close();
            navigateTo(previousController);
        }


   // public void  delete(int accountID,String type){
   //     Account.getById(accountID).addAccess(accountID,user.getEmail(),type);
   // }
    public Access isHolder(int userid,int accountid){
        return Account.isHolder(userid,accountid);
    }
    public void  update(int accountId ,String type){
        account.updateAccess(accountId,user.getId(),type);
        view.close();
        navigateTo(previousController);

    }
    public void showErrorUpdate(){
        showError("you can update holder access  with only one holder ");
    }
    public  void  showErrorDelete(){
        showError("you can delete holder access  with only one holder");
    }
    public void DeleteAccess(Account account,User user,String type){

        askConfirmation("Do you want to remove this account from your Acces? " + account.getIban(), "Remove favourite");
        account.deleteAccess(user.getId(),account.getId(),type );
        account.reload();



    }
    @Override
    public Window getView() {
        return view;
    }


    public void close() {
    }
}

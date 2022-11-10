package tgpr.bank.controller;


import tgpr.bank.model.Account;
import tgpr.bank.model.Category;
import tgpr.bank.view.AccountDetailsView;
import tgpr.framework.Controller;
import com.googlecode.lanterna.gui2.Window;

import java.util.List;


public class AccountDetailsController extends Controller {
    private final AccountDetailsView view;
    private Account account;
    private Category category;


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
    public List<Category> getCategory() {
        return Category.getCategories(getAccount());
    }

    public List<Category> getCategoryUses(Category category) {
        return Category.getUsesCategory(getAccount(),category);
    }
    public void editCategory(Category category) {
        navigateTo(new DisplayCategoryController(  category));
    }

    public void showError(){
            showError("you may not edit a system category !");
    }
    public void add(String name, int idAccount){
        account.addCategory(name, idAccount);
    }

    public void close(){
        view.close();
    }

    public void addFavourite(int accountID){
        account.addFavourite(accountID);
    }

    public List<Account> getFavorites(){

        return account.getFavorites();
    }

    public List<Account> getFavoritesNotListed(){
        return account.getFavoritesNotListed();
    }

    public void DeleteFavouriteAccount(Account account){
        if(askConfirmation("Do you want to remove this account from your favorite? " + account.getIban(), "Remove favourite")){
            account.delete();
            account.reload();
            view.reloadDataFav();
            view.reloadInfo();

        }


    }
}


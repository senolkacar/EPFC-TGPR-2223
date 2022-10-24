package tgpr.bank.model;

import java.time.LocalDate;
import java.util.List;

public class Client extends User {
    private List<Account> AccountsList;
    private Account account;
    private List<Account> FavoritListAccout;

    public List<Account> getAccountsList() {
        return AccountsList;
    }

    public void setAccountsList(List<Account> accountsList) {
        AccountsList = accountsList;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<Account> getFavoritListAccout() {
        return FavoritListAccout;
    }

    public void setFavoritListAccout(List<Account> favoritListAccout) {
        FavoritListAccout = favoritListAccout;
    }

    public void AddFavoriteAccount (Account account){
        this.FavoritListAccout.add(account);
    }



}

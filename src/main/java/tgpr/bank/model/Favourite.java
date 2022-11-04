package tgpr.bank.model;

import tgpr.framework.Model;
import tgpr.framework.Params;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Favourite extends Model {
    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getAccountid() {
        return accountid;
    }

    public void setAccountid(int accountid) {
        this.accountid = accountid;
    }

    private int userid ;
    private int accountid ;


    @Override
    protected void mapper(ResultSet rs) throws SQLException {
        userid= rs.getInt("user") ;
        accountid=rs.getInt("account");

    }
   public List <Account> favouritAccounts (){
        return queryList(Account.class,"select favourite.account, iban,title,account.type from favourite,account,user " +
                "where favourite.user=user.id and favourite.account=account.id and favourite.user=:loggeduser",
                new Params("loggeduser",Security.getLoggedUser().getId()));

   }


    @Override
    public void reload() {

    }
}

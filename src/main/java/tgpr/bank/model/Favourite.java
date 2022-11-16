package tgpr.bank.model;

import org.springframework.util.backoff.BackOff;
import tgpr.framework.Model;
import tgpr.framework.Params;

import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class Favourite extends Model {
    @Override
    public String toString() {
        return "Favourite{" +
                "userid=" + userid +
                ", accountid=" + accountid +
                '}';
    }

    private int userid;
    private int accountid;

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


    public Favourite(int userid, int accountid) {
        this.userid = userid;
        this.accountid = accountid;
    }


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

    public static Favourite getFavAcc(Integer userId,Integer accountId){
        return queryOne(Favourite.class,"select * from favourite where user=:userid and account=:accountid",new Params().add("userid", userId).add("accountid",accountId));
    }
    public static boolean alreadyInFav(Integer userId, Integer accountId){
        return getFavAcc(userId,accountId)!=null;
    }
    public static void addToFav(Integer userId, Integer accountId){
        execute("insert into favourite(user,account) values(:userid,:accountid)",new Params().add("userid",userId).add("accountid",accountId));
    }



    @Override
    public void reload() {
        reload("select * from favourite where user=:userid", new Params("user", userid));
    }


}

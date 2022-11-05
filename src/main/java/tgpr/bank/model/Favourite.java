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
    private int userid ;
    private int accountid ;
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
    public Favourite() {

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
    public List<Account> favouritAccounts (){
        return queryList(Account.class,"select iban,titre as ' ' ,type  from favourite,account,user " +
                        "where favourite.user=user.id and favourite.account=account.id and favourite.user=:loggeduser",
                new Params("loggeduser",Security.getLoggedUser().getId()));

    }


    @Override
    public void reload() { reload( "select * from favourite where user=:userid", new Params("user",userid));}
    public  Favourite getByAccount(int accountid){
        return queryOne(Favourite.class,"select * from favourite where account=:accountid",new Params("account",accountid));
    }

    public  List<Account> getFavoriteAccounts(){
        return queryList(Account.class,"select id,iban,title,floor,type,saldo from account JOIN favourite on account.id = favourite.account where favourite.user =:loggeduser;",new Params("loggeduser",userid));
    }


    //    public boolean save(){
//        int c;
//        Favourite f=getByAccount(accountid);
//        String sql;
//        if(f==null){
//            sql="insert into favourite (account) "+"values(:accountid)";
//        }else{
//            sql="update favourite set account=:accountid where account=:accountid";
//        }
//        c=execute(sql, new Params().add("account",accountid));
//        return c==1;
//    }
    public List<Account> getPossibleFavorites (){
        return queryList(Account.class,"select  *  from account join transfer  " +
                        "on account.id = transfer.target_account join favourite on transfer.created_by = favourite.user " +
                        "where transfer.created_by =:loggeduser and transfer.state='executed' " +
                        "and account.id not in(select favourite.account " +
                        "from favourite) ",
                new Params("loggeduser",Security.getLoggedUser().getId()));

    }
    public void addFavourite() {
        execute("insert into favourite (user, account) values ((select id from account where id=:idAccount), (select id from user where id=:loggeriuser))   " +
                "from account join transfer join favourite ", new Params()
                .add("idAccount", accountid)
                .add("idUser", userid));
    }
}

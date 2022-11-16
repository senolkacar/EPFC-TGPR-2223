package tgpr.bank.model;

import tgpr.framework.Model;
import tgpr.framework.Params;

import java.lang.constant.Constable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.sql.*;

public class Account extends Model{

    private int id;
    private String iban;
    private String title;
    private double floor;
    private String type;
    private double saldo;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getFloor() {
        return floor;
    }

    public void setFloor(double floor) {
        this.floor = floor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    @Override
    protected void mapper(ResultSet rs) throws SQLException {
        id = rs.getInt("id");
        iban = rs.getString("iban");
        title = rs.getString("title");
        floor = rs.getDouble("floor");
        type = rs.getString("type");
        saldo = rs.getDouble("saldo");
    }

    @Override
    public void reload() {
        //il faudra peut être changer cette partie
        reload("select * from account",new Params());
    }
    public static Access isHolder(int userid,int accountid){
        String sql="Select * from access where user=:userid and account=:accountid";
        return queryOne(Access.class,sql,new Params("userid",userid).add("accountid",accountid));
    }


    public static Account getById(int id) {
        return queryOne(Account.class, "select * from account where id=:id", new Params("id", id));
    }

    public static List<Account> getAll() {
        return queryList(Account.class,"SELECT * FROM account where id in(Select account from access,user where user.id=access.user and user.email=:loggedUser)",
                new Params("loggedUser",Security.getLoggedUser().getEmail()));
    }


    public void deleteAccess(int id ,int accountId) {
        if(numberofhorder(accountId)==1) {
            execute("delete from access where access.type=\"proxy\" and  account=:accountId and user=:userId", new Params("userId", id).add("accountId", accountId));
        }
        else{
            execute("delete from access where account=:accountId and user=:userId", new Params("userId", id).add("accountId", accountId));

        }
    }
    public void update(int accountId, String type){
        if(numberofhorder(accountId)>1){
            execute("update access set type=:type where access.account=:accountId",new Params(":type",type).add("accountId",accountId) );
        }
    }
    public Integer numberofhorder(int accountid){
       return queryScalar(Integer.class,"select count (access.user),access.account from access where access.type ='holder' and access.account=:accountid " +
               "group by access.account " +
              "having count (access.user >1) ",new Params("accountid",accountid));
    }
    public static List<Account> getAllAccount(String email){
        return  queryList(Account.class,"SELECT * FROM account where account.id " +
                "in ( select access.account from access where access.user=:iduser) "
                ,new Params("iduser",User.getByEmail(email).getId()));
    }
    public  static List<Account> getAccountNoAccess(String email){
        return  queryList(Account.class,"SELECT * FROM account where account.id " +
                        "not in ( select access.account from access where access.user=:iduser) "
                ,new Params("iduser",User.getByEmail(email).getId()));
    }

    public boolean save() {
        int c;
        Account a = getById(id);
        String sql;
        if (a == null)
            sql = "insert into account ( iban, title, floor, type, saldo) " +
                    "values (:iban,:title,:floor,:type,:saldo)";
        else
            sql = "update account set  iban=:iban, title=:title, floor=:floor ,type=:type ,saldo=:saldo " +
                    " where id=:id";
        c = execute(sql, new Params()
                .add(  "iban", iban)
                .add("title", title)
                .add("floor", floor)
                .add("type", type)
                .add("saldo", saldo));
        return c == 1;
    }
    public void deleteAccess(int accountId, String email){
        if(numberofhorder(accountId)==1){
            execute("delete from access where access.type=\"proxy\" and  account=:accountId and user=:userId", new Params("userId", id).add("accountId", accountId));
        }
        else{
            execute("delete from access where account=:accountId and user=:userId", new Params("userId", User.getByEmail(email).getId()).add("accountId", accountId));

        }
        }

    public void addAccess(int accountId, String email, String type){
        execute("insert into access (user,account,type) values(:iduser,:idaccount,:type)",new Params()
                .add("idaccount",accountId)
                .add("type",type)
                .add("iduser",User.getByEmail(email).getId()));

    }
    public void updateAccess(int accountId,int userId,String type){
        if(numberOfHolder(accountId)>1){
            update(accountId,userId,type);
        }

    }

    public void update(int accountId,int userId,String type) {
        String sql = "update access set type=:type where access.account=:accountId and userId";
        execute(sql, new Params()
                .add("accountId",accountId)
                .add("userId", userId)
                .add("type",type));
    }

    public  Integer numberOfHolder(int accountId){
       return queryScalar(Integer.class,"select count(*) from access where access.type='holder' and access.account=:accountId group by access.account",new Params("accountId",accountId));

    }
    @Override
    public String toString() {
        return "account{" +
                "id=" + id +
                ", iban='" + iban + '\'' +
                ", title='" + title + '\'' +
                ", floor=" + floor +
                ", type='" + type + '\'' +
                ", saldo=" + saldo +
                '}';
    }
    public String toStringfavouritaccount() {
        return iban+" " + title+ " " + type;
    }

}

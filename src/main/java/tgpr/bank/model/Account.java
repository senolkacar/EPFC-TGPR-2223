package tgpr.bank.model;

import tgpr.framework.Error;
import tgpr.framework.Model;
import tgpr.framework.Params;

import java.lang.constant.Constable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.text.NumberFormat;
import java.util.List;
import java.sql.*;
import java.util.Objects;

public class
Account extends Model{

    private int id;
    private String iban;
    private String title;
    private double floor;
    private String type;
    private double saldo;


    public static List<Account> getAll() {
        return queryList(Account.class, "SELECT * FROM account where id in (Select account from access,user where user.id=access.user and user.email=:loggedUser)",
                new Params("loggedUser", Security.getLoggedUser().getEmail()));
    }


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

    public String transformInEuro(double montant) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String moneyString = formatter.format(montant);
        return (moneyString);
    }

    // ajout de la fonciton pour afficher en euro
    public String getSaldoWithEuroSign() {
        return transformInEuro(saldo);
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
        reload("select * from account", new Params());
    }
    public static Access isHolder(int userid,int accountid){
        String sql="Select * from access where user=:userid and account=:accountid";
        return queryOne(Access.class,sql,new Params("userid",userid).add("accountid",accountid));
    }

    public void addCategory(String name, int idAccount) {
        Category category = Category.getByAccount(Security.getLoggedUser().getId(), name);
        String vide = "";
        if (category == null && name != vide
        ) {


            execute("insert into category ( name,account) " +
                    "values (:name,:account )", new Params()

                    .add("name", name)
                    .add("account", idAccount));
        }
    }
    public static boolean isExternal(int compte){
        List<Account> liste = Account.getAll();

        boolean result= false;

        for (Account a : liste
             ) {
            if (a.getType().equals("external") && a.getId()==compte){
                result=true;
            }
        }
        return result;
    }

    public void addFavourite(int accountid) {
        execute("insert into favourite (user, account) values (:loggeduser,:idAccount)", new Params()
                .add("idAccount", accountid)
                .add("loggeduser", Security.getLoggedUser().getId()));
    }

    public List<Account> getFavorites() {
        return queryList(Account.class, "select * from favourite join account on favourite.account=account.id where user=:loggeduser", new Params("loggeduser", Security.getLoggedUser().getId()));
    }

    public List<Account> getFavoritesNotListed() {
        return queryList(Account.class, "select * from account where account.id NOT IN(select favourite.account from favourite where user=:loggeduser) AND account.id NOT IN(Select account from access,user where user.id=access.user and user.email=:email) and account.id in(select transfer.target_account from transfer)", new Params("loggeduser", Security.getLoggedUser().getId()).add("email", Security.getLoggedUser().getEmail()));
    }

//    public void delete() {
//        execute("delete from favourite where account=:id", new Params("id", id));
//
//    }




    public static Account getById(int id) {
        return queryOne(Account.class, "select * from account where id=:id", new Params("id", id));
    }


    public boolean delete() {
        int c = execute("delete from favourite where account=:id", new Params("id", id));
        return c == 1;
    }

    public void deleteAccess(int id ,int accountId,String type) {
        if(numberofhorder(accountId)==1 && type== "holder") {
            execute("delete from access where access.type=:type and  account=:accountId and user=:userId", new Params("userId", id).add("accountId", accountId).add("type",type));
        }
        else{
            execute("delete from access where  access.type=:type and account=:accountId and user=:userId", new Params("userId", id).add("accountId", accountId));

        }
    }
    public void update(int accountId, String type){
        if(numberofhorder(accountId)>1){
            execute("update access set type=:type where access.account=:accountId",new Params(":type",type).add("accountId",accountId) );

    }}
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
        if(numberOfHolder(accountId)>1) {
            update(accountId, userId, type);
        }

    }

    public void update(int accountId,int userId,String type) {
        String sql = "update access set type=:type where access.account=:accountId and access.user=:userId";
        execute(sql, new Params()
                .add("accountId",accountId)
                .add("userId", userId)
                .add("type",type));
    }

    public  int numberOfHolder(int accountId){
       return queryScalar(Integer.class,"select count(*) from access where access.type='holder' and access.account=:accountId group by access.account",new Params("accountId",accountId));

    }
    @Override
    public String toString() {
        return getIban() + " | " +
                getTitle() + " | " +
                getType() + " | " +
                getSaldo();
    }

    public String toStringfavouritaccount() {
        return iban+" " + title+ " " + type;
    }

    public static List<Account> getFavouriteAccounts(){
        return queryList(Account.class,"select * from favourite,account,user " +
                        "where favourite.user=user.id and favourite.account=account.id and favourite.user=:loggeduser",
                new Params("loggeduser",Security.getLoggedUser().getId()));
    }

    private static List<Account> getUserOtherAccounts(Integer selectedAccountID){
        return queryList(Account.class,"SELECT * FROM account where id !=:selectedAccountID and id in(Select account from access,user where user.id=access.user and user.email=:loggedUser)",
                new Params()
                        .add("loggedUser",Security.getLoggedUser().getEmail())
                        .add("selectedAccountID",selectedAccountID));
    }

    public static List<Account> getTargetAccounts(List<Account> listFavourites, List<Account> loggedUserAccounts){
        List<Account> listToReturn = new ArrayList<>();
        listToReturn.addAll(loggedUserAccounts);
        listToReturn.addAll(listFavourites);
        return listToReturn;
    }

    public static List<Account> getTargetAccounts(Integer selectedAccountID){
        return getTargetAccounts(getFavouriteAccounts(),getUserOtherAccounts(selectedAccountID));
    }

    public static List<String> getTargetAccountsToString(List<Account> loggedUserAccounts, List<Account> listFavourites){
        List<String> listToReturn = new ArrayList<>();
        for(int i = 0; i<loggedUserAccounts.size();++i){
            listToReturn.add(loggedUserAccounts.get(i).iban + " | " +
                            loggedUserAccounts.get(i).title + " | " +
                            loggedUserAccounts.get(i).type + " | " +
                            loggedUserAccounts.get(i).saldo);
        }
        for(int i=0; i<listFavourites.size();++i){
            listToReturn.add(listFavourites.get(i).iban + " | " + listFavourites.get(i).title + " | favourite");
        }
        return listToReturn;
    }

    public static List<String> getTargetAccountsToString(Integer selectedAccountID){
        return getTargetAccountsToString(getUserOtherAccounts(selectedAccountID),getFavouriteAccounts());
    }

    public static void newExternalAccount(String iban, String title){
        execute("insert into account(iban,title,type) " +
                "values(:iban,:title,'external')",new Params().add("iban",iban).add("title",title));
    }

    public static Account getByIban(String iban){
        return queryOne(Account.class,"select * from account where account.iban=:iban",new Params("iban",iban));
    }

    public static boolean accountAlreadyExistsInDB(String iban) {
        return getByIban(iban)!=null;
    }

    public static Account getLastCreatedAccount(){
        return queryOne(Account.class,"select * from account where account.id = (select MAX(id) from account)");
    }

    public static boolean isExternalAccount(String iban){
        return Objects.equals(getByIban(iban).type, "external");
    }

    public static void updateAccountSaldo(Integer accountId, Double saldo){
        execute("UPDATE account SET saldo =:saldo where account.id = :accountid",new Params().add("accountid",accountId).add("saldo",saldo));
    }
}


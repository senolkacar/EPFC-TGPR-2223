package tgpr.bank.model;

import tgpr.framework.Model;
import tgpr.framework.Params;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;



public class Category extends Model {
    private static Integer idAccount;
    public enum Fields {
        name, id
    }
    private int id;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAccount() {
        return account;
    }

    private String name;
    private boolean type;
    private int account;
    public Category() {
    }

    public Category(String name, boolean account) {
        this.name = name;
        this.type = account;
    }

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category(int id, String name, boolean account) {
        this.id = id;
        this.name = name;
        this.type = account;
    }

    public static Integer getIdAccount(Account account) {
        return account.getId();
    }

    public static int getAccountId(Account account) {
        return account.getId();
    }



    public boolean isSystem() {
        return type;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public void setAccount(boolean account) {
        this.type = account;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    protected void mapper(ResultSet rs) throws SQLException {


        name = rs.getString("name");
        type = rs.getBoolean("account");
        id = rs.getInt("id");
        idAccount = rs.getInt("account");


    }

    public void delete(Category category) {
        String sql ="delete from category where account is not null && category.id=:categoryID";
        execute(sql, new Params()
                .add("name", name)
                .add("categoryID", category.getId()));

    }
    public void update(String name,Category category) {
        String sql = "update category set name=:name where category.id=:categoryID";
        execute(sql, new Params()
                .add("name", name)
                .add("categoryID", category.getId()));
    }

    public static List<Category> getAll() {
        return queryList(Category.class, "SELECT * FROM category order by name ");

    }

    public String toString() {
        return this.name;
    }

    @Override
    public void reload() {
        reload("select  * from category   ", new Params("account ", idAccount));
    }


    public static Category getByAccount(int idAccount, String name) {
        return queryOne(Category.class, "select * from category where   account=:account and name=:name", new Params()
                .add("account", idAccount)
                .add("name", name));
    }



    public static List<Category> getCategories(Account account) {
        return queryList(Category.class, "select * from category where account=:account  or account is null   order by name                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   ", new Params()
                .add("account", account.getId())
        );
    }

    public static List<Category> getUsesCategory(Account account,Category category) {
        String sql = "SELECT * FROM category join transfer_category on category.id = transfer_category.category where transfer_category.account=:accountID and transfer_category.category=:categoryID";
        return queryList(Category.class, sql, new Params()
                .add("accountID", account.getId())
                .add("categoryID", category.getId())
        );

    }
}


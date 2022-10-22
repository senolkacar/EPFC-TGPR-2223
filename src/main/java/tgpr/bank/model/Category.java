package tgpr.bank.model;

import tgpr.framework.Model;
import tgpr.framework.Params;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import tgpr.bank.model.Category;


public class Category extends Model {
    private int cmp;
    private int id;
    private String name;
     private boolean account;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    private Integer type;

    public Category() {
    }

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category(int id, String name, boolean account) {
        this.id = id;
        this.name = name;
        this.account = account;
    }

    public boolean isAccount() {
        return  account;
    }

    public void setAccount(boolean account) {
        this.account = account;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }





    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + account +
                '}';
    }

    @Override
    protected void mapper(ResultSet rs) throws SQLException {



        name = rs.getString("name");
        account = rs.getBoolean("account");




    }

    public static List<Category> getAll() {
        return queryList(Category.class,"SELECT * FROM category order by name ");

    }

    @Override
    public void reload() {
        reload("select  * from category   ", new Params("account ", account));
    }
    public static Category getByAccount(int account) {
        return queryOne(Category.class, "select * from category where account is null ||  account=:account", new Params("account", account));
    }


    public static Object getUses(String name, int account) {
        return queryOne(Category.class,"SELECT COUNT(*) from category WHERE name =:name and account=:account" , new Params("account", account));
    }


    public static Object getUses(Category category) {
        return queryOne(Category.class,"SELECT COUNT(*) from category WHERE name =:name and account=:account" , new Params("account", getUses("gift",2)));
    }
}
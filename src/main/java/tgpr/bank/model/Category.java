package tgpr.bank.model;

import org.springframework.util.Assert;
import tgpr.framework.Model;
import tgpr.framework.Params;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;




public class Category extends Model {
    private static Integer idAccount;


    private int id;
    private String name;
    private boolean type;
    int account;

    int cmp;

    public int getCmp() {
        return cmp;
    }

    public void setCmp(int cmp) {
        this.cmp = cmp;
    }

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

    public static Account getAccountInfo(int accountID) {
        return queryOne(Account.class, "select * from account where id = :id", new Params("id", accountID));
    }

    public static Account getAccount(int accountID) {
        Account account = getAccountInfo(accountID);
        return account;
    }

    public boolean isSystem() {
        return type;
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public void setAccount(boolean account) {
        this.type = account;
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
                "name='" + name + '\'' +
                ", type=" + type +
                '}';
    }

    @Override
    protected void mapper(ResultSet rs) throws SQLException {


        name = rs.getString("name");
        type = rs.getBoolean("account");
        id = rs.getInt("id");
        idAccount = rs.getInt("account");


    }

    public boolean save() {
        int c;
        Category category = getByAccount(idAccount);
        String sql;
        if (category == null)
            sql = "insert into category (id, name, account) " +
                    "values (:id,:name,:account )";

        else
            sql = "update category set name=:name " +
                    " where account=:account";
        c = execute(sql, new Params()
                .add("id", id)
                .add("name", name)
                .add("account", idAccount));

        return c == 1;
    }

    public boolean delet() {
        int c = execute("delete from category  where account is not null && account=:account && name=:name", new Params("name", name).add("account", idAccount));
        return c == 1;
    }

    public void delete() {
        Assert.isTrue(Security.isAdmin(), "");
        execute("delete from gategory where name=:name && account=:account", new Params("name", name).add("account", idAccount));

        int c = execute("delete from  where pseudo=:pseudo", new Params("name", name));
        Assert.isTrue(c == 1, "System");
    }


    @Override
    public boolean equals(Object o) {
        // s'il s'agit du même objet en mémoire, retourne vrai
        if (this == o) return true;
        // si l'objet à comparer est null ou n'est pas issu de la même classe que l'objet courant, retourne faux
        if (o == null || getClass() != o.getClass()) return false;
        // transtype l'objet reçu en Member
        Account member = (Account) o;
        // retourne vrai si les deux objets ont le même pseudo
        // remarque : cela veut dire que les deux objets sont considérés comme identiques s'ils on le même pseudo
        //            ce qui a du sens car c'est la clef primaire de la table. Attention cependant car cela signifie
        //            que si d'autres attributs sont différents, les objets seront malgré tout considérés égaux.
        return idAccount.equals(Category.idAccount);
    }

    @Override
    public int hashCode() {
        // on retourne le hash code du pseudo qui est "unique" puisqu'il correspond à la clef primaire
        return Objects.hash(idAccount);
    }


    public static List<Category> getAll() {
        return queryList(Category.class, "SELECT * FROM category order by name ");

    }

    @Override
    public void reload() {
        reload("select  * from category   ", new Params("account ", idAccount));
    }

    //public static Category getByAccount(int account) {
    //    return queryOne(Category.class, "select * from category where account is null ||  account=:account", new Params("account", account));
    //}

    public static Category getByAccount(int idAccount) {
        return queryOne(Category.class, "select * from category where   account=:account", new Params("account", idAccount));
    }


    public static Category getUses(String name, int idAccount) {
        String sql = "SELECT COUNT(category.name) from category  WHERE category.account= :account and name=:name and (category.account in (SELECT transfer_category.account FROM transfer_category ))   GROUP by category.name;";
        return queryOne(Category.class, sql, new Params("name", name).add("account", idAccount));
    }


    public static Object getUses(Category category) {
        return queryOne(Category.class, "SELECT COUNT(*) from category WHERE name =:name and account=:account", new Params("account", getUses("gift", 2)));
    }


    public static List<Category> getCategories(Account account) {
        return queryList(Category.class, "select * from category where account=:account  or account is null   order by name                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   ", new Params()
                .add("account", account.getId())
        );
    }

    public static List<Category> getUsesCategory(Account account) {
        String sql = "SELECT COUNT(*) FROM transfer_category,category where category.id=transfer_category.category and transfer_category.account=:account  GROUP by transfer_category.category, category.name";
        return queryList(Category.class, sql, new Params()
                .add("account", account.getId())

        );

    }

    public static List<Category> Uses(Account account) {
        List<Category> Uses = getUsesCategory(account);
        List<Integer> list = new ArrayList<>();
        for (Category val : Uses) {
            //list.add(val);

        }
        return Uses;
    }
}


package tgpr.bank.model;

import tgpr.framework.Model;
import tgpr.framework.Params;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Category extends Model {

    private int id;
    private String name;
    private  Integer  account;
    private  final  Integer idAccount = getById(Security.getLoggedUser().getId());

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", account=" + account +
                '}';
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

    public Integer getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }
    public static List<Category> getAll(){
        return queryList(Category.class,"select * from Category where account is null or account=:idAccount",new Params("idAccount",idAccount)));
    }


    // SELECT COUNT(category.id) FROM category,transfer_category WHERE transfer_category.account=1 AND transfer_category.category=category.id GROUP by category.id

    @Override
    protected void mapper(ResultSet rs) throws SQLException {
        this.name= rs.getString("name");
        this.id= rs.getInt("id");
        this.account= rs.getInt("account");

    }

    @Override
    public void reload() {
        
    }
}

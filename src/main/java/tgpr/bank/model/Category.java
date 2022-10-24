package tgpr.bank.model;

import tgpr.framework.Model;
import tgpr.framework.Params;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Category extends Model {

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
    private int account;

    public String toString() {
        return this.name;
    }


    @Override
    protected void mapper(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.name = rs.getString("name");
        this.account = rs.getInt("account");
    }

    @Override
    public void reload() {
        String sql = "select * from category where id = :id";
        reload(sql,new Params().add("id",this.id));

    }
}

package tgpr.bank.model;

import tgpr.framework.Model;
import tgpr.framework.Params;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Access extends Model {
    private Integer user;
    private Integer account;
    private String type;

    public enum Fields {user,account,type};

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public Integer getAccount() {
        return account;
    }

    public void setAccount(Integer account) {
        this.account = account;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    protected void mapper(ResultSet rs) throws SQLException {
        user = rs.getInt("user");
        account = rs.getInt("account");
        type = rs.getString("type");
    }

    @Override
    public void reload() {
        reload("select * from access",new Params());
    }
}

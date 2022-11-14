package tgpr.bank.model;

import tgpr.framework.Model;
import tgpr.framework.Params;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Access extends Model {
    private Integer user;
    private Integer account;
    private String type;
    private boolean typet;

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

    public boolean isTypet() {
        return typet;
    }

    public void setTypet(boolean typet) {
        this.typet = typet;
    }

    public void setType(String type) {
        this.type = type;
    }
    public boolean isHolder(){
        return typet;
    }


    @Override
    protected void mapper(ResultSet rs) throws SQLException {
        user = rs.getInt("user");
        account = rs.getInt("account");
        type = rs.getString("type");
        typet = rs.getBoolean("type");
    }

    @Override
    public void reload() {
        reload("select * from access",new Params());
    }

    public String toString(){
        return this.type;
    }
}

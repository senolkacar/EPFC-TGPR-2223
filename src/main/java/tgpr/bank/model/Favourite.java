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

    private int userid;
    private int accountid;

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
        userid = rs.getInt("user");
        accountid = rs.getInt("account");

    }


    @Override
    public void reload() {
        reload("select * from favourite where user=:userid", new Params("user", userid));
    }


}

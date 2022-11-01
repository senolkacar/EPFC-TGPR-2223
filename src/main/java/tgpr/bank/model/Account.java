package tgpr.bank.model;

import tgpr.framework.Model;
import tgpr.framework.Params;
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


    public static Account getById(int id) {
        return queryOne(Account.class, "select * from account where id=:id", new Params("id", id));
    }

    public static List<Account> getAll() {
        return queryList(Account.class,"SELECT * FROM account where id in(Select account from access,user where user.id=access.user and user.email=:loggedUser)",
                new Params("loggedUser",Security.getLoggedUser().getEmail()));
    }

    public boolean delete() {
        int c = execute("delete from account where id=:id", new Params("id", id));
        return c == 1;
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

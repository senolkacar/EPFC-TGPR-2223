package tgpr.bank.model;

import tgpr.framework.Model;
import tgpr.framework.Params;
import java.sql.*;
import java.util.List;

public class Account extends Model{

    private int id;
    private String iban;
    private String title;
    private double floor;
    private String type;
    private double saldo;

    public Account(){

    }

    public Account(int id , String iban , String title , double floor , String type , double saldo){
        this.id=id;
        this.iban=iban;
        this.title=title;
        this.floor=floor;
        this.type=type;
        this.saldo=saldo;
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

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    @Override
    public void reload() {
        reload("select * from account where id=:id", new Params("id", id));
    }

    public static Account getById(int id) {


        return queryOne(Account.class, "select * from account where id=:id", new Params("id", id));
    }

    protected void mapper(ResultSet resultSet) throws SQLException {
        id = resultSet.getInt("id");
        iban = resultSet.getString("iban");
        title = resultSet.getString("title");
        floor = resultSet.getDouble("floor");
        type = resultSet.getString("type");
        saldo = resultSet.getDouble("saldo");
    }

    public static List<Account> getAll() {

        return queryList(Account.class, "select* from account order by id");
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





}

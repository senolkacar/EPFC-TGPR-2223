package tgpr.bank.model;

import tgpr.framework.Model;
import tgpr.framework.Params;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Account extends Model {

    private int id;
    private String iban;
    private String title;
    private double floor;
    private String type;
    private double saldo;



    public static List<Account> getAll() {
        return queryList(Account.class,"SELECT * FROM account where id in(Select account from access,user where user.id=access.user and user.email=:loggedUser)",
                new Params("loggedUser",Security.getLoggedUser().getEmail()));
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

    public String toString() {
        return this.iban+" - "+this.title;
    }

    public boolean add(String name, int idAccount) {
        int c;
        Category category = Category.getByAccount(Security.getLoggedUser().getId(),name);
        String sql;
        if (category == null)
            sql = "insert into category ( name,account) " +
                    "values (:name,:account )";

        else
            sql = "update category set name=:name " +
                    " where account=:account";
        c = execute(sql, new Params()
                .add("name", name)
                .add("account", idAccount));

        return c == 1;
    }
}
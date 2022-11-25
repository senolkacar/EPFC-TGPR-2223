package tgpr.bank.model;

import java.sql.SQLException;
import java.time.LocalDate;

import tgpr.framework.Model;
import tgpr.framework.Params;
import tgpr.framework.Tools;

import java.sql.ResultSet;
import java.util.List;


public class User extends Model {


    private Integer id;
    private String type;
    private String email;
    private String password;
    private LocalDate birth_date;
    private String last_name;
    private String first_name;
    private Integer agency;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, String last_name, String first_name) {
        this.email = email;
        this.password = password;
        this.last_name = last_name;
        this.first_name = first_name;
    }

    public User(String email, String password, LocalDate birth_date, String last_name, String first_name, Integer agency) {
        this.email = email;
        this.password = password;
        this.birth_date = birth_date;
        this.last_name = last_name;
        this.first_name = first_name;
        this.agency = agency;
    }

    public enum Fields {
        email, Password, birth_date, last_name, first_name
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(LocalDate birth_date) {
        this.birth_date = birth_date;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public Integer getAgency() {
        return agency;
    }

    public void setAgency(Integer agency) {
        this.agency = agency;
    }

    public User(){
    }

    public User(String email) {
        this.email = email;
    }

    public boolean isAdmin() {
        return "admin".equals(type);
    }

    public boolean isManager() {
        return "manager".equals(type);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", birthdate=" + birth_date +
                ", lastname='" + last_name + '\'' +
                ", firstname='" + first_name + '\'' +
                ", agency=" + agency +
                '}'+"\n";
    }

    public static User checkCredentials(String email, String password) {
        var user = User.getByEmail(email);
        if (user != null && user.password.equals(Tools.hash(password)))
            return user;
        return null;
    }

    public static User createClient(String email, String password, String last_name, String first_name, LocalDate birth_date, Integer agency) {
        var user = new User(email, password, birth_date, last_name, first_name, agency);
        user.type = "client";
        return user;
    }

    protected void mapper(ResultSet rs) throws SQLException {
        email = rs.getString("email");
        id = rs.getInt("id");
        agency = rs.getInt("agency");
        birth_date = rs.getObject("birth_date", LocalDate.class);
        last_name = rs.getString("last_name");
        first_name = rs.getString("first_name");
        type = rs.getString("type");
        password = rs.getString("password");
    }
    @Override
    public void reload() {
        reload("select * from user where email=:email", new Params("email", email));
    }


    public static List<User> getAll() {
        return queryList(User.class, "select * from user order by email");
    }

    public static User getByEmail(String email) {
        return queryOne(User.class, "select * from user where email=:email", new Params("email", email));
    }
    public static String getById(int id){
        User s = queryOne(User.class, "select * from user where id=:id", new Params("id", id));
        return s.last_name + " " + Tools.ifNull(s.first_name, " ");
    }

    public User save() {
        int c;
        User u = getByEmail(email);
        String sql;
        if (u == null)
            sql = "insert into user (email, password, birth_date, last_name, first_name, agency, type) values (:email, :password, :birth_date, :last_name, :first_name, :agency, :type)";
        else
            sql = "update user set password=:password, last_name=:last_name,first_name=:first_name, birth_date=:birth_date,type=:type,agency=:agency where email=:email";

        c = execute(sql, new Params()
                .add("email", email)
                .add("birth_date", birth_date)
                .add("password", password)
                .add("last_name", last_name)
                .add("first_name", first_name)
                .add("type", type)
                .add("agency", agency));
        return this;
    }
    public boolean delete() {
        int c = execute("delete from user where email=:email", new Params("email", email));
        return c == 1;
    }
}
package tgpr.bank.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.cglib.core.Local;
import org.springframework.util.Assert;
import tgpr.framework.Model;
import tgpr.framework.Params;
import tgpr.framework.SortOrder;
import tgpr.framework.Tools;

public class User extends Model {
    private Integer id;
    private String type;
    private String email;
    private String password;
    private LocalDate birthdate;
    private String lastname;
    private String firstname;
    private Integer agency;

    private List<User> listUser;

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

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public Integer getAgency() {
        return agency;
    }

    public void setAgency(Integer agency) {
        this.agency = agency;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", birthdate=" + birthdate +
                ", lastname='" + lastname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", agency=" + agency +
                ", listUser=" + listUser +
                '}';
    }

    //Constructor client
    public User(Integer id, String type, String email, String password, LocalDate birthdate, String lastname, String firstname, Integer agency) {
        this.id = id;
        this.type = type;
        this.email = email;
        this.password = password;
        this.birthdate = birthdate;
        this.lastname = lastname;
        this.firstname = firstname;
        this.agency = agency;
    }

    //Constructor Admin
    public User(Integer id, String type, String email, String password, String lastname) {
        this.id = id;
        this.type = type;
        this.email = email;
        this.password = password;
        this.lastname = lastname;
    }


    //Constructor Manager
    public User(Integer id, String type, String email, String password, String lastname, String firstname) {
        this.id = id;
        this.type = type;
        this.email = email;
        this.password = password;
        this.lastname = lastname;
        this.firstname = firstname;
    }

    public static List<User> getAll() {
        return queryList(User.class,"select * from user");
    }

    public static User getByMail(String email){
        return queryOne(User.class,"select * from user where email= :email", new Params("email", email));
    }

    @Override
    protected void mapper(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.agency = rs.getInt("agency");
        this.birthdate = rs.getObject("birth_date", LocalDate.class);
        this.email = rs.getString("email");
        this.lastname = rs.getString("last_name");
        this.firstname = rs.getString("first_name");
        this.type = rs.getString("type");
        this.password = rs.getString("password");
    }

    @Override
    public void reload() {
        reload("Select * from user where email = :email",new Params("email", email));
    }




}

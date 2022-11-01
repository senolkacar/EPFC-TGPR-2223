package tgpr.bank.model;

import tgpr.framework.Model;
import tgpr.framework.Params;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Agency extends Model {
    private int id;
    private String name;
    private int managerid;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getManagerid() {
        return managerid;
    }


    @Override
    protected void mapper(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.name = rs.getString("name");
        this.managerid = rs.getInt("manager");
    }

    @Override
    public void reload() {
        String sql = "Select * from agency where manager = :id";
        reload(sql, new Params().add("id",this.managerid));
    }

    public static List<Agency> getAllAgency(){
        return queryList(Agency.class, "SELECT * FROM agency where manager = :id", new Params().add("id", Security.getLoggedUser().getId()));
    }

    public static List<User> getAllClients(Agency agency){
        return queryList(User.class, "SELECT * FROM user WHERE agency = :agency and type='client'", new Params().add("agency",agency.getId()));
    }

    public static List<User> getAllClientsFiltered(Agency agency, String filter){
        return queryList(User.class, "SELECT * FROM user WHERE agency = :agency and type='client' and (first_name like :filter or last_name like :filter or birth_date like :filter or email like :filter)", new Params().add("agency",agency.getId()).add("filter", "%"+filter+"%"));
    }

    public static Agency getAgencyName(int id){
        return queryOne(Agency.class, "SELECT * FROM agency WHERE id = :id", new Params().add("id",id));
    }

    public static Agency getAgencyIdByName(String name){
        return queryOne(Agency.class, "SELECT * FROM agency WHERE name = :name", new Params().add("name",name));
    }

    public static List<String> getAllAgencyName(List<Agency> agencies){
        List<String> agencyNames = new ArrayList<>();
        for (Agency agency : agencies) {
            agencyNames.add(agency.getName());
        }
        return agencyNames;
    }


    public String toString(){
        return this.name;
    }
}

package tgpr.bank.model;
import tgpr.framework.Params;

import java.util.List;

public class Manager extends User {
    public Manager() {
        super();
    }

   public Manager (String email,String password){
        super(email,password);
   }

   public Manager(String email,String password,String last_name,String first_name){
        super(email,password,last_name,first_name);
   }

   public static List<User> getAllManager(){
        return queryList(User.class, "SELECT * FROM user WHERE type = 'manager'");
   }

   public static User getManagerByEmail(String email){
        return queryOne(User.class, "SELECT * FROM user WHERE email=:email AND type = 'manager'", new Params().add("email", email));
   }

}

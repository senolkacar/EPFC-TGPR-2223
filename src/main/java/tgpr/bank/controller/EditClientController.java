package tgpr.bank.controller;

import com.googlecode.lanterna.gui2.Window;
import tgpr.bank.model.*;
import tgpr.bank.view.EditClientView;
import tgpr.framework.Controller;
import tgpr.framework.ErrorList;
import tgpr.framework.Tools;

import java.util.List;

public class EditClientController extends Controller {
    private final EditClientView view;
    private User client;

    private Agency agency;

    private final boolean isNewClient;

    /*public EditClientController() {
        this(null);
    }*/

    public EditClientController(Agency agency) {
        this.agency = agency;
        isNewClient = true;
        view = new EditClientView(this, null, agency);
    }
    public EditClientController(User client) {
        this.client = client;
        isNewClient = client == null;
        agency = getAgencyByClient();
        view = new EditClientView(this,client,agency);
    }


    public void save(String email, String password, String last_name, String first_name, String birthdate, String agencyName) {
        var errors = validate(email,password,last_name,first_name,birthdate,agencyName);
        if(errors.isEmpty()){
            Agency agency= Agency.getAgencyIdByName(agencyName);
            int agencyId = agency.getId();
            var hashedPassword = password.isBlank() ? password : Tools.hash(password);
            client = User.createClient(email,hashedPassword,last_name,first_name,Tools.toDate(birthdate),agencyId);
            client.save();
            view.close();
        }else{
            showErrors(errors);
        }
    }

    public ErrorList validate(String email,String password,String last_name,String first_name,String birthdate, String agencyName){
        var errors = new ErrorList();
        if(isNewClient){
            errors.add(UserValidator.isValidAvailableEmail(email));
            errors.add(UserValidator.isValidPassword(password));
        }
        if(!birthdate.isBlank() && !Tools.isValidDate(birthdate)){
            errors.add("invalid birthdate",User.Fields.birth_date);
        }
        Agency agency= Agency.getAgencyIdByName(agencyName);
        int agencyId = agency.getId();
        var hashedPassword = password.isBlank() ? password : Tools.hash(password);
        var client = User.createClient(email,hashedPassword,last_name,first_name,Tools.toDate(birthdate),agencyId);
        errors.addAll(UserValidator.validate(client));
        return errors;
    }

    public void delete(){
        client.delete();
        view.close();
    }

    @Override
    public Window getView() {
        return view;
    }

    public User getClient() {
        return client;
    }
    public List<Account> showAccountAccess (User user){
        var controller=new AccessAccountClientController(user);
        navigateTo(controller);
        return controller.getAccount();
    }
    public Agency getAgencyByClient() {
        agency = Agency.getAgencyName(client.getAgency());
        return agency;
    }

    public Agency getAgency() {
        return agency;
    }



}

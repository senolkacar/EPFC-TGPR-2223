package tgpr.bank.controller;

import com.googlecode.lanterna.gui2.Window;
import tgpr.bank.model.Agency;
import tgpr.bank.model.User;
import tgpr.bank.view.AgencyDetailsView;
import tgpr.framework.Controller;

import java.util.List;

public class AgencyDetailsController extends Controller {
    private final AgencyDetailsView view;
    private Agency agency;

    public AgencyDetailsController(Agency agency) {
        this.agency = agency;
        view = new AgencyDetailsView(this,agency);
    }

    public User addClient(Agency agency) {
        var controller = new EditClientController(agency);
        navigateTo(controller);
        return controller.getClient();
    }

    @Override
    public Window getView() {
        return view;
    }

    public List<Agency> getAgencies() {
        return Agency.getAllAgency();
    }

    public Agency getAgency() {
        return agency;
    }

    public void editClient(User client){
        navigateTo(new EditClientController(client));
    }
    public List<User> getClients(String filter) {
        return Agency.getAllClientsFiltered(getAgency(), filter);
    }

}

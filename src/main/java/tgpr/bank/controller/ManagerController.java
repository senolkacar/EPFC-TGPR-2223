package tgpr.bank.controller;

import com.googlecode.lanterna.gui2.Window;
import tgpr.bank.model.Account;
import tgpr.bank.model.Agency;
import tgpr.bank.model.Security;
import tgpr.bank.view.ManagerView;
import tgpr.bank.view.ViewAccountList;
import tgpr.framework.Controller;

import java.util.List;


public class ManagerController extends Controller {
    @Override
    public Window getView() {
        return new ManagerView(this);
    }

    public List<Agency> getAgency() {
        return Agency.getAllAgency();
    }

    public void logout() {
        Security.logout();
        navigateTo(new LoginController());
    }

    public void exit() {
        System.exit(0);
    }

    public void showAgencyDetails(Agency agency) {
        navigateTo(new AgencyDetailsController(agency));
    }

}

package tgpr.bank.controller;

import com.googlecode.lanterna.gui2.Window;
import tgpr.bank.model.User;
import tgpr.bank.view.UserListView;
import tgpr.framework.Controller;
import tgpr.bank.model.Security;

import java.util.List;

public class UserListController extends Controller {

    private List<User> users;

    @Override
    public Window getView() {
        return new UserListView(this);
    }

    public List<User> getUsers() {
        return User.getAll();
    }

    public void logout() {
        Security.logout();
        navigateTo(new LoginController());
    }

    public void exit() {
        System.exit(0);
    }


}


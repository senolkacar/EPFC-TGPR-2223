package tgpr.bank;

import tgpr.bank.controller.TestController;
import tgpr.bank.model.User;
import tgpr.framework.Controller;
import tgpr.framework.Model;

public class BankApp {
    public final static String DATABASE_SCRIPT_FILE = "/database/tgpr-2223-xyy.sql";

    public static void main(String[] args) {
        if (!Model.checkDb(DATABASE_SCRIPT_FILE))
            Controller.abort("Database is not available!");
        else
            Controller.navigateTo(new TestController());

        var test = User.getByMail("admin@test.com");
        System.out.println(test);

    }
}

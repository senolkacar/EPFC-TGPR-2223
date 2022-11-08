package tgpr.bank.controller;

import com.googlecode.lanterna.gui2.Window;
import tgpr.bank.model.Account;
import tgpr.bank.model.Transfer;
import tgpr.bank.model.TransferValidator;
import tgpr.bank.view.NewTransferView;
import tgpr.framework.Controller;
import tgpr.framework.ErrorList;

import java.util.List;

public class NewTransferController extends Controller {

    @Override
    public Window getView() {
        return new NewTransferView(this);
    }

    public List<Account> getAccounts() {
        return Account.getAll();
    }

    public ErrorList validate(String iban, String title, String amount, String description,Double sourceSaldo,Double sourceFloor) {
        var errors = new ErrorList();
        errors.add(TransferValidator.isValidTargetAccount(iban));
        errors.add(TransferValidator.isValidTitle(title));
        errors.add(TransferValidator.isValidAmount(amount,sourceSaldo,sourceFloor));
        errors.add(TransferValidator.isValidDescription(description));
        return errors;
    }
}

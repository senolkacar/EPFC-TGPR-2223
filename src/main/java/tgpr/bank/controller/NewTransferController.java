package tgpr.bank.controller;

import com.googlecode.lanterna.gui2.Window;
import tgpr.bank.model.Account;
import tgpr.bank.model.Transfer;
import tgpr.bank.model.TransferValidator;
import tgpr.bank.view.NewTransferView;
import tgpr.framework.Controller;
import tgpr.framework.ErrorList;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class NewTransferController extends Controller {

    private final NewTransferView view;

    public NewTransferController(){
        view = new NewTransferView(this);
    }
    @Override
    public Window getView() {
        return new NewTransferView(this);
    }

    public List<Account> getAccounts() {
        return Account.getAll();
    }

    public ErrorList validate(String iban, String title, String amount, String description,Double sourceSaldo,Double sourceFloor,String date) {
        var errors = new ErrorList();
        errors.add(TransferValidator.isValidTargetAccount(iban));
        errors.add(TransferValidator.isValidTitle(title));
        errors.add(TransferValidator.isValidAmount(amount,sourceSaldo,sourceFloor));
        errors.add(TransferValidator.isValidDescription(description));
        errors.add(TransferValidator.isValidDateFormat(date));
        return errors;
    }

    public void save(String iban, String title, String amount, String description, Double sourceSaldo, Double sourceFloor, String effectiveAt, Integer sourceAccountId, Integer targetAccountID, Double targetSaldo, LocalDateTime createdAT,Integer createdBy){
        String state = "executed";
        LocalDate date = null;
        var errors = validate(iban,title,amount,description, sourceSaldo, sourceFloor,effectiveAt);
        if(errors.isEmpty()){
            if(!Objects.equals(effectiveAt, "")){
                state = "future";
                date = LocalDate.parse(effectiveAt, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            }
            Transfer.addTransferToDB(Double.parseDouble(amount),description,sourceAccountId,targetAccountID,sourceSaldo,targetSaldo,createdAT,createdBy,date,state);
        }else {
            System.out.println("Save failed because of these errors: " + errors);
        }
    }

    public void close(){
        view.close();
    }
}

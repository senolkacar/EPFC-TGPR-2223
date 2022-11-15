package tgpr.bank.controller;

import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import tgpr.bank.model.*;
import tgpr.bank.view.NewTransferView;
import tgpr.framework.Controller;
import tgpr.framework.ErrorList;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class NewTransferController extends Controller {

    private final NewTransferView view;

    public static Controller previousController;

    public NewTransferController(){
        view = new NewTransferView(this);
    }
    @Override
    public Window getView() {
        return view;
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

    public void save(String iban, String title, String amount, String description, Double sourceSaldo, Double sourceFloor, String effectiveAt, Integer sourceAccountId, Integer targetAccountID, Double targetSaldo, LocalDateTime createdAT,Integer createdBy, boolean addToFav, String category){
        String state = "executed";
        LocalDate date = null;
        var errors = validate(iban,title,amount,description, sourceSaldo, sourceFloor,effectiveAt);
        if(errors.isEmpty()){
            if(targetAccountID==null){
                if(Account.accountAlreadyExistsInDB(iban)){
                    targetAccountID = Account.getByIban(iban).getId();
                    if(!Account.isExternalAccount(iban)){
                        targetSaldo = Account.getByIban(iban).getSaldo();
                        targetSaldo = targetSaldo+Double.parseDouble(amount);
                    }
                }else{
                    Account.newExternalAccount(iban,title);
                    targetAccountID = Account.getLastCreatedAccount().getId();
                }
            }

            if(!Objects.equals(effectiveAt, "")){
                state = "future";
                date = LocalDate.parse(effectiveAt, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                sourceSaldo=null;
                targetSaldo=null;
            }else{
                sourceSaldo = sourceSaldo-Double.parseDouble(amount);
                if(targetSaldo!=null){
                    targetSaldo = targetSaldo+Double.parseDouble(amount);
                }
            }
            Transfer.addTransferToDB(Double.parseDouble(amount),description,sourceAccountId,targetAccountID,sourceSaldo,targetSaldo,createdAT,createdBy,date,state);
            Account.updateAccountSaldo(sourceAccountId,sourceSaldo);
            Account.updateAccountSaldo(targetAccountID,targetSaldo);
            if(addToFav){
                if(!Favourite.alreadyInFav(Security.getLoggedUser().getId(),targetAccountID)){
                    Favourite.addToFav(Security.getLoggedUser().getId(),targetAccountID);
                }
            }
            if(category!=null){
                Category.addTransferToTransferCat(Category.getCatByName(category,sourceAccountId).getId(),Transfer.getLastCreatedTransfer().getId(),sourceAccountId);
            }
            showMessage("Your transfer has been successfuly created", "Information", MessageDialogButton.valueOf("OK"));
            view.close();
            navigateTo(previousController);
        }else {
            showErrors(errors);
        }
    }
}

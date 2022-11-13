package tgpr.bank.model;

import tgpr.framework.Error;
import tgpr.framework.ErrorList;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public abstract class TransferValidator {
    public static Error isValidTargetAccount(String targetAccount){
        if(targetAccount==null || targetAccount.isBlank()){
            return new Error("target iban required",Transfer.Fields.TargetAccountIban);
        }
        if(!Pattern.matches("[a-zA-Z]{2}[0-9]{2}\s[0-9]{4}\s[0-9]{4}\s[0-9]{4}",targetAccount)){
            return new Error("bad target iban format",Transfer.Fields.TargetAccountIban);
        }
        return Error.NOERROR;
    }

    public static Error isValidTitle(String title){
        if(title==null || title.isBlank()){
            return new Error("target account title required",Transfer.Fields.TargetAccountTitle);
        }
        return Error.NOERROR;
    }

    public static Error isValidAmount(String amount,Double sourceSaldo,Double sourceFloor){
        if(sourceFloor<0){
            sourceFloor = Math.abs(sourceFloor);
        }
        double amountmax = sourceFloor + sourceSaldo;
        if(amount==null || amount.isBlank()){
            return new Error("amount required",Transfer.Fields.Amount);
        }
        try {
            if (Double.parseDouble(amount) > amountmax || Double.parseDouble(amount) < 0) {
                return new Error("amount must be <= " + amountmax + " €", Transfer.Fields.Amount);
            }
        } catch (NumberFormatException n) {
            return new Error("invalid format", Transfer.Fields.Amount);
        }
        return Error.NOERROR;
    }

    public static Error isValidDescription(String description){
        if(description==null || description.isBlank()){
            return new Error("description required",Transfer.Fields.Description);
        }
        return Error.NOERROR;
    }

    public static Error isValidDateFormat(String date){
        if(!date.isEmpty() || !date.isBlank()){
            if(!Pattern.matches("^(0?[1-9]|[12][0-9]|3[01])[\\/\\-](0?[1-9]|1[012])[\\/\\-]\\d{4}$",date)){
                return new Error("date format invalid, must be dd-mm-yyyy",Transfer.Fields.EffectiveAt);
            }
        }
        return Error.NOERROR;
    }

    public static boolean targetAccountIsSelected(String selectedTargetAccount){
        return(!Objects.equals(selectedTargetAccount, "-- insert IBAN myself --"));
    }



}

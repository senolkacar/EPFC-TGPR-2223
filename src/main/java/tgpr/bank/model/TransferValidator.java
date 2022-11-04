package tgpr.bank.model;

import tgpr.framework.Error;
import tgpr.framework.ErrorList;

import java.util.List;

public abstract class TransferValidator {
    public static Error isValidTargetAccount(String targetAccount){
        if(targetAccount==null || targetAccount.isBlank())
            return new Error("target iban required",Transfer.Fields.TargetAccountIban);
        else
            return Error.NOERROR;
    }

    public static List<Error> validate (Transfer transfer){
        var errors = new ErrorList();

        errors.add(isValidTargetAccount(transfer.getTargetAccount().getIban()));

        return errors;
    }
}

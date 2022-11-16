package tgpr.bank.model;

import tgpr.framework.Error;

import java.util.regex.Pattern;

public abstract class AccessValidator {
    public static Error typeisnotempty(String type) {
        if (type == null )
            return new Error("type required",Access.Fields.type);

        return Error.NOERROR;
    }
}

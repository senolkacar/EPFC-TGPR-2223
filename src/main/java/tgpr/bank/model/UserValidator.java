package tgpr.bank.model;

import com.googlecode.lanterna.gui2.CheckBox;
import com.googlecode.lanterna.gui2.TextBox;
import org.w3c.dom.Text;
import tgpr.framework.Error;
import tgpr.framework.ErrorList;
import tgpr.framework.Tools;

import javax.tools.Tool;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.regex.Pattern;

public abstract class UserValidator {

    public static Error isValidEmail(String email) {
        if (email == null || email.isBlank())
            return new Error("email required", User.Fields.email);
        if (!Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", email))
            return new Error("invalid email", User.Fields.email);

        return Error.NOERROR;
    }

    public static Error isValidPassword(String password) {
        if (password == null || password.isBlank())
            return new Error("password required", User.Fields.Password);
        if (!Pattern.matches("[a-zA-Z0-9]{3,}", password))
            return new Error("invalid password", User.Fields.Password);
        return Error.NOERROR;
    }
    public static Error isValidDate(String date){
        if(Tools.isValidDateTime(date)){
            return Error.NOERROR;
        }
        else{
            return new Error("invalid date");
        }
    }
    public static Error isValidAvailableEmail(String email) {
        var error = isValidEmail(email);
        if(error != Error.NOERROR)
            return error;
        if(User.getByEmail(email) != null)
            return new Error("email already used", User.Fields.email);
        return Error.NOERROR;
    }

    public static List<Error> validate(User client){
        var errors = new ErrorList();
        errors.add(isValidEmail(client.getEmail()));
        errors.add(isValidPassword(client.getPassword()));
        return errors;
    }
}

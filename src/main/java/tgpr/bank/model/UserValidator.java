package tgpr.bank.model;

import tgpr.framework.Error;
import tgpr.framework.ErrorList;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.regex.Pattern;

public abstract class UserValidator {

    public static Error isValidBirthdate(LocalDate birthdate) {
        if (birthdate == null)
            return Error.NOERROR;
        if (birthdate.compareTo(LocalDate.now()) > 0)
            return new Error("may not be born in the future", User.Fields.BirthDate);
        var age = Period.between(birthdate, LocalDate.now()).getYears();
        if (age < 18)
            return new Error("must be 18 years old", User.Fields.BirthDate);
        else if (age > 120)
            return new Error("may not be older then 120 years", User.Fields.BirthDate);
        return Error.NOERROR;
    }

    public static Error isValidEmail(String email) {
        if (email == null || email.isBlank())
            return new Error("email required", User.Fields.email);
        if (!Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", email))
            return new Error("invalid email", User.Fields.email);
        return Error.NOERROR;
    }

    public static Error isValidAvailablePseudo(String email) {
        var error = isValidEmail(email);
        if (error != Error.NOERROR)
            return error;
        if (User.getByEmail(email) != null)
            return new Error("not available", User.Fields.email);
        return Error.NOERROR;
    }

    public static Error isValidPassword(String password) {
        if (password == null || password.isBlank())
            return new Error("password required", User.Fields.Password);
        if (!Pattern.matches("[a-zA-Z0-9]{3,}", password))
            return new Error("invalid password", User.Fields.Password);
        return Error.NOERROR;
    }

    public static List<Error> validate(User user) {
        var errors = new ErrorList();

        // field validations
        errors.add(isValidEmail(user.getEmail()));
        errors.add(isValidBirthdate(user.getBirthdate()));

        return errors;
    }

}

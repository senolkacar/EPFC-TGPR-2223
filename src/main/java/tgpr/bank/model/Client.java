package tgpr.bank.model;

import java.time.LocalDate;

public class Client extends User {


    public Client(Integer id, String type, String email, String password, LocalDate birthdate, String lastname, String firstname, Integer agency) {
        super(id, type, email, password, birthdate, lastname, firstname, agency);
    }
}

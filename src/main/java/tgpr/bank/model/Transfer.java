package tgpr.bank.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Transfer {

    private int id;
    private double amount;
    private String description;
    private Account sourceAccount;
    private Account targetAccount;
    private double sourceSaldo;
    private double targetSaldo;
    private LocalDateTime createdAt;
    private int createdBy;
    private LocalDate effectiveAt;
    private String state;
}

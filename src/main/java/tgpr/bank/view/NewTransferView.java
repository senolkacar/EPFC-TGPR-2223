package tgpr.bank.view;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.bank.controller.NewTransferController;
import tgpr.bank.model.Account;
import tgpr.bank.model.Category;
import tgpr.bank.model.Favourite;
import tgpr.bank.model.Transfer;
import tgpr.framework.Layouts;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NewTransferView extends DialogWindow {
    private NewTransferController controller;
    private ComboBox<Account> cBoxSourceAccount;
    private ComboBox<String> cBoxTargetAccount;
    private final TextBox txtBoxIban;
    private final TextBox txtBoxTitle;
    private final TextBox txtBoxAmount;
    private final TextBox txtBoxDescription;
    private final TextBox txtBoxDate;
    private final Label errIBAN;
    private final Label errTitle;
    private final Label errAmount;
    private final Label errDescription;
    private List<Account> listTargetAccounts;

    public NewTransferView(NewTransferController controller){
        super("Create Transfer");
        this.controller = controller;
        setHints(List.of(Hint.CENTERED));
        setCloseWindowWithEscape(true);

        Panel root = new Panel();
        setComponent(root);

        Panel panel = new Panel().setLayoutManager(new GridLayout(2).setTopMarginSize(1))
                .setLayoutData(Layouts.LINEAR_CENTER).addTo(root);

//        refresh();

        panel.addComponent(new Label("Source Account: "));
        cBoxSourceAccount = new ComboBox<Account>(Account.getAll()).addTo(panel).takeFocus();
        cBoxSourceAccount.addListener((selectedIndex,previousSelection, changedByUserInteraction) -> reloadData());
        new EmptySpace().addTo(panel);
        new EmptySpace().addTo(panel);
        panel.addComponent(new Label("Target Account: "));
        listTargetAccounts = Account.getTargetAccounts(cBoxSourceAccount.getSelectedItem().getId());
        var listTargetAccountsToString = Account.getTargetAccountsToString(cBoxSourceAccount.getSelectedItem().getId());
        listTargetAccountsToString.add(0,"-- insert IBAN myself --");
        cBoxTargetAccount = new ComboBox<String>(listTargetAccountsToString).addTo(panel).addListener((SelectedIndex,previousSelection, changedByUserInteraction) -> reloadInfo());
        new EmptySpace().addTo(panel);
        Panel targetAccountFields = new Panel().setLayoutManager(new GridLayout(2).setRightMarginSize(2).setTopMarginSize(1).setBottomMarginSize(1)).addTo(panel).addTo(panel);
        targetAccountFields.addComponent(new Label("IBAN: "));
        txtBoxIban = new TextBox(new TerminalSize(20,1) ).addTo(targetAccountFields)
                .setTextChangeListener((txt,byUser) -> validate());
        new EmptySpace().addTo(targetAccountFields);
        errIBAN = new Label("").addTo(targetAccountFields).setForegroundColor(TextColor.ANSI.RED);
        targetAccountFields.addComponent(new Label("Title: "));
        txtBoxTitle = new TextBox(new TerminalSize(40,1)).addTo(targetAccountFields)
                .setTextChangeListener((txt,byUser) -> validate());
        new EmptySpace().addTo(targetAccountFields);
        errTitle = new Label("").addTo(targetAccountFields).setForegroundColor(TextColor.ANSI.RED);
        targetAccountFields.addComponent(new Label("Add to favourites "));
        CheckBox checkBoxAddtoFav = new CheckBox().addTo(targetAccountFields);
        panel.addComponent(new Label("Amount: "));
        txtBoxAmount = new TextBox(new TerminalSize(10,1)).addTo(panel)
                .setTextChangeListener((txt,byUser) -> validate());
        new EmptySpace().addTo(panel);
        errAmount = new Label("").addTo(panel).setForegroundColor(TextColor.ANSI.RED);
        panel.addComponent(new Label("Description: "));
        txtBoxDescription = new TextBox(new TerminalSize(30,2)).addTo(panel)
                .setTextChangeListener((txt,byUser) -> validate());
        new EmptySpace().addTo(panel);
        errDescription = new Label("").addTo(panel).setForegroundColor(TextColor.ANSI.RED);
        panel.addComponent(new Label("Effect Date: "));
        txtBoxDate = new TextBox(new TerminalSize(9,1)).addTo(panel);
        new EmptySpace().addTo(panel);
        new EmptySpace().addTo(panel);
        panel.addComponent(new Label("Category: "));
        ComboBox<Category> cbBoxCategory = new ComboBox<Category>().addTo(panel);

        Panel buttons = new Panel(new GridLayout(2)).setLayoutData(Layouts.LINEAR_CENTER).addTo(root);
        Button buttonCreate = new Button("Create").addTo(buttons);
        Button buttonClose = new Button("Close").addTo(buttons);
    }

    public void reloadData(){
        listTargetAccounts = Account.getTargetAccounts(cBoxSourceAccount.getSelectedItem().getId());
        var listTargetAccountsToString = Account.getTargetAccountsToString(cBoxSourceAccount.getSelectedItem().getId());
        int size = cBoxTargetAccount.getItemCount();
        for(int i=size ; i>0;--i){
            cBoxTargetAccount.removeItem(i-1);
        }
        cBoxTargetAccount.addItem("-- insert IBAN myself --");
        for(String account : listTargetAccountsToString){
            cBoxTargetAccount.addItem(account);
        }
    }

    private void reloadInfo() {
        if(cBoxTargetAccount.getSelectedItem().equals("-- insert IBAN myself --")){
            txtBoxIban.setText("").setReadOnly(false);
            txtBoxTitle.setText("").setReadOnly(false);
        }else {
            Account account = listTargetAccounts.get(cBoxTargetAccount.getSelectedIndex()-1);
            txtBoxIban.setText(account.getIban()).setReadOnly(true);
            txtBoxTitle.setText(account.getTitle()).setReadOnly(true);
        }
    }

    public void validate(){
        var errors = controller.validate(
                txtBoxIban.getText(),
                txtBoxTitle.getText(),
                txtBoxAmount.getText(),
                txtBoxDescription.getText(),
                cBoxSourceAccount.getSelectedItem().getSaldo(),
                cBoxSourceAccount.getSelectedItem().getFloor()
        );

        errIBAN.setText(errors.getFirstErrorMessage(Transfer.Fields.TargetAccountIban));
        errTitle.setText(errors.getFirstErrorMessage(Transfer.Fields.TargetAccountTitle));
        errAmount.setText(errors.getFirstErrorMessage(Transfer.Fields.Amount));
        errDescription.setText(errors.getFirstErrorMessage(Transfer.Fields.Description));
    }

    public void save(String iban, String title, String amount, String description){

    }

}

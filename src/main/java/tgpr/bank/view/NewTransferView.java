package tgpr.bank.view;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.bank.controller.NewTransferController;
import tgpr.bank.model.*;
import tgpr.framework.Layouts;

import java.time.LocalDateTime;
import java.util.List;

public class NewTransferView extends DialogWindow {
    private NewTransferController controller;
    private ComboBox<Account> cBoxSourceAccount;
    private ComboBox<String> cBoxTargetAccount;
    private ComboBox<String> cbBoxCategory;
    private final TextBox txtBoxIban;
    private final TextBox txtBoxTitle;
    private final TextBox txtBoxAmount;
    private final TextBox txtBoxDescription;
    private final TextBox txtBoxDate;
    private final Label errIBAN;
    private final Label errTitle;
    private final Label errAmount;
    private final Label errDescription;
    private final Label errDate;
    private final CheckBox checkBoxAddtoFav;
    private List<Account> listTargetAccounts;
    private List<String> listTargetAccountsToString;
    private List<Category> listCategory;
    private List<String> listCategoryToString;


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
        listTargetAccountsToString = Account.getTargetAccountsToString(cBoxSourceAccount.getSelectedItem().getId());
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
        checkBoxAddtoFav = new CheckBox().addTo(targetAccountFields);
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
        txtBoxDate = new TextBox(new TerminalSize(15,1)).addTo(panel)
                .setTextChangeListener((txt,byUser)->validate());
        new EmptySpace().addTo(panel);
        errDate = new Label("").addTo(panel).setForegroundColor(TextColor.ANSI.RED);
        panel.addComponent(new Label("Category: "));
        listCategory = Category.getByAccount(cBoxSourceAccount.getSelectedItem().getId());
        listCategoryToString = Category.getCategoryNames(cBoxSourceAccount.getSelectedItem().getId());
        listCategoryToString.add(0,"NO CATEGORY");
        cbBoxCategory = new ComboBox<String>(listCategoryToString).addTo(panel);

        Panel buttons = new Panel(new GridLayout(2)).setLayoutData(Layouts.LINEAR_CENTER).addTo(root);
        Button buttonCreate = new Button("Create",() -> save(txtBoxIban.getText(),txtBoxTitle.getText(),txtBoxAmount.getText(),txtBoxDescription.getText())).addTo(buttons);
        Button buttonClose = new Button("Close",this::close).addTo(buttons);
    }

    public void reloadData(){
        listTargetAccounts = Account.getTargetAccounts(cBoxSourceAccount.getSelectedItem().getId());
        listTargetAccountsToString = Account.getTargetAccountsToString(cBoxSourceAccount.getSelectedItem().getId());
        int size = cBoxTargetAccount.getItemCount();
        for(int i=size ; i>0;--i){
            cBoxTargetAccount.removeItem(i-1);
        }
        cBoxTargetAccount.addItem("-- insert IBAN myself --");
        for(String account : listTargetAccountsToString){
            cBoxTargetAccount.addItem(account);
        }
        listCategory = Category.getByAccount(cBoxSourceAccount.getSelectedItem().getId());
        listCategoryToString = Category.getCategoryNames(cBoxSourceAccount.getSelectedItem().getId());
        int sizeCat = cbBoxCategory.getItemCount();
        for(int i=sizeCat;i>0;--i){
            cbBoxCategory.removeItem(i-1);
        }
        listCategoryToString.add(0,"NO CATEGORY");
        for(String category : listCategoryToString){
            cbBoxCategory.addItem(category);
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
                cBoxSourceAccount.getSelectedItem().getFloor(),
                txtBoxDate.getText()
        );

        errIBAN.setText(errors.getFirstErrorMessage(Transfer.Fields.TargetAccountIban));
        errTitle.setText(errors.getFirstErrorMessage(Transfer.Fields.TargetAccountTitle));
        errAmount.setText(errors.getFirstErrorMessage(Transfer.Fields.Amount));
        errDescription.setText(errors.getFirstErrorMessage(Transfer.Fields.Description));
        errDate.setText(errors.getFirstErrorMessage(Transfer.Fields.EffectiveAt));
    }

    public void save(String iban, String title, String amount, String description){
        Integer targetAccountId=null;
        Double targetSaldo=null;
        String category=null;
        if(!TransferValidator.targetAccountIsNotSelected(cBoxTargetAccount.getSelectedItem())){
            targetAccountId = listTargetAccounts.get(cBoxTargetAccount.getSelectedIndex()-1).getId();
            targetSaldo = listTargetAccounts.get(cBoxTargetAccount.getSelectedIndex()-1).getSaldo();
        }
        if(!TransferValidator.categoryIsNotSelected(cbBoxCategory.getSelectedItem())){
            category = listCategory.get(cbBoxCategory.getSelectedIndex()-1).getName();
        }
        controller.save(iban,title,amount,description,cBoxSourceAccount.getSelectedItem().getSaldo(),cBoxSourceAccount.getSelectedItem().getFloor(),txtBoxDate.getText(),cBoxSourceAccount.getSelectedItem().getId(),targetAccountId,targetSaldo, LocalDateTime.now(),cBoxSourceAccount.getSelectedItem().getId(),checkBoxAddtoFav.isChecked(),category);
    }

}

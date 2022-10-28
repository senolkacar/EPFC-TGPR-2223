package tgpr.bank.view;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.bank.controller.NewTransferController;
import tgpr.bank.model.Account;
import tgpr.bank.model.Category;
import tgpr.bank.model.Favourite;
import tgpr.framework.Layouts;

import java.util.List;

public class NewTransferView extends DialogWindow {
    private NewTransferController controller;
    private ComboBox<Account> cBoxSourceAccount;
    private TextBox txtBoxIban;

    public NewTransferView(NewTransferController controller){
        super("Create Transfer");
        this.controller = controller;
        setHints(List.of(Hint.CENTERED));
        setCloseWindowWithEscape(true);

        Panel root = new Panel();
        setComponent(root);

        Panel panel = new Panel().setLayoutManager(new GridLayout(2).setTopMarginSize(1).setVerticalSpacing(1))
                .setLayoutData(Layouts.LINEAR_CENTER).addTo(root);

//        refresh();

        panel.addComponent(new Label("Source Account: "));
        cBoxSourceAccount = new ComboBox<Account>(Account.getAll()).addTo(panel).takeFocus();
        cBoxSourceAccount.addListener((selectedIndex, previousSelection, changedByUserInteraction) -> validate());
        panel.addComponent(new Label("Target Account: "));
        ComboBox<Account> cBoxTargetAccount = new ComboBox<Account>().addTo(panel);
        new EmptySpace().addTo(panel);
        Panel targetAccountFields = new Panel().setLayoutManager(new GridLayout(2).setRightMarginSize(2).setVerticalSpacing(1)).addTo(panel);
        targetAccountFields.addComponent(new Label("Iban: "));
        txtBoxIban = new TextBox(new TerminalSize(20,1) ).addTo(targetAccountFields);
        txtBoxIban.setText(cBoxSourceAccount.getSelectedItem().getIban());
//        cBoxSourceAccount.addListener((selectedIndex, previousSelection, changedByUserInteraction) -> validate());
        new EmptySpace().addTo(panel);
        targetAccountFields.addComponent(new Label("Title: "));
        TextBox txtBoxTitle = new TextBox(new TerminalSize(40,1)).addTo(targetAccountFields);
        new EmptySpace().addTo(panel);
        targetAccountFields.addComponent(new Label("Add to favourites "));
        CheckBox checkBoxAddtoFav = new CheckBox().addTo(targetAccountFields);
        panel.addComponent(new Label("Amount: "));
        TextBox txtBoxAmount = new TextBox(new TerminalSize(10,1)).addTo(panel);
        panel.addComponent(new Label("Description: "));
        TextBox txtBoxDescription = new TextBox(new TerminalSize(30,2)).addTo(panel);
        panel.addComponent(new Label("Effect Date: "));
        TextBox txtBoxDate = new TextBox(new TerminalSize(9,1)).addTo(panel);
        panel.addComponent(new Label("Category: "));
        ComboBox<Category> cbBoxCategory = new ComboBox<Category>().addTo(panel);

        Panel buttons = new Panel(new GridLayout(2)).setLayoutData(Layouts.LINEAR_CENTER).addTo(root);
        Button buttonCreate = new Button("Create").addTo(buttons);
        Button buttonClose = new Button("Close").addTo(buttons);

    }

//    public void refresh(){
//        if(cBoxSourceAccount.setSelectedItem(Account.))
//    }


    public void validate(){

    }

}

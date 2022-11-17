package tgpr.bank.view;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.bank.controller.DisplayAccountAccessController;
import tgpr.bank.model.Account;
import tgpr.bank.model.User;
import tgpr.framework.ColumnSpec;
import tgpr.framework.ObjectTable;

import java.util.List;

import static tgpr.framework.Controller.askConfirmation;

public class DisplayAccountAccessView extends DialogWindow {
    private final DisplayAccountAccessController controller;

    private final Account account;
    private User user;
    private ComboBox<String> cboType;
    private ObjectTable<Account> oneAccountTable;
    private Label lblIban;


    public DisplayAccountAccessView(DisplayAccountAccessController controller, Account account) {
        super("Update Access");
        this.controller = controller;
        this.account = account;

        setHints(List.of(Window.Hint.CENTERED));
        setCloseWindowWithEscape(true);

        Panel root = new Panel();
        setComponent(root);


        Panel fields = new Panel().setLayoutManager(new GridLayout(2).setTopMarginSize(1)).addTo(root);
        fields.addComponent(new Label("IBAN:"));
        lblIban = new Label("").addTo(fields).addStyle(SGR.BOLD);


        new EmptySpace().addTo(root);
        cboType = new ComboBox<String>("holder", "proxy").addTo(root).setPreferredSize(new TerminalSize(10, 1))
                .addListener((newIndex, oldIndex, byUser) -> reloadData());

        createButtonsPanel().addTo(root);
        refresh();

    }


    private void reloadData() {

    }

    private void refresh() {
        if (account != null) {
            lblIban.setText(account.getIban());


        }
    }

    private Panel createButtonsPanel() {
        var root = new Panel()
                .setLayoutManager(new LinearLayout(Direction.HORIZONTAL))
                .setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

        new Button("Update", this::update).addTo(root);

        new Button("Delete", this::delete).addTo(root);
        Button btnClose = new Button("Close", this::close).addTo(root);
        return root;
    }



     private void delete() {
        askConfirmation("Do you want to remove this account from your Acces? " + account.getIban(), "Remove favourite");
         if(account.numberOfHolder(account.getId())==1 && cboType.getItem(0)=="holder"){
             controller.showErrorDelete();
         }else {
             controller.delete(account.getId(),cboType.getSelectedItem());
             account.reload();
         }
    }




    private void update() {
        int cmp=account.numberOfHolder(account.getId());

        if(cmp==1 && cboType.getSelectedItem()=="holder"){
            controller.showErrorUpdate();
        }else{
          controller.update(account.getId(), cboType.getSelectedItem());
          controller.close();


    }

}}





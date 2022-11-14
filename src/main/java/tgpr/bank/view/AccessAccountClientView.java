package tgpr.bank.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.bank.controller.AccessAccountClientController;
import tgpr.bank.model.Access;
import tgpr.bank.model.Account;
import tgpr.bank.model.User;
import tgpr.framework.ColumnSpec;
import tgpr.framework.ObjectTable;
import tgpr.framework.ViewManager;

import java.util.List;

public class AccessAccountClientView extends DialogWindow {
    private final AccessAccountClientController controller;
    private final User user;


    private ObjectTable<Account> accountTable;
    private ComboBox<String> cboAccess;
    private ComboBox<String> cboType;
    private List<Account> accountNoAccess;


    public AccessAccountClientView(AccessAccountClientController controller, User user) {
        super(user.getEmail());
        this.controller = controller;
        this.user = user;

        setTitle("AccessAccount");
        setHints(List.of(Hint.CENTERED));

        Panel root = new Panel();
        setComponent(root);

        new EmptySpace().addTo(root);

        accountTable = new ObjectTable<>(

                new ColumnSpec<>("id", Account::getId),
                new ColumnSpec<>("IBAN", Account::getIban),
                new ColumnSpec<>("title", Account::getTitle),
                new ColumnSpec<>("type access", a -> controller.isHolder(user.getId(), a.getId()))
//                new ColumnSpec<>("Access type", Access::getType)
        );


        root.addComponent(accountTable);
        accountTable.setPreferredSize(new TerminalSize(70, 15));
        var buttons = new Panel().addTo(root).setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
        root.addComponent(buttons, LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));
        cboAccess = new ComboBox<String>().addTo(buttons).setPreferredSize(new TerminalSize(28, 1))
                .addListener((newIndex, oldIndex, byUser) -> reloadInfo());
        cboType = new ComboBox<String>("holder", "proxy").addTo(buttons).setPreferredSize(new TerminalSize(10, 1))
                .addListener((newIndex, oldIndex, byUser) -> reloadData());
//        Button add = new Button("Add", this::addFavourite).addTo(buttons);
        Button add = new Button("Add", this::addAccees).addTo(buttons);
        Button btnReset = new Button("Reset", this::reset).addTo(buttons);
        Button btnClose = new Button("Close", this::close).addTo(buttons);
        accountTable.setSelectAction(() ->{
            var accountAccess = accountTable.getSelected();
            accountTable.setSelected(accountAccess);
            controller.DeleteAccess(accountAccess);

        });
        reloadData();

    }

    private void addAccees() {
    }


    public void reloadData() {

        accountTable.clear();
        var account = controller.getAccount();
        accountTable.add(account);
        var listaccount = controller.getAccountNoAccess();
        for (Account acc : listaccount) {
            cboAccess.addItem(acc.getIban());
        }
        reloadInfo();

    }

    public void reset() {
        cboAccess.setSelectedIndex(0);

    }

    public void reloadInfo() {
        accountNoAccess = controller.getAccountNoAccess();
        int size = cboAccess.getItemCount();
        for (int i = size; i > 0; i--) {
            cboAccess.removeItem(i - 1);
        }
        cboAccess.addItem(0, "");
        var listaccount = controller.getAccountNoAccess();
        for (Account acc : listaccount) {
            cboAccess.addItem(acc.getIban());
        }
    }
}




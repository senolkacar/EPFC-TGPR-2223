package tgpr.bank.view;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.bank.controller.AccessAccountClientController;
import tgpr.bank.controller.DisplayAccessAccountController;
import tgpr.bank.model.Account;

import java.util.List;



public class DisplayAccessAccountView  extends DialogWindow {
    private final DisplayAccessAccountController controller;
    private Account account;

    public DisplayAccessAccountView(DisplayAccessAccountController controller, Account account) {
        super("Access Account");
       this.controller = controller;
       this.account=account;


        setHints(List.of(Hint.CENTERED));
        setCloseWindowWithEscape(true);

        Panel root = new Panel();
        setComponent(root);


        new EmptySpace().addTo(root);
        createButtonsPanel().addTo(root);


    }




    private Panel createButtonsPanel() {
        var root = new Panel()
                .setLayoutManager(new LinearLayout(Direction.HORIZONTAL))
                .setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

        new Button("Update", this::update).addTo(root);

        new Button("Delete", this::delete).addTo(root);
        return root;
    }

    private void delete() {
    }

    private void update() {
    }


}

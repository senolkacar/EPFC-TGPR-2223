package tgpr.bank.view;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.bank.controller.DisplayAccountAccessController;
import tgpr.bank.model.Account;

import java.util.List;

public class DisplayAccountAccessView extends DialogWindow {
    private final DisplayAccountAccessController controller;
    private final Account account;


    public DisplayAccountAccessView(DisplayAccountAccessController controller, Account account) {
        super("Update Access");
        this.controller = controller;
        this.account = account;

        setHints(List.of(Window.Hint.CENTERED));
        setCloseWindowWithEscape(true);

        Panel root = new Panel().setLayoutManager(new LinearLayout(Direction.VERTICAL).setSpacing(1));
        setComponent(root);

    //    createButtonsPanel().addTo(root);

    }


}

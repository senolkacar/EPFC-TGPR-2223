package tgpr.bank.view;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;

import java.util.List;

import static java.time.zone.ZoneRulesProvider.refresh;
import static javax.swing.text.StyleConstants.setComponent;

public class DisplayAccessAccountView  extends DialogWindow {
    private final DisplayAccessAccountView controller;

    public DisplayAccessAccountView(DisplayAccessAccountView controller) {
        super("Access Account");
       this.controller = controller;


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

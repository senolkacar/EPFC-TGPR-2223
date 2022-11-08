package tgpr.bank.view;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.framework.Tools;
import tgpr.bank.controller.DisplayCategoryController;
import tgpr.bank.model.Category;

import java.util.List;

public class DisplayCategoryView extends DialogWindow {

    private final DisplayCategoryController controller;
    private Category category;
    private final Label lblName;
    private  TextBox txtName;
    private Label errName;

    private final Label lblType;


    public DisplayCategoryView(DisplayCategoryController controller, Category category) {
        super("View Category");

        this.controller = controller;
        this.category = category;

        setHints(List.of(Hint.CENTERED));
        setCloseWindowWithEscape(true);

        Panel root = new Panel();
        setComponent(root);

        Panel fields = new Panel().setLayoutManager(new GridLayout(2).setTopMarginSize(1)).addTo(root);

        fields.addComponent(new Label("name:"));
        lblName = new Label("").addTo(fields).addStyle(SGR.BOLD);


        fields.addComponent(new Label("type:"));
        lblType = new Label("").addTo(fields).addStyle(SGR.BOLD);

        new EmptySpace().addTo(root);

        var buttons = new Panel().setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
        new Button("Update", this::update).addTo(buttons);
        new Button("Delete", this::delete).addTo(buttons);
        new Button("Close", this::close).addTo(buttons);

        root.addComponent(buttons, LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

        refresh();
    }

    private void refresh() {
        if (category != null) {
            lblName.setText(category.getName());
            lblType.setText(category.isSystem() ? "local" : "System");
        }
    }

    private void update() {
        category = controller.update();
        refresh();
    }


    private void delete() {
        controller.delete();
    }

}

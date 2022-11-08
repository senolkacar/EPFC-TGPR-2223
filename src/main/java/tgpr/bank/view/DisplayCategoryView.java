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

    private DisplayCategoryController controller;
    private  Category category;
    private  TextBox txtName;
    private Label errName;
    private TextBox txtCategoryName;


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

        new Label("Name:").addTo(fields);
        txtCategoryName = new TextBox().addTo(fields)
                .setText(category.getName());



        fields.addComponent(new Label("type:"));
        lblType = new Label("").addTo(fields).addStyle(SGR.BOLD);

        new EmptySpace().addTo(root);

   //     var buttons = new Panel().setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
  //      new Button("Update", this::update).addTo(root);

 //       new Button("Delete", this::delete).addTo(buttons);
 //       new Button("Close", this::close).addTo(root);

//        root.addComponent(buttons, LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        createButtonsPanel().addTo(root);
        refresh();
    }

    private Panel createButtonsPanel(){
        var root = new Panel()
                .setLayoutManager(new LinearLayout(Direction.HORIZONTAL))
                .setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

        new Button("Update", this::update).addTo(root);

        new Button("Delete", this::delete).addTo(root);
        new Button("Close", this::close).addTo(root);

        return root;
    }

    private void update() {
        var name = txtCategoryName.getText();
        category = controller.update(name,category);
        txtCategoryName.setText(name);
        controller.close();
    }



    private void refresh() {
        if (category != null) {
            txtCategoryName.setText(category.getName());
            lblType.setText(category.isSystem() ? "local" : "System");
        }
    }




    private void delete() {
        controller.delete();
    }

}

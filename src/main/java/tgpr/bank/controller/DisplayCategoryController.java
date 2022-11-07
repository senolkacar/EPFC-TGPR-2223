package tgpr.bank.controller;

import com.googlecode.lanterna.gui2.Window;
import tgpr.bank.model.Category;
import tgpr.bank.view.DisplayCategoryView;
import tgpr.framework.Controller;
import tgpr.framework.ErrorList;
import tgpr.framework.Tools;

public class DisplayCategoryController extends Controller {
    private final DisplayCategoryView view;
    private final Category category;

    public DisplayCategoryController(Category category) {
        this.category = category;
        view = new DisplayCategoryView(this, category);
    }

    public void delete() {
        if (askConfirmation("You are about to delete this category. Please confirm.", "Delete category")) {
            category.delete();
            view.close();
        }
    }

    public Category update() {
       // var controller = new EditCategoryController(category);
       // navigateTo(controller);
        //
        // return (Category) controller.getCategory();
        return category
                ;
    }

    @Override
    public Window getView() {
        return view;
    }

    public ErrorList validate(String body) {
        var errors = new ErrorList();

        return errors;
    }

}
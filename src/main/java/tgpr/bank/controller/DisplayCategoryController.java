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




    public void delete(Category category){
        category.delete(category);
    }

    public Category update(String name,Category category) {
        category.update(name,category);
        return category;
    }

    @Override
    public Window getView() {
        return view;
    }

    public ErrorList validate(String body) {
        var errors = new ErrorList();

        return errors;
    }

    public void close(){
        view.close();
    }

}
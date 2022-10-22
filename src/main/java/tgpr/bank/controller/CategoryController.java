package tgpr.bank.controller;

import com.googlecode.lanterna.gui2.Window;

import tgpr.bank.model.Category;

import tgpr.bank.view.ViewCategoryList;
import tgpr.framework.Controller;

import java.util.List;

public class CategoryController extends Controller {

    private List<Category>  categories;
    @Override
    public Window getView() {
        return new ViewCategoryList(this);
    }





    public List<Category> getCategory() {
        return Category.getAll();
    }


    public void logout() {
    }

    public void exit() {
    }
}

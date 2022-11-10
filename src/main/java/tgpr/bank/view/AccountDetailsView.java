package tgpr.bank.view;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.bank.controller.AccountDetailsController;
import tgpr.bank.model.Account;
import tgpr.bank.model.Category;
import tgpr.bank.model.Security;
import tgpr.framework.ColumnSpec;
import tgpr.framework.ObjectTable;
import tgpr.framework.Tools;

import java.util.List;



public class AccountDetailsView extends DialogWindow {
    private final AccountDetailsController controller;
    private  ObjectTable<Category> categoryTable;

    private final Account account;


    private final Label lblIban = new Label("");
    private final Label lblTitle = new Label("");
    private final Label lblType = new Label("");
    private final Label lblSaldo = new Label("");

    private  TextBox txtNewCategory;



    public AccountDetailsView(AccountDetailsController controller, Account account) {
        super("Account Details");
        this.controller = controller;
        this.account = account;
        setHints(List.of(Hint.CENTERED, Hint.FIXED_SIZE));
        setCloseWindowWithEscape(true);
        setFixedSize(new TerminalSize(115, 20));

        Panel root = new Panel().setLayoutManager(new LinearLayout(Direction.VERTICAL).setSpacing(1));
        setComponent(root);

        new EmptySpace().addTo(root);

        accountDetailPanel().addTo(root);
        historyPanel().addTo(root);
        favorites_categoryPanel().addTo(root);
        buttonPanel().addTo(root);
        refresh();

    }

    private Panel accountDetailPanel(){
        Panel panel = new Panel().setLayoutManager(new GridLayout(4).setHorizontalSpacing(2));
        panel.addComponent(new Label("IBAN:"));
        lblIban.addTo(panel).addStyle(SGR.BOLD);

        panel.addComponent(new Label("Type:"));
        lblType.addTo(panel).addStyle(SGR.BOLD);

        panel.addComponent(new Label("Title:"));
        lblTitle.addTo(panel).addStyle(SGR.BOLD);


        panel.addComponent(new Label("Saldo:"));
        lblSaldo.addTo(panel).addStyle(SGR.BOLD);

        return panel;

    }

    private Border historyPanel() {
       var panel = new Panel().setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));
       Border border = panel.withBorder(Borders.doubleLine("History"));

       return border;
    }



    private Panel favorites_categoryPanel(){
        Panel panel = new Panel().setLayoutManager(new LinearLayout(Direction.HORIZONTAL).setSpacing(1));
        panel.addComponent(categoryPanel());
        panel.addComponent(favoritePanel());
        return panel;
    }

    private Border categoryPanel(){
        var panel = new Panel().setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));
        Border border = panel.withBorder(Borders.singleLine("Category"));
        categoryTable = new ObjectTable<>(

                new ColumnSpec<>("Category", c -> (c.getName())),
                new ColumnSpec<Category>("type", c -> c.isSystem() ? "local" : "system"),
                new ColumnSpec<>("Uses", c-> controller.getCategoryUses(c).size())
        );
        categoryTable.setPreferredSize(new TerminalSize(50, 5));
        categoryTable.addTo(panel);
        categoryTable.setSelectAction(() -> {
            var category = categoryTable.getSelected();
            if(!category.isSystem()){
                controller.showError();
            }else{
                controller.editCategory(category);
                reloadData();

                categoryTable.setSelected(category);

            }

        });


        Panel root = new Panel();
        root.setLayoutManager(new LinearLayout(Direction.HORIZONTAL).setSpacing(1));

        txtNewCategory = new TextBox().setPreferredSize(new TerminalSize(15,1)).addTo(root);
        Button btnAddCatrgoty = new Button("Add",this::addCategory).addTo(root);
        Button btnResetCatrgoty = new Button("Reset", this::resetCategory).addTo(root);
        root.addTo(panel);
        reloadData();



        return  border;
    }
    public void resetCategory(){
        txtNewCategory.setText("");

    }



    private void addCategory(){
       controller.add(txtNewCategory.getText(),account.getId());
        txtNewCategory.setText("");
        reloadData();
        }
    public void reloadData() {

     categoryTable.clear();
     var Category  = controller.getCategory();
     categoryTable.add(Category);


        }



    private Border favoritePanel() {
        var panel = new Panel().setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));
        Border border = panel.withBorder(Borders.doubleLine("Favorite"));

        return border;
    }

    private Panel buttonPanel() {
        Panel panel = new Panel().setLayoutManager(new LinearLayout(Direction.HORIZONTAL).setSpacing(1));
        Button btnNewTransfer = new Button("New Transfer").addTo(panel);
        Button btnExit = new Button("Exit").addTo(panel);
        return panel;
    }

    public void refresh(){
        lblIban.setText(account.getIban());
        lblTitle.setText(account.getTitle());
        lblType.setText(account.getType());
        lblSaldo.setText(Tools.toString(account.getSaldo()));
    }

}

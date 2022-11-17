package tgpr.bank.view;

import com.googlecode.lanterna.gui2.*;
import tgpr.bank.controller.AccountFavoritListController;

import java.util.List;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.table.Table;
import tgpr.framework.ColumnSpec;
import tgpr.framework.ObjectTable;
import tgpr.framework.Tools;
import tgpr.framework.ViewManager;
import tgpr.bank.model.Account;

public class AccountFavoritListView extends BasicWindow {

    private  AccountFavoritListController controller;
    private  ObjectTable<Account> table;

    public AccountFavoritListView(AccountFavoritListController controller) {
        this.controller = controller;

        setTitle("Favourit Accounts");
        setHints(List.of(Hint.EXPANDED));

        Panel root = new Panel();

        setComponent(root);

        new EmptySpace().addTo(root);

        table = new ObjectTable<>(
                new ColumnSpec<>("IBAN", Account ::getIban),

                new ColumnSpec<>("  TITLE", Account ::getTitle),
                new ColumnSpec<>("TYPE", Account ::getType)
        );

        root.addComponent(table);

        table.setPreferredSize(new TerminalSize(ViewManager.getTerminalColumns(), 15));


        new EmptySpace().addTo(root);

        // crée un bouton pour l'ajout d'un membre et lui associe une fonction lambda qui sera appelée
        // quand on clique sur le bouton
        var btnAddMember = new Button("Add Member", () -> {
            System.out.println("Add Member");
        }).addTo(root);

        reloadData();

    }
    public void reloadData() {

        table.clear();

        var favoriteAccount = controller.getFavouritAccounts();

        table.add(favoriteAccount);
    }
}
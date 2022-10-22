package tgpr.bank.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;

import tgpr.bank.controller.CategoryController;
import tgpr.bank.model.Category;

import tgpr.framework.Layouts;
import tgpr.framework.ObjectTable;

import tgpr.framework.ColumnSpec;
import tgpr.framework.ViewManager;

import java.util.List;
import java.util.function.Function;

public class ViewCategoryList extends BasicWindow {
    private final CategoryController controller;
    private final ObjectTable<Category> table;



    public ViewCategoryList(CategoryController controller) {

        this.controller = controller;

        setTitle("categories");
        setHints(List.of(Hint.EXPANDED));
        // Le panel 'root' est le composant racine de la fenêtre (il contiendra tous les autres composants)
        Panel root = new Panel();
        setComponent(root);



        // ajoute une ligne vide
        new EmptySpace().addTo(root);

        // crée un tableau de données pour l'affichage des membres
        table = new ObjectTable<>(
                new ColumnSpec<>("name", Category::getName),
                new ColumnSpec<Category>("type", c -> c.isAccount() ? "System" : "local"),
                new ColumnSpec<>("Uses",Category::getId)



        );
        // ajoute le tableau au root panel
        root.addComponent(table);
        // spécifie que le tableau doit avoir la même largeur quee le terminal et une hauteur de 15 lignes
        table.setPreferredSize(new TerminalSize(ViewManager.getTerminalColumns(), 15));
        new EmptySpace().addTo(root);

        // crée un bouton pour l'ajout d'un membre et lui associe une fonction lambda qui sera appelée
        // quand on clique sur le bouton

        // ajoute le tableau au root panel
        root.addComponent(table);
        // spécifie que le tableau doit avoir la même largeur quee le terminal et une hauteur de 15 lignes
        table.setPreferredSize(new TerminalSize(ViewManager.getTerminalColumns(), 15));
        // charge les données dans la table
        // charge les données dans la table

        new Label("").addTo(root);
        Object txtProfile = new TextBox(new TerminalSize(21, 1)).addTo(root)
                .setTextChangeListener((txt, byUser) -> validate());
        new EmptySpace().addTo(root);
        Label errProfile = new Label("").addTo(root).setForegroundColor(TextColor.ANSI.RED);
        Panel buttons = new Panel().setLayoutManager(new LinearLayout(Direction.HORIZONTAL))
                .setLayoutData(Layouts.LINEAR_CENTER).addTo(root);
        Button btnLAdd = new Button("add", this::add).addTo(buttons);
        Button btnRest = new Button("Exit", this::Reset).addTo(buttons);
        reloadData();
    }

    private void Reset() {
    }

    private void add() {
    }

    
    
    private void validate() {
    }

    public void reloadData() {
        // vide le tableau
        table.clear();
        // demande au contrôleur la liste des membres
        var categories = controller.getCategory();
        // ajoute l'ensemble des membres au tableau
        table.add(categories);
    }

}

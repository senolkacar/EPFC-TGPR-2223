package tgpr.bank.view;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.bank.controller.DisplayTransferController;
import tgpr.bank.model.Transfer;
import tgpr.bank.model.User;
import tgpr.framework.Controller;
import tgpr.framework.Tools;

import java.util.List;

public class DisplayTransferView extends DialogWindow {

    private final DisplayTransferController controller;
    private final Transfer transfer;

    private final Label lblCreatedAt = new Label("");
    private final Label lblEffectiveAt= new Label("");
    private final Label lblCreatedBy = new Label("");
    private final Label lblSourceAccount = new Label("");
    private final Label lblTargetAccount = new Label("");
    private final Label lblAmount = new Label("");
    private final Label lblSaldoAfterTransfer = new Label("");
    private final Label lblDescription = new Label("");
    private final Label lblState = new Label("");

    private final Label lblCategory = new Label("");

    private ComboBox<String> cboCategory;

    public DisplayTransferView(DisplayTransferController controller, Transfer transfer) {
        super("View Transfer");

        this.controller = controller;
        this.transfer = transfer;

        setHints(List.of(Window.Hint.CENTERED));
        setCloseWindowWithEscape(true);

        Panel root = new Panel().setLayoutManager(new LinearLayout(Direction.VERTICAL).setSpacing(1));
        setComponent(root);

        createFieldsPanel().addTo(root);
        createButtonsPanel().addTo(root);

        refresh();
    }

    private Panel createFieldsPanel() {
        Panel panel = new Panel().setLayoutManager(new GridLayout(2).setTopMarginSize(1));

        panel.addComponent(new Label("Created At:"));
        lblCreatedAt.addTo(panel).addStyle(SGR.BOLD);

        panel.addComponent(new Label("Effective At:"));
        lblEffectiveAt.addTo(panel).addStyle(SGR.BOLD);

        panel.addComponent(new Label("Created By:"));
        lblCreatedBy.addTo(panel).addStyle(SGR.BOLD);

        panel.addComponent(new Label("Source Account"));
        lblSourceAccount.addTo(panel).addStyle(SGR.BOLD).setLabelWidth(65);

        panel.addComponent(new Label("Target Account:"));
        lblTargetAccount.addTo(panel).addStyle(SGR.BOLD).setLabelWidth(65);

        panel.addComponent(new Label("Amount:"));
        lblAmount.addTo(panel).addStyle(SGR.BOLD);

        panel.addComponent(new Label("Saldo after transfer:"));
        lblSaldoAfterTransfer.addTo(panel).addStyle(SGR.BOLD);

        panel.addComponent(new Label("Description:"));
        lblDescription.addTo(panel).addStyle(SGR.BOLD);

        panel.addComponent(new Label("State:"));
        lblState.addTo(panel).addStyle(SGR.BOLD);

//        new Label("Category:").addTo(panel);
//        var relationshipTypes = Member.RelationshipType.getSortedStrings();
//        relationshipTypes.add(0, "All");
//        cboRelationship = new ComboBox<>(relationshipTypes).addTo(panel)
//                .addListener((newIndex, oldIndex, byUser) -> reloadData());

        return panel;
    }

    private void refresh() {
        if (transfer != null) {
            lblCreatedAt.setText(String.valueOf(transfer.getCreatedAt()));
            lblEffectiveAt.setText(String.valueOf(transfer.getEffectiveAt()));
            lblCreatedBy.setText((User.getById(transfer.getCreatedBy())));
            lblSourceAccount.setText(transfer.getSourceAccountInfoForTransfer(transfer.getSourceAccountID()));
            lblTargetAccount.setText(transfer.getTargetAccountInfoForTransfer(transfer.getTargetAccountID()));
            lblAmount.setText(transfer.transformInEuro(transfer.getAmount()));
//            lblSaldoAfterTransfer -> Rien n'est afficher sur l'enoncé du projet.
            lblDescription.setText(transfer.getDescription());
            lblState.setText(transfer.getState());
//            lblCategory; -> liste déroulante à faire


        }
    }


    // A FAIRE PLUS TARD (SAVE)
    private Panel createButtonsPanel() {
        var panel = new Panel()
                .setLayoutManager(new LinearLayout(Direction.HORIZONTAL))
                .setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

        String result = "future";
        new Button("Delete", this::delete).addTo(panel)
                .setVisible((result.equals(transfer.getState())));
        // RESTE ENCORE A FAIRE LES BONNE REQUETES SQL CAR ERREUR LORSQU'ON DELETE
        new Button("Close", this::close ).addTo(panel);

        return panel;
    }

    private void delete() {
        controller.delete();
    }

}

package tgpr.bank.view;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.bank.controller.DisplayTransferController;
import tgpr.bank.model.Transfer;
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

    public DisplayTransferView(DisplayTransferController controller, Transfer transfer) {
        super("View Transfer");

        this.controller = controller;
        this.transfer = transfer;

        setHints(List.of(Window.Hint.CENTERED));
        setCloseWindowWithEscape(true);

        Panel root = new Panel().setLayoutManager(new LinearLayout(Direction.VERTICAL).setSpacing(1));
        setComponent(root);

        createFieldsPanel().addTo(root);
 //       A FAIRE PLUS TARD
 //       createButtonsPanel().addTo(root);

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
        lblSourceAccount.addTo(panel).addStyle(SGR.BOLD).setLabelWidth(50);

        panel.addComponent(new Label("Target Account:"));
        lblTargetAccount.addTo(panel).addStyle(SGR.BOLD);

        panel.addComponent(new Label("Amount:"));
        lblAmount.addTo(panel).addStyle(SGR.BOLD);

        panel.addComponent(new Label("Saldo after transfer:"));
        lblSaldoAfterTransfer.addTo(panel).addStyle(SGR.BOLD);

        panel.addComponent(new Label("Description:"));
        lblDescription.addTo(panel).addStyle(SGR.BOLD);

        panel.addComponent(new Label("State:"));
        lblState.addTo(panel).addStyle(SGR.BOLD);

        panel.addComponent(new Label("Category:"));
        lblCategory.addTo(panel).addStyle(SGR.BOLD).setLabelWidth(30);

        return panel;
    }

    private void refresh() {
        if (transfer != null) {
            lblCreatedAt.setText(String.valueOf(transfer.getCreatedAt()));
            lblEffectiveAt.setText(String.valueOf(transfer.getEffectiveAt()));
            lblCreatedBy.setText(String.valueOf(transfer.getCreatedBy()));
            lblSourceAccount.setText(String.valueOf(transfer.getAccountInfo(transfer.getSourceAccountID())));
            lblTargetAccount.setText(String.valueOf(transfer.getAccountInfo(transfer.getTargetAccountID())));
            lblAmount.setText(transfer.getAmountWithEuroSign());
//            lblSaldoAfterTransfer -> Rien n'est afficher sur l'enoncé du projet.
            lblDescription.setText(transfer.getDescription());
            lblState.setText(transfer.getState());
//            lblCategory; -> liste déroulante à faire


        }
    }


    // A FAIRE PLUS TARD (DELETE & SAVE)
//    private Panel createButtonsPanel() {
//        var panel = new Panel()
//                .setLayoutManager(new LinearLayout(Direction.HORIZONTAL))
//                .setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
//
//        new Button("Delete", this::delete).addTo(panel)
//                .setVisible(transfer.canDelete(Security.getLoggedUser()));
//        new Button("Close", this::close).addTo(panel);
//
//        return panel;
//    }

}

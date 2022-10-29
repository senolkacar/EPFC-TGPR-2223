package tgpr.bank.controller;

import com.googlecode.lanterna.gui2.Window;
import tgpr.bank.model.Transfer;
import tgpr.bank.view.DisplayTransferView;
import tgpr.framework.Controller;

public class DisplayTransferController extends Controller {

    private final DisplayTransferView view;

    private Transfer transfer;

    public DisplayTransferController(Transfer transfer) {
        this.transfer = transfer;
        view = new DisplayTransferView(this, transfer);
    }

//    public Transfer displayTransfer(Transfer transfer) {
//        var controller = new DisplayTransferController(transfer);
//        navigateTo(controller);
//        return controller.();++-++-
    @Override
    public Window getView() {
        return view;
    }
    public Transfer getTransfer() {
        return transfer;
    }

    public void delete() {
        if (askConfirmation("You are about to delete this transfer. Please confirm.", "Delete Transfer")) {
            transfer.delete();
            view.close();
            transfer = null;
        }
    }

}

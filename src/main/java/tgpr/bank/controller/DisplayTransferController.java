package tgpr.bank.controller;

import com.googlecode.lanterna.gui2.Window;
import tgpr.bank.model.Account;
import tgpr.bank.model.Transfer;
import tgpr.bank.view.DisplayTransferView;
import tgpr.framework.Controller;

public class DisplayTransferController extends Controller {

    private final DisplayTransferView view;

    private Transfer transfer;

    public DisplayTransferController(Transfer transfer, Account account) {
        this.transfer = transfer;
        view = new DisplayTransferView(this, transfer, account);
    }
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

    public void save(int accountID, int transferID, int categoryID){
        transfer.save(accountID,transferID,categoryID);
        view.close();
    }
    public void update(int accountID, int transferID, int categoryID) {
        transfer.update(accountID, transferID, categoryID);
        view.close();
    }
    public void deleteTransferCategory (int accountID){
        transfer.deleteTransferCategory(accountID);
    }

    public void close(){
        view.close();
    }

}

package tgpr.bank.model;

import org.springframework.cglib.core.Local;
import tgpr.framework.Model;
import tgpr.framework.Params;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Transfer extends Model {

    public enum Fields{
        Amount,Description,SourceAccountIban,TargetAccountIban,TargetAccountTitle,SourceSaldo,TargetSaldo,CreatedAT,CreatedBy,EffectiveAt
    }

    private LocalDateTime date = DateInterface.getUsedDate();
    public String getDate() {
        return String.valueOf(date);
    }

    public static List<Transfer> getAllTransfersForBTTF(){
        String sql = "select * from transfer order by ifnull(effective_at, created_at)";
        return queryList(Transfer.class, sql,new Params());
    }

    private LocalDateTime createdAtLDT;

    public LocalDateTime getCreatedAtLDT() {
        return createdAtLDT;
    }

    public LocalDateTime getEffectiveAtLDT() {
        return effectiveAtLDT;
    }
    public String getStringEffectiveAtLDT(){
        if (effectiveAtLDT!=null){
            return effectiveAtLDT.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        }
        else{
            return createdAt;
        }
    }


    private LocalDateTime effectiveAtLDT;
    private int id;
    private double amount;
    private String description;
    private int sourceAccountID;
    private int targetAccountID;
    private double sourceSaldo;
    private double targetSaldo;
    private String createdAt;

    private String createdAtHistory;

    private int createdBy;
    private String effectiveAt;

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public static String transformInEuro(double montant){
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String moneyString = formatter.format(montant);
        return (moneyString);
    }
    public String getCreatedAtHistory() {
        return createdAtHistory;
    }

    public String getDescription() {
        return description;
    }

    public int getSourceAccountID() {
        return sourceAccountID;
    }

    public int getTargetAccountID() {
        return targetAccountID;
    }

    public double getSourceSaldo() {
        return sourceSaldo;
    }

    public double getTargetSaldo() {
        return targetSaldo;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public String getEffectiveAt() {
        return effectiveAt;
    }

    public String getState() {
        return state;
    }

    private String state;


    @Override
    protected void mapper(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.amount = rs.getDouble("amount");
        this.description = rs.getString("description");
        this.sourceAccountID = rs.getInt("source_account");
        this.targetAccountID = rs.getInt("target_account");
        this.sourceSaldo = rs.getDouble("source_saldo");
        this.targetSaldo = rs.getDouble("target_saldo");
        this.createdAt = rs.getTimestamp("created_at").toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        this.createdBy = rs.getInt("created_by");
        this.effectiveAt = rs.getObject("effective_at") != null ? rs.getTimestamp("effective_at").toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) : null;
        this.state = rs.getString("state");

        this.createdAtHistory = rs.getTimestamp("created_at").toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.createdAtLDT = rs.getObject("created_at", LocalDateTime.class);
        this.effectiveAtLDT = rs.getObject("effective_at", LocalDateTime.class);
    }

    @Override
    public void reload() {
        String sql = "select * from transfer where source_account=:source_account or target_account=:target_account ORDER by GREATEST( COALESCE(effective_at, 0), COALESCE(created_at, 0) )DESC";
        reload(sql,new Params()
                .add("source_account",1)
                .add("target_account",1));
    }

    public static int getAccountId(Account account) {
        return account.getId();
    }
    public static int getSourceAccountId(Transfer transfer) {
        return transfer.getSourceAccountID();
    }
    public static int getTargetAccountId(Transfer transfer) {
        return transfer.getTargetAccountID();
    }

    public static Account getAccountInfo(int accountID){
        return queryOne(Account.class, "select * from account where id = :id", new Params("id",accountID));
    }
    public static Account getAccount(int accountID){
        Account account = getAccountInfo(accountID);
        return account;
    }

    public static String getAccountInfoForTransfer(int id, int accountid){
        Account a = queryOne(Account.class, "select * from account where id=:id", new Params("id", id));
        String s = a.getIban() + " | " + a.getTitle() + " | " + a.getType() + " | "+ a.getSaldoWithEuroSign();
        if(isMyAccount(accountid)) {
            s += " (your account)";
        }
        return s;
    }

    public static boolean isMyAccount(int accountid){
        Account a = queryOne(Account.class, "SELECT * from account where id = (SELECT id from access Where account=:accountid AND user=:user)", new Params()
                .add("accountid",accountid)
                .add("user",Security.getLoggedUser().getId()));

        if(a==null){
            return false;
        }
        else {
            return true;
        }

    }

    public Account getSourceAccount() {
        return getAccount(sourceAccountID);
    }

    public Account getTargetAccount() {
        return getAccount(targetAccountID);
    }

    public static Category getCategoryTransfer(int accountID, int transferID){
        String sql ="select * from category where category.id in(select category from transfer_category where transfer_category.transfer = :transfer and transfer_category.account = :account)";
        return queryOne(Category.class, sql, new Params("account",accountID).add("transfer",transferID));
    }

    public static Category getCategory(int accountID, int transferID){
        return getCategoryTransfer(accountID, transferID);
    }

    public static List<Transfer> getTransfers(int accountID){
        return queryList(Transfer.class, "select * from transfer where source_account=:source_account or target_account=:target_account ORDER BY effective_at DESC, created_at DESC", new Params()
                .add("source_account",accountID)
                .add("target_account",accountID));
    }

    public static void addTransferToDB(Double amount, String description, Integer sourceAccountID, Integer targetAccountID, Double sourceSaldo, Double targetSaldo, String createdAT, Integer createdBy, LocalDate effectiveAT, String state ){
        execute("insert into Transfer(amount,description,source_account,target_account,source_saldo,target_saldo,created_at,created_by,effective_at,state)" +
                "values(:amount,:description,:sourceAccountID,:targetAccountID,:sourceSaldo,:targetSaldo,:createdAT,:createdBy,:effectiveAT,:state)",
                new Params()
                .add("amount",amount)
                .add("description", description)
                .add("sourceAccountID",sourceAccountID)
                .add("targetAccountID",targetAccountID)
                .add("sourceSaldo",sourceSaldo)
                .add("targetSaldo",targetSaldo)
                .add("createdAT",createdAT)
                .add("createdBy",createdBy)
                .add("effectiveAT",effectiveAT)
                .add("state",state));
    }

    public static Transfer getLastCreatedTransfer(){
        return queryOne(Transfer.class,"select * from transfer where transfer.id = (select MAX(id) from transfer)");
    }

    public static List<Transfer> getTransfers(Account account) {
        return queryList(Transfer.class, "select * from transfer where (source_account=:source_account or target_account=:target_account) ORDER by GREATEST( COALESCE(effective_at, 0), COALESCE(created_at, 0) )DESC", new Params()
                .add("source_account",account.getId())
                .add("target_account",account.getId()));
    }

    public static List<Transfer> getTransfersForLabel(Account account) {
        return queryList(Transfer.class, "select * from transfer where (source_account=:source_account or target_account=:target_account) ORDER by GREATEST( COALESCE(effective_at, 0), COALESCE(created_at, 0) )ASC", new Params()
                .add("source_account",account.getId())
                .add("target_account",account.getId()));
    }

    public static Transfer getTransfer(int id) {
        return queryOne(Transfer.class, "select * from transfer where id=:id", new Params("id",id));
    }


    public static List<Transfer> getTransfersFilter(Account account, String filter) {
        String sql = "select * from transfer where (source_account=:source_account or target_account=:target_account) and (transfer.amount like :filter or transfer.description like :filter or transfer.source_saldo like :filter or transfer.created_at like :filter or transfer.effective_at like :filter or transfer.state like :filter) ORDER by GREATEST( COALESCE(effective_at, 0), COALESCE(created_at, 0) )DESC";
        return queryList(Transfer.class,sql, new Params()
                    .add("source_account",account.getId())
                    .add("target_account",account.getId())
                    .add("filter","%"+filter+"%"));

    }

    @Override
    public String toString() {
        return "id=" + id +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", sourceAccountID=" + sourceAccountID +
                ", targetAccountID=" + targetAccountID +
                ", sourceSaldo=" + sourceSaldo +
                ", targetSaldo=" + targetSaldo +
                ", createdAt='" + createdAt + '\'' +
                ", createdBy=" + createdBy +
                ", effectiveAt='" + effectiveAt + '\'' +
                ", state='" + state + '\'';
    }
    public void delete() {

        execute("delete from transfer_category where transfer=:id", new Params()
                .add("id", id));
        execute("delete from transfer where id=:id", new Params()
                .add("id", id));
    }
    public void deleteTransferCategory(int account) {
        execute("delete from transfer_category where transfer_category.transfer = (SELECT id FROM transfer where transfer.id=:transfer) AND transfer_category.account = (SELECT id FROM account WHERE account.id=:account)", new Params()
                .add("transfer",id)
                .add("account",account));
    }
    public List<Category> getCategoriesById(int id){
        return queryList(Category.class,
                "SELECT * FROM category where account=:account or account is null",
                new Params("account",id));
    }

    public Category getIdCategoryWithName(String name, int account){
        return queryOne(Category.class,
                "SELECT * FROM category where name=:name and (account=:account || account is null)" ,
                new Params()
                        .add("name",name)
                        .add("account",account))
                            ;
    }

    public void save(int account, int transfer, int category){
        execute("INSERT INTO transfer_category (transfer,account,category) VALUES ((SELECT id FROM transfer WHERE transfer.id=:transfer ), (SELECT id from account where account.id=:account) , (SELECT id from category WHERE category.id=:category))", new Params()
                .add("transfer",transfer)
                .add("account",account)
                .add ("category",category));
    }

    public void update(int account, int transfer, int category){
        execute("UPDATE transfer_category SET category=:category WHERE account=:account AND transfer=:transfer", new Params()
                .add("transfer",transfer)
                .add("account",account)
                .add ("category",category));
    }
    public static List<Transfer> updateEverything(List<Transfer> transfers, Account ab){

        String sql = "select * from transfer where created_at > :date";
        List<Transfer> listOfTransfersToBeDeleted = queryList(Transfer.class, sql,new Params()
                .add("date", (DateInterface.getUsedDate())));
        for (Transfer transfer: listOfTransfersToBeDeleted
             ) {
            execute("delete from transfer_category where transfer =( select id from transfer where created_at > :date and id=:id)", new Params()
                    .add("date", (DateInterface.getUsedDate()))
                    .add("id",transfer.getId()));
            execute("delete from transfer where created_at > :date and id=:id", new Params()
                    .add("date", (DateInterface.getUsedDate()))
                    .add("id",transfer.getId()));
        }

        execute("update account set saldo = 0 where type != 'external'" , new Params());

        execute("update transfer set source_saldo = NULL, target_saldo = NULL", new Params());

        for (Transfer transfer:transfers) {

           LocalDateTime execDate;
            if (transfer.effectiveAtLDT!=null){
                execDate=transfer.effectiveAtLDT;
            }
            else {
                execDate=transfer.createdAtLDT;
            }
            if (execDate.compareTo((DateInterface.getUsedDate()))<=0){
                execute("UPDATE transfer SET state='executed' WHERE id=:id", new Params()
                        .add("id",transfer.id));

                Account a = getAccount(transfer.sourceAccountID);
                Account b = getAccount(transfer.targetAccountID);

                if(!a.getType().equals("external")){
                    if((a.getSaldo()-transfer.amount)>= a.getFloor()){
                        double newAmountSource = a.getSaldo()-transfer.amount;
                        execute("UPDATE account SET saldo=:newAmount where id=:id", new Params()
                                .add("newAmount", newAmountSource)
                                .add("id",a.getId()));

                        if(!b.getType().equals("external")){
                            double newAmountTarget = b.getSaldo()+ transfer.amount;
                            execute("UPDATE account SET saldo=:newAmount where id=:id", new Params()
                                    .add("newAmount", newAmountTarget)
                                    .add("id",b.getId()));
                        }
                    }
                    else {
                        execute("UPDATE transfer SET state='rejected' WHERE id=:id", new Params()
                                .add("id",transfer.id));
                    }
                }
                else if (!b.getType().equals("external")){
                    double newAmountTarget = b.getSaldo()+ transfer.amount;
                    execute("UPDATE account SET saldo=:newAmount where id=:id", new Params()
                            .add("newAmount", newAmountTarget)
                            .add("id",b.getId()));
                }
            }
            else if(transfer.createdAtLDT.compareTo(Date.changeFormatToEn(DateInterface.getUsedDate()))<=0) {
                execute("UPDATE transfer SET state='future' WHERE id=:id", new Params()
                        .add("id", transfer.id));
            }
            else{
                execute("UPDATE transfer SET state='ignored' WHERE id=:id", new Params()
                        .add("id",transfer.id));

            }
        }
        return transfers;
    }

    public static void updateDatabase(Account ab, Transfer transfer){
                    double somme = 0;
            List<Transfer> transferSaldo = Transfer.getTransfersForLabel(ab);
            for (Transfer transferr : transferSaldo) {
                if (!transferr.getState().equals("rejected")) {
                    if (transferr.getSourceAccountID() == ab.getId()) {
                        somme -= transferr.getAmount();
                    } else {
                        somme += transferr.getAmount();
                    }
                }
                if ((transferr.getId() == (transfer.getId())) && (!transferr.getState().equals("future") && (!transferr.getState().equals("rejected")))) {
                    if(ab.getType().equals("external")) {
                        if (transfer.getSourceAccount() != ab) {
                            execute("UPDATE transfer SET source_saldo=:newAmount where id=:id", new Params()
                                    .add("id", transferr.getId())
                                    .add("newAmount", somme ));
                        } else {
                            execute("UPDATE transfer SET target_saldo=:newAmount where id=:id", new Params()
                                    .add("id", transferr.getId())
                                    .add("newAmount", somme + transferr.getAmount()));
                        }
                    }
                    else{
                        if (Account.isExternal(transfer.getTargetAccount().getId())){
                            execute("UPDATE transfer SET source_saldo=:newAmount where id=:id", new Params()
                                    .add("id", transferr.getId())
                                    .add("newAmount", somme + transferr.getAmount()));
                        }
                        else {
                            execute("UPDATE transfer SET source_saldo=:newAmount where id=:id", new Params()
                                    .add("id", transferr.getId())
                                    .add("newAmount", somme));

                            execute("UPDATE transfer SET target_saldo=:newAmount where id=:id", new Params()
                                    .add("id", transferr.getId())
                                    .add("newAmount", somme + transferr.getAmount()));
                        }
                    }
                }
            }
    }

    public static void deleteEverything(){

        execute("update account set saldo = 0 where type != 'external'" , new Params());

        execute("update transfer set source_saldo = NULL, target_saldo = NULL", new Params());
    }
}


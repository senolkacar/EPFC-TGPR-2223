package tgpr.bank.model;

import tgpr.framework.Model;
import tgpr.framework.Params;
import tgpr.framework.Tools;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.List;

public class Transfer extends Model {

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

    public String transformInEuro(double montant){
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
        this.amount = rs.getInt("amount");
        this.description = rs.getString("description");
        this.sourceAccountID = rs.getInt("source_account");
        this.targetAccountID = rs.getInt("target_account");
        this.sourceSaldo = rs.getDouble("source_saldo");
        this.targetSaldo = rs.getDouble("target_saldo");
        this.createdAt = rs.getTimestamp("created_at").toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        this.createdAtHistory = rs.getTimestamp("created_at").toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.createdBy = rs.getInt("created_by");
        this.effectiveAt = rs.getObject("effective_at") != null ? rs.getTimestamp("effective_at").toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : null;
        this.state = rs.getString("state");
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
        Category category = getCategoryTransfer(accountID, transferID);
        return category;
    }


    public static List<Transfer> getTransfers(Account account) {
        return queryList(Transfer.class, "select * from transfer where (source_account=:source_account or target_account=:target_account) ORDER by GREATEST( COALESCE(effective_at, 0), COALESCE(created_at, 0) )DESC", new Params()
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
}


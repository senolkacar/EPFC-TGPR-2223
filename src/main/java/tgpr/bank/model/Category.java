package tgpr.bank.model;
import tgpr.framework.Model;
import tgpr.framework.Params;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Category extends Model {
    private static Integer idAccount;
    public enum Fields {
        name, id
    }
    private int id;
    private boolean type;
    private Integer account;


    private String name;
//    private static final Integer idAccount = Account.getById(Security.getLoggedUser().getId()).getId();

    public int getId() {
        return id;
    }

    public Category(String name, boolean account) {
        this.name = name;
        this.type = account;
    }

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category(int id, String name, boolean account) {
        this.id = id;
        this.name = name;
        this.type = account;
    }

    public boolean isSystem() {
        return type;
    }

    public void setAccount(boolean account) {
        this.type = account;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Integer getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }
//    public static List<Category> getAll(){
//        return queryList(Category.class,"select * from Category where account is null or account=:idAccount",new Params("idAccount",idAccount));
//    }

    public static List<Category> getByAccount(int account) {
        return queryList(Category.class, "select * from category where account is null ||  account=:account", new Params("account", account));
    }

    public static List<String> getCategoryNames(List<Category> listCat){
        List<String> listToReturn = new ArrayList<>();
        for(int i = 0; i<listCat.size();++i){
            listToReturn.add(listCat.get(i).getName());
        }
        return listToReturn;
    }

    public static List<String> getCategoryNames(Integer selectedAccountID){
        return getCategoryNames(getByAccount(selectedAccountID));
    }


    // SELECT COUNT(category.id) FROM category,transfer_category WHERE transfer_category.account=1 AND transfer_category.category=category.id GROUP by category.id

    @Override
    protected void mapper(ResultSet rs) throws SQLException {
        name = rs.getString("name");
        type = rs.getBoolean("account");
        id = rs.getInt("id");
        idAccount = rs.getInt("account");
    }

    public void delete(Category category) {
        String sql ="delete from category where account is not null && category.id=:categoryID";
        execute(sql, new Params()
                .add("name", name)
                .add("categoryID", category.getId()));

    }
    public void update(String name,Category category) {
        String sql = "update category set name=:name where category.id=:categoryID";
        execute(sql, new Params()
                .add("name", name)
                .add("categoryID", category.getId()));
    }

    public String toString() {
        return this.name;
    }

    @Override
    public void reload() {
        
    }

    public static Category getCatByName(String categoryName,Integer account){
        return queryOne(Category.class, "select * from category where (account is null ||  account=:account) and category.name=:name", new Params("account", account).add("name",categoryName));
    }
    public static void addTransferToTransferCat(Integer catId, Integer transferId, Integer accountId){
        execute("insert into transfer_category(category,transfer,account) values(:cat,:transfer,:account)",new Params().add("cat",catId).add("transfer",transferId).add("account",accountId));
    }
    public static Category getByAccount(int idAccount, String name) {
        return queryOne(Category.class, "select * from category where   account=:account and name=:name", new Params()
                .add("account", idAccount)
                .add("name", name));
    }
    public static List<Category> getCategories(Account account) {
        return queryList(Category.class, "select * from category where account=:account  or account is null   order by name                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   ", new Params()
                .add("account", account.getId())
        );
    }

    public static List<Category> getUsesCategory(Account account,Category category) {
        String sql = "SELECT * FROM category join transfer_category on category.id = transfer_category.category where transfer_category.account=:accountID and transfer_category.category=:categoryID";
        return queryList(Category.class, sql, new Params()
                .add("accountID", account.getId())
                .add("categoryID", category.getId())
        );
    }
}


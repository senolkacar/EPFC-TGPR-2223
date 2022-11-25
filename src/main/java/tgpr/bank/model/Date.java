package tgpr.bank.model;

import tgpr.framework.Model;
import tgpr.framework.Params;
import tgpr.framework.Tools;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Date extends Model{


    private LocalDateTime systemDate;

    public Date() {
    }
    public static  LocalDateTime getSysDateParsed(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return LocalDateTime.parse(date, formatter);
    }

    public static LocalDateTime changeFormatToEn(LocalDateTime date) {
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(date);
        return date;
    }

    public static LocalDateTime changeFormatToEu(LocalDateTime date) {
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH).format(date);
        return date;
    }
    public static String getStateOfBTTF(){
        if(DateInterface.isHasChanged()){
            return DateInterface.getUsedDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        }
        else {
            return "use system/date time";
        }
    }

    public void reload(){
        String sql = "SELECT * FROM global where 1";
        reload(sql,new Params());
    }
    public static Date getSysDate(){
        String sql = "SELECT * FROM global where 1";
        return queryOne(Date.class, sql,new Params());
    }

    public static Date changeDateOnDB(LocalDateTime customeDate){
        String sql = "UPDATE global SET system_date=:customDate";
        return queryOne(Date.class, sql,new Params("customDate",customeDate));
    }

    protected void mapper(ResultSet rs) throws SQLException {
        this.systemDate = rs.getObject("system_date", LocalDateTime.class);
    }
        public LocalDateTime getSystemDate(){
        return systemDate;
    }

    public String toString() {
        return Tools.toString(getSystemDate());
    }

}

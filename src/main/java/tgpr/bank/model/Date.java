package tgpr.bank.model;

import tgpr.framework.Model;
import tgpr.framework.Params;
import tgpr.framework.Tools;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class Date extends Model{
    private LocalDateTime systemDate;

    public void reload(){
        String sql = "SELECT * FROM global where 1";
        reload(sql,new Params());
    }
    public static Date getSysDate(){
        String sql = "SELECT * FROM global where 1";
        return queryOne(Date.class, sql,new Params());
    }
    protected void mapper(ResultSet rs) throws SQLException {
        this.systemDate = rs.getObject("system_date", LocalDateTime.class);
    }

        public LocalDateTime getSystemDate(){
        return systemDate;
    }

    public String toString() {
        return Tools.toString(systemDate);
    }

}

package org.restserver;
import java.sql.SQLException;

public class PowerTrafo {
    DbConnect myConn = new DbConnect();
    FuncsAndProcs fps = new FuncsAndProcs();
    
    public String savePowerTrafoLayout(String ipAddress, Integer value) throws SQLException {
        myConn.connect(0); 
        String layout = myConn.fetchSql("select layout from tb980_powertrafo_layout where ipAddress = ?", ipAddress, "layout");
        return layout;
    }
    
}

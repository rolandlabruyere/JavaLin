package org.restserver;
import java.sql.SQLException;

public class PowerTrafo  {
    DbConnect myConn = new DbConnect();
    FuncsAndProcs fps = new FuncsAndProcs();
    ConstructHtmlPages chp = new ConstructHtmlPages();
    /*
        0 = connect to the customer sales database
        1 = connect to the html pages database
    */
    
    public String powerTrafoLayout(String tabItem, String ipAddress, Integer value) throws SQLException {
        myConn.connect(0); 
        myConn.execSql("insert into tb910_temp_trafo_settings values (?, ?, ?, ?, ?)", ipAddress + ";1;" + tabItem + ";" + value.toString() + ";" + fps.depositTimestamp(0));
        return chp.constructTrafoLayoutPage(tabItem, value);
    }
    
}

package org.restserver;
import java.sql.SQLException;
import static org.restserver.FuncsAndProcs.decodeBase64;


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

    public String postPowerTrafoSpecs(String tabItem, String ipAddress, String valueString) throws SQLException {
        DbConnect conn = new DbConnect();
        conn.connect(0); 
        String decodedValues = decodeBase64(valueString);
        String[] value = decodedValues.split("&");

        //initialize a new power trafo for this ip 
        conn.execSql("insert into voorthuiscustomersales.tb200_power_trafo_config (ip, trafoNum, timestamp) values (?, ?, ?)", ipAddress + ";" + getNextNumber(tabItem) + ";" + fps.depositTimestamp(0));
        return "done";
        }




    public String getNextNumber(String tabItem) throws SQLException {
        myConn.connect(1); 
        String dateString = fps.depositTimestamp(0);
        String mYear = dateString.substring(0, 4);
        String mMonth = dateString.substring(5, 7);

        String trafoNumber = myConn.fetchSql("select * from tb900_numberstabel where itemtype = ?", tabItem, "itemNumber");
        String formattedTrafoNumber =  mYear + mMonth + fps.formatNumber(Integer.parseInt(trafoNumber));

        myConn.execSql("delete from tb900_numberstabel where itemType = ?", tabItem);
        myConn.execSql("insert into tb900_numberstabel values (?, ?, ?, ?)", tabItem + ";" + mYear + ";" + mMonth + ";" + (Integer.parseInt(trafoNumber) + 1));

        return formattedTrafoNumber;
    }
    
}

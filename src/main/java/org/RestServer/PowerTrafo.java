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
        String[] values = decodedValues.split("&");
        String trafoNumber = getNextNumber(tabItem);
        Integer layOutValue = Integer.valueOf(conn.fetchSql("select * from voorthuiscustomersales.tb910_temp_trafo_settings where ip = ? and part = ?", ipAddress + ";1", "CommonValues"));

        //initialize a new power trafo for this ip 
        conn.execSql("insert into voorthuiscustomersales.tb200_power_trafo_config (ip, trafoNum, timestamp) values (?, ?, ?)", ipAddress + ";" + trafoNumber + ";" + fps.depositTimestamp(0));

        //first insert the boolean values into the database
        for (Integer i = 0; i < 7; i++) {
            switch (layOutValue & (int)Math.pow(2, i)) {
                case 1  -> conn.execSql("update voorthuiscustomersales.tb200_power_trafo_config set secundary = true where ip = ? and trafoNum = ?", ipAddress + ";" + trafoNumber);
                case 2  -> conn.execSql("update voorthuiscustomersales.tb200_power_trafo_config set centertap = true where ip = ? and trafoNum = ?", ipAddress + ";" + trafoNumber);
                case 32 -> conn.execSql("update voorthuiscustomersales.tb200_power_trafo_config set filamentCenterTap = true where ip = ? and trafoNum = ?", ipAddress + ";" + trafoNumber);
                case 64 -> conn.execSql("update voorthuiscustomersales.tb200_power_trafo_config set tapFiftyVolt = true where ip = ? and trafoNum = ?", ipAddress + ";" + trafoNumber);
            };
        }

        //then insert the other values into the database
        for (String value : values) {
            String[] keyValue = value.split("=");
            conn.execSql("update voorthuiscustomersales.tb200_power_trafo_config set " + keyValue[0] + " = " + keyValue[1] + " where ip = ? and trafoNum = ?", ipAddress + ";" + trafoNumber);
        }

        //remove the temporary settings for this ip address
        conn.execSql("delete from voorthuiscustomersales.tb910_temp_trafo_settings where ip = ? and part = ?", ipAddress + ";1");
        return "/now finalizing calculations";
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

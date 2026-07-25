package org.restserver;
import java.math.BigDecimal;
import java.sql.SQLException;
import static org.restserver.FuncsAndProcs.decodeBase64;

public class PowerTrafo  {
    FuncsAndProcs fps = new FuncsAndProcs();
    ConstructHtmlPages chp = new ConstructHtmlPages();
    /*
        0 = connect to the customer sales database
        1 = connect to the html pages database
    */
    
    public String powerTrafoLayout(String tabItem, String ipAddress, Integer value) throws SQLException {
        DbConnect myConn = new DbConnect();
        myConn.connect(0); 
        myConn.execSql("insert into voorthuiscustomersales.tb910_temp_trafo_settings values (?, ?, ?, ?, ?)", ipAddress + ";1;" + tabItem + ";" + value.toString() + ";" + fps.depositTimestamp(0));
        return chp.constructTrafoLayoutPage(tabItem, value);
    }

    public String postPowerTrafoSpecs(String tabItem, String ipAddress, String valueString) throws SQLException {
        DbConnect conn = new DbConnect();
        conn.connect(0); 
        String decodedValues = decodeBase64(valueString);
        String[] values = decodedValues.split("&");
        String trafoNumber = getNextNumber(tabItem);
        Integer layOutValue = Integer.valueOf(conn.fetchSql("select * from voorthuiscustomersales.tb910_temp_trafo_settings where ip = ? and part = ?", ipAddress + ";1", "CommonValues"));

        //check for grid settings on this ip, otherwise create them
        checkGridEntry(ipAddress);

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

        //double voltage to adjust to "Fender style" full bridge rectifier
        conn.execSql("update voorthuiscustomersales.tb200_power_trafo_config set volts = volts * 2 where ip = ? and trafoNum = ? and centerTap = true ", ipAddress + ";" + trafoNumber);

        //remove the temporary settings for this ip address
        conn.execSql("delete from voorthuiscustomersales.tb910_temp_trafo_settings where ip = ? and part = ?", ipAddress + ";1");
        return calcPowerTrafo(tabItem, ipAddress, trafoNumber);
    }


    /*************************************************************************************************
     *    private functions
     * ***********************************************************************************************/

    // calculate the actual trafo
    private String calcPowerTrafo(String tabItem, String ipAdress, String trafoNumber) throws SQLException{
        DbConnect conn = new DbConnect();
        conn.connect(0); 

        //the main html page is containing several placeholders, which are going to be replaced by this function 
        String mainHtml = conn.fetchSql("select * from voorthuishtmlpages.tb100_htmlpaginas where id = ?", "calculatedTrafoSpecs" , "InlineHtml" );

        //iniatilize the key variables
        String placeHoldersAll = getPlaceholders(tabItem);
        String rowHideBoolsAll   = getPlaceholders(tabItem + "_bools");
        String[] placeHolders = placeHoldersAll.split("|");
        String[] rowHideBools = rowHideBoolsAll.split("|");
        String[] trafoValues = conn.fetchSql("select * from voorthuiscustomersales.vw205_power_trafo_all where ip = ? and trafoNum = ?", ipAdress + ";" + trafoNumber); 

        Float secVoltage    = Float.parseFloat(trafoValues[3]);
        Float secMilliAmps  = Float.parseFloat(trafoValues[4]);
        int   secCenterTap  = Integer.parseInt(trafoValues[5]);
        int   tapFiftyVolt  = Integer.parseInt(trafoValues[6]);
        Float filFiveAmps   = Float.parseFloat(trafoValues[7]);
        Float filSixAmps    = Float.parseFloat(trafoValues[8]);
        Float filTwelveAmps = Float.parseFloat(trafoValues[9]);
        int   filCenterTap  = Integer.parseInt(trafoValues[10]);
        Float primVoltage   = Float.parseFloat(trafoValues[11]);
        Float primFreq      = Float.parseFloat(trafoValues[12]);

        Float primaryVa = getSumVASecundary(ipAdress, trafoNumber);
        Float primaryAmps = primaryVa / primVoltage;
        Float coreArea = (float) Math.sqrt(primaryVa) * 1.15f;
        Float turnsPerVolt = primFreq/coreArea;  
        Float primWireSize = getWireSize(primaryVA, primVoltage);


        return primFreq.toString();
    }

    private String getNextNumber(String tabItem) throws SQLException {
        DbConnect myConn = new DbConnect();
        myConn.connect(1); 
        String dateString = fps.depositTimestamp(0);
        String mYear = dateString.substring(0, 4);
        String mMonth = dateString.substring(5, 7);

        String trafoNumber = myConn.fetchSql("select * from voorthuishtmlpages.tb900_numberstabel where itemtype = ?", tabItem, "itemNumber");
        String formattedTrafoNumber =  mYear + mMonth + fps.formatNumber(Integer.parseInt(trafoNumber));

        myConn.execSql("delete from tb900_numberstabel where itemType = ?", tabItem);
        myConn.execSql("insert into tb900_numberstabel values (?, ?, ?, ?)", tabItem + ";" + mYear + ";" + mMonth + ";" + (Integer.parseInt(trafoNumber) + 1));

        return formattedTrafoNumber;
    }

    private String getPlaceholders(String searchItem) throws SQLException {
        DbConnect myConn = new DbConnect();
        myConn.connect(1); 
        return myConn.fetchSql("select * from voorthuishtmlpages.tb910_placeholders where functionName = ?", searchItem, "placeHolderString");
    }

    private void checkGridEntry(String myIp) throws SQLException{
        DbConnect myConn = new DbConnect();
        myConn.connect(0);

        try{ 
            myConn.fetchSql("select * from voorthuiscustomersales.tb930_grid_settings_per_ip where Ip = ?", myIp);
        } catch (Exception e){
            myConn.execSql("insert into voorthuiscustomersales.tb930_grid_settings_per_ip values(?, ?, ?, ?)", myIp + ";230;50;" + fps.depositTimestamp(0));
        }
    }

    private Float getSumVASecundary(String myIp, String trafoNum) throws SQLException{
        DbConnect myConn = new DbConnect();
        myConn.connect(0);

        String[] result = myConn.fetchSql("select * from vw205_power_trafo_all where ip = ? and trafoNum = ?", myIp + ";" + trafoNum);
        Float primVa = Float.parseFloat(result[3]) * Float.parseFloat(result[4]) / 1000; 
              primVa = primVa + Float.parseFloat(result[7]) * 5; 
              primVa = primVa + Float.parseFloat(result[8]) * 6.3f;
              primVa = primVa + Float.parseFloat(result[9]) * 12.8f;
              primVa = primVa/0.9f; 

        return primVa;
    }

    private float getWireSize(Float primaryVA, Float primVoltage){

        return 0.00f;
    };

    /*
      function getWireSize(primVa, primVoltage: single): single; overload;
  var
    thisQuery: tAdoQuery;
    current: single;
  begin
    thisQuery := tAdoQuery.Create(nil);
    current := primVA / primVoltage;
    result := 0;

    with thisQuery do begin
      connection := form1.adoConnHtmlPages;
      SQL.add('select min(diameter) from tb230_draad_metrisch where MaxAmp >= :amperage');
      Parameters.ParamByName('amperage').Value := current;
      try
        Open;
        Result := fields[0].AsFloat;
      except
        on E:exception do writelog(E.Message);
      end;
    end;
  end;

  function getWireSize(secAmps: single; isMilliAmps: boolean): single; overload;
  var
    thisQuery: tAdoQuery;
    current: single;
  begin
    thisQuery := tAdoQuery.Create(nil);
    result := 0;

    if isMilliAmps then
      current := secAmps / 1000
    else
      current := secAmps;

    with thisQuery do begin
      connection := form1.adoConnHtmlPages;
      SQL.add('select min(diameter) from tb230_draad_metrisch where MaxAmp >= :amperage');
      Parameters.ParamByName('amperage').Value := current;
      try
        Open;
        Result := fields[0].AsFloat;
      except
        on E:exception do writelog(E.Message);
      end;
    end;
  end;

    */
    
}

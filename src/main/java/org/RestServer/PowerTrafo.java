package org.restserver;
import java.sql.SQLException;
import static org.restserver.FuncsAndProcs.decodeBase64;
import static org.restserver.FuncsAndProcs.encodeBase64;

public class PowerTrafo  {
    FuncsAndProcs fps = new FuncsAndProcs();
    ConstructHtmlPages chp = new ConstructHtmlPages();
    static final Float filamentFiveVolts = 5f;
    static final Float filamentSixVolts = 6.3f;
    static final Float filamentTwelveVolts = 12.6f;

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
        String[]itemValues = new String[27];
        conn.connect(0); 

        //the main html page is containing a set of placeholders, which is going to be replaced by this function 
        String exportHtml = conn.fetchSql("select * from voorthuishtmlpages.tb100_htmlpaginas where id = ?", "calculatedTrafoSpecs" , "InlineHtml" );

        //iniatilize the key variables
        String placeHoldersAll = getPlaceholders(tabItem);
        String rowHideBoolsAll   = getPlaceholders(tabItem + "_bools");
        String[] placeHolders = placeHoldersAll.split(";");
        String[] rowHideBools = rowHideBoolsAll.split(";");

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

        Float primaryVA = getSumVASecundary(ipAdress, trafoNumber);
        Float primaryAmps = primaryVA / primVoltage;
        Float coreArea = (float) Math.sqrt(primaryVA) * 1.15f;
        Float turnsPerVolt = primFreq/coreArea;  
        Float primWireSize = getWireSize(primaryVA, primVoltage);
        Float primaryTurns = turnsPerVolt * primVoltage;
        Float secundaryTurns = turnsPerVolt * secVoltage;
        Float fiftyVoltTapTurns = turnsPerVolt * 50;
        Float secCenterTapTurns = secundaryTurns / 2;
        Float secWireSize = getWireSize(secMilliAmps, true);
        Float filFiveTurns = turnsPerVolt * filamentFiveVolts;
        Float filFiveCtTurns = filFiveTurns / 2;
        Float filSixTurns = turnsPerVolt * filamentSixVolts;
        Float filSixCtTurns = filSixTurns / 2;
        Float filTwelveTurns = turnsPerVolt * filamentTwelveVolts;
        Float filTwelveCtTurns = filTwelveTurns / 2;
        Float filFiveWireSize = getWireSize(filFiveAmps, false);
        Float filSixWireSize = getWireSize(filSixAmps, false);
        Float filTwelveWireSize = getWireSize(filTwelveAmps, false);
        Float primTurnArea = calcTurnArea(primWireSize, primaryTurns);
        Float secTurnArea = calcTurnArea(secWireSize, secundaryTurns);
        Float fiveVoltTurnArea = calcTurnArea(filFiveWireSize, filFiveTurns);
        Float sixVoltTurnArea = calcTurnArea(filSixWireSize, filSixTurns);
        Float twelveVoltTurnArea = calcTurnArea(filTwelveWireSize, filTwelveTurns);

        //vervang de placeholders door de berekende waarden
        itemValues[0]  = String.format("%.0f", primaryVA).trim();
        itemValues[1]  = String.format("%.2f", primaryAmps).trim();
        itemValues[2]  = String.format("%.2f", coreArea).trim();
        itemValues[3]  = String.format("%.2f", turnsPerVolt).trim();
        itemValues[4]  = String.format("%.2f", primWireSize).trim();
        itemValues[5]  = String.format("%.0f", primaryTurns).trim();
        itemValues[6]  = String.format("%.0f", secundaryTurns).trim();
        itemValues[7]  = String.format("%.0f", fiftyVoltTapTurns).trim();
        itemValues[8]  = String.format("%.0f", secCenterTapTurns).trim();
        itemValues[9]  = String.format("%.2f", secWireSize).trim();
        itemValues[10] = String.format("%.0f", filFiveTurns).trim();
        itemValues[11] = String.format("%.0f", filFiveCtTurns).trim();
        itemValues[12] = String.format("%.2f", filFiveWireSize).trim();
        itemValues[13] = String.format("%.0f", filSixTurns).trim();
        itemValues[14] = String.format("%.0f", filSixCtTurns).trim();
        itemValues[15] = String.format("%.2f", filSixWireSize).trim();
        itemValues[16] = String.format("%.0f", filTwelveTurns).trim();
        itemValues[17] = String.format("%.0f", filTwelveCtTurns).trim();
        itemValues[18] = String.format("%.2f", filTwelveWireSize).trim();
        itemValues[19] = String.format("%.2f", primTurnArea).trim();
        itemValues[20] = String.format("%.2f", secTurnArea).trim();
        itemValues[21] = String.format("%.2f", fiveVoltTurnArea).trim();
        itemValues[22] = String.format("%.2f", sixVoltTurnArea).trim();
        itemValues[23] = String.format("%.2f", twelveVoltTurnArea).trim();
        itemValues[24] = String.format("%.2f", primTurnArea + secTurnArea + fiveVoltTurnArea + sixVoltTurnArea + twelveVoltTurnArea).trim();
        itemValues[25] = getSuitableEiType(primTurnArea + secTurnArea + fiveVoltTurnArea + sixVoltTurnArea + twelveVoltTurnArea);
        itemValues[26] = trafoNumber;

        for (int t = 0; t < placeHolders.length; t++){
          exportHtml = exportHtml.replace(placeHolders[t], itemValues[t]);
        }

        if (tapFiftyVolt == 0) {
          exportHtml = exportHtml.replace(rowHideBools[0],"hidden");
          itemValues[7] =  "0";
         } else {exportHtml = exportHtml.replace(rowHideBools[0], "");}

        if (secCenterTap == 0) {
          exportHtml = exportHtml.replace(rowHideBools[1],"hidden");
          itemValues[8] =  "0";
         } else {exportHtml = exportHtml.replace(rowHideBools[1], "");}

        if (filCenterTap == 0) {
          exportHtml = exportHtml.replace(rowHideBools[2],"hidden");
          itemValues[11] =  "0";
          itemValues[14] =  "0";
          itemValues[17] =  "0";
         } else {exportHtml = exportHtml.replace(rowHideBools[2], "");}

        if (filFiveWireSize == 0) {
          exportHtml = exportHtml.replace(rowHideBools[3],"hidden");
          itemValues[10] =  "0";
          itemValues[11] =  "0";
          itemValues[12] =  "0.00";
         } else {exportHtml = exportHtml.replace(rowHideBools[3], "");}

        if (filSixWireSize == 0) {
          exportHtml = exportHtml.replace(rowHideBools[4],"hidden");
          itemValues[13] =  "0";
          itemValues[14] =  "0";
          itemValues[15] =  "0.00";
         } else {exportHtml = exportHtml.replace(rowHideBools[4], "");}

        if (filTwelveWireSize == 0) {
          exportHtml = exportHtml.replace(rowHideBools[5],"hidden");
          itemValues[16] =  "0";
          itemValues[17] =  "0";
          itemValues[18] =  "0.00";
         } else {exportHtml = exportHtml.replace(rowHideBools[5], "");}

        // For replacement of the placeholders, itemValues[26] should be the trafonumber. 
        // However for the insert query itemValues[26] is the timestamp, so here I replace them 
        itemValues[26] = fps.depositTimestamp(0);

         if (saveCalculatedTrafoSpecs(ipAdress, trafoNumber, itemValues)) {
            saveHtmlDoc(ipAdress, trafoNumber, exportHtml.replace("$hideButton$", "hidden"));
         //   saveHtmlDoc(ipAdress, trafoNumber, exportHtml);
            return exportHtml;
        } else {
            return "failed to save power trafo";
        }
    }

    private String getNextNumber(String tabItem) throws SQLException {
        DbConnect myConn = new DbConnect();
        myConn.connect(1); 
        String dateString = fps.depositTimestamp(0);
        String mYear = dateString.substring(0, 4);
        String mMonth = dateString.substring(5, 7);

        String trafoNumber = myConn.fetchSql("select * from voorthuishtmlpages.tb900_numberstabel where itemtype = ?", tabItem, "itemNumber");
        String formattedTrafoNumber =  mYear + mMonth + fps.formatNumber(Integer.parseInt(trafoNumber));

        myConn.execSql("replace into voorthuishtmlpages.tb900_numberstabel values (?, ?, ?, ?)", tabItem + ";" + mYear + ";" + mMonth + ";" + (Integer.parseInt(trafoNumber) + 1));

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

        String[] result = myConn.fetchSql("select * from voorthuiscustomersales.vw205_power_trafo_all where ip = ? and trafoNum = ?", myIp + ";" + trafoNum);
        Float primVa = Float.parseFloat(result[3]) * Float.parseFloat(result[4]) / 1000; 
              primVa = primVa + Float.parseFloat(result[7]) * 5; 
              primVa = primVa + Float.parseFloat(result[8]) * 6.3f;
              primVa = primVa + Float.parseFloat(result[9]) * 12.8f;
              primVa = primVa/0.9f; 

        return primVa;
    }

    private float getWireSize(Float power, Float voltage) throws SQLException {
        DbConnect myConn = new DbConnect();
        myConn.connect(1);
        Float current = power /voltage; 

        String result = myConn.fetchSql("select min(diameter) as wireSize from voorthuishtmlpages.tb230_draad_metrisch where MaxAmp >= ?", current.toString(), "wireSize");
        return Float.parseFloat(result);
    };

    private float getWireSize(Float secAmps, Boolean isMilliAmps) throws SQLException {
        DbConnect myConn = new DbConnect();
        Float current = 0f;
        myConn.connect(1);

        if (isMilliAmps) {
          current = secAmps / 1000;
        } else {
          current = secAmps;
        }

        String result = myConn.fetchSql("select min(diameter) as wireSize from voorthuishtmlpages.tb230_draad_metrisch where MaxAmp >= ?", current.toString(), "wireSize");
        return Float.parseFloat(result);
    };

    private Float calcTurnArea(Float diam , Float turns){
      float Fv = 0.25f ;
      return ((turns * (float)Math.pow(diam, 2 )) / Fv) / 100f;
    }

    private String getSuitableEiType(Float calculatedWindowsArea) throws SQLException {
        DbConnect myConn = new DbConnect();
        myConn.connect(1);
        try{
           return myConn.fetchSql("select min(OppervlakVensters), TypeAanduiding from voorthuishtmlpages.tb250_trafoblik where OppervlakVensters >= ?", calculatedWindowsArea.toString(), "TypeAanduiding");
        } catch(Exception e){
           throw new RuntimeException("no suitable sheets found for these values");
        }
    }

    private Boolean saveCalculatedTrafoSpecs(String ipAddress, String trafoNumber, String[] allValues) throws SQLException {
        boolean result = false;
        String valueString = ipAddress + ";" + trafoNumber + ";";
        DbConnect myConn = new DbConnect();
        myConn.connect(0);

        for (String value: allValues){
            valueString += value + ";"; 
        }

        try{
            myConn.execSql("replace into voorthuiscustomersales.tb210_power_trafo_calcspecs values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", valueString);
            result = true;
        } catch(Exception e){result = false;}
        
        return result;
    }

    private void saveHtmlDoc(String ipAddress, String trafoNumber, String htmlCode) throws SQLException {
        DbConnect myConn = new DbConnect();
        myConn.connect(0);
        String decodedHtml = encodeBase64(htmlCode);

        myConn.execSql("replace into voorthuiscustomersales.tb940_save_html_doc values (?, ?, ?, ?)", ipAddress + ";" + trafoNumber + ";" + decodedHtml + ";" + fps.depositTimestamp(0));
    }
}

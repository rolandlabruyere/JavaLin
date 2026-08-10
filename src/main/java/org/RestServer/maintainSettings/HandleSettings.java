package org.restserver.maintainSettings;

import java.sql.SQLException;
import static org.restserver.common.FuncsAndProcs.decodeBase64;

import org.restserver.common.FuncsAndProcs;
import org.restserver.database.DbConnect;
import org.restserver.htmlConstructor.ConstructHtmlPages;

public class HandleSettings {
    ConstructHtmlPages chp = new ConstructHtmlPages();
    FuncsAndProcs fps = new FuncsAndProcs();

    public String getSettings(String tabItem, String ipAddress) throws SQLException {
        String query1 = "select count(*) as number from voorthuiscustomersales.vw925_customerstats where ip = ?";
        String query2 = "select * from voorthuiscustomersales.vw925_customerstats where ip = ?";
        String result = "";
        DbConnect conn = new DbConnect();
        conn.connect();
        if (conn.fetchSql(query1, ipAddress, "number").equals("0")){
            result = chp.getHtmlPage(tabItem);
        } else {
            result = constructSettingsPage(chp.getHtmlPage(tabItem + "Saved"), chp.getPlaceholders(tabItem + "Saved"), conn.fetchSql(query2, ipAddress));
        }
        return result;
    }

    public String updateSettings(String tabItem, String ipAddress, String valuePairs) throws SQLException {
        DbConnect conn = new DbConnect();
        conn.connect(); 
        String[] values = decodeBase64(valuePairs).split("&");
        for (int t = 0; t < values.length - 1; t++){
            switch (t){
                case 0, 1 -> {
                    String[] pair = values[t].split("=");
                    conn.execSql("update voorthuiscustomersales.tb930_grid_settings_per_ip set " + pair[0] + " = " + pair[1] + ", timestamp = \"" + fps.depositTimestamp(0) + "\" where Ip = ?", ipAddress); 
                }
                default -> {
                    String[] pair = values[t].split("=");
                    conn.execSql("update voorthuiscustomersales.tb110_address set " + pair[0] + " = \"" + pair[1] + "\", timestamp = \"" + fps.depositTimestamp(0) + "\" where Ip = ?", ipAddress); 
                }
            }
        }
        String[] pair = values[values.length - 1].split("=");
        System.out.println(pair[1]);

        return "update gelukt";
    }
    
    private String constructSettingsPage(String htmlPage, String placeholders, String[] values) throws SQLException {
        String[] placeHolders = placeholders.split(";");

        for (int t = 0; t < placeHolders.length; t++){
            htmlPage = htmlPage.replace(placeHolders[t], values[t + 1]);
        }
        return htmlPage;
    }
}

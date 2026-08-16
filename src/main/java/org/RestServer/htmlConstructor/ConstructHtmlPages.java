package org.restserver.htmlConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.restserver.common.FuncsAndProcs;
import org.restserver.database.DbConnect;
import java.sql.ResultSetMetaData;

public class ConstructHtmlPages {
    FuncsAndProcs fps = new FuncsAndProcs();

    public String getHtmlPage(String tabItem) throws SQLException {
        DbConnect myConn = new DbConnect();
        myConn.connect(); 
        String htmlPage = myConn.fetchSql("select * from voorthuishtmlpages.tb100_htmlpaginas where id = ?", tabItem, "InlineHtml");

        return htmlPage;
    }

    public String getPlaceholders(String searchItem) throws SQLException {
        DbConnect myConn = new DbConnect();
        myConn.connect(); 
        return myConn.fetchSql("select * from voorthuishtmlpages.tb910_placeholders where functionName = ?", searchItem, "placeHolderString");
    }

    public String constructTrafoLayoutPage(String tabItem, Integer layOutValue) throws SQLException {
        DbConnect myConn = new DbConnect();
        myConn.connect(); 
        String htmlString = myConn.fetchSql("select * from voorthuishtmlpages.tb120_html_snippets where id = ? and itemNr = 0", tabItem, "HtmlCode");

        for (Integer i = 0; i < 7; i++) {
            Integer bitValue = (int)Math.pow(2, i);
            if((layOutValue & bitValue) == bitValue) {
                htmlString = htmlString.replace("$snippet"+ bitValue +"$", getSnippet(tabItem, bitValue));
            } else {
                htmlString = htmlString.replace("$snippet"+ bitValue +"$", "");
            }
        }
        return htmlString;
    }

    public String constructOpenOrderInfo(String tabItem, String ipAddress) throws SQLException {
        DbConnect myConn = new DbConnect();
        myConn.connect(); 

        String mainInfo = getSnippet(tabItem, 0);
        String snippet = getSnippet(tabItem, 1);
        String placeHolder = getPlaceholders(tabItem);

        ResultSet openOrders = myConn.openSql("select trafoNum, orderType from voorthuiscustomersales.tb970_active_orders where ip = ? and isOpen = false order by 1, 2" , ipAddress);
        ResultSetMetaData rsmd = openOrders.getMetaData();
        String column1 = "$"+ rsmd.getColumnName(1) + "$";
        String column2 = "$"+ rsmd.getColumnName(2) + "$";

        while (openOrders.next()){
            String hulp = snippet.replace(column1, openOrders.getString(1));
            hulp = hulp.replace(column2, openOrders.getString(2)); 
            mainInfo = mainInfo.replace(placeHolder, hulp);
        }
        mainInfo = mainInfo.replace(placeHolder, "");
        return mainInfo;
    }

    public String getHistTrafo(String ipAddress, String trafoNumber) throws SQLException {
        DbConnect myConn = new DbConnect();
        myConn.connect();
        String result = myConn.fetchSql("select * from voorthuiscustomersales.tb940_save_html_doc where ip = ? and trafoNum = ?", ipAddress + ";" + trafoNumber, "htmlDoc"); 

        return fps.decode_Base64(result);
    }

    private String getSnippet(String tabItem, Integer itemNr) throws SQLException {
        DbConnect myConn = new DbConnect();
        myConn.connect(); 
        return myConn.fetchSql("select * from voorthuishtmlpages.tb120_html_snippets where id = ? and itemNr = ?", tabItem + ";" + itemNr.toString(), "HtmlCode");
    }
}

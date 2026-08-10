package org.restserver.htmlConstructor;

import java.sql.SQLException;

import org.restserver.common.FuncsAndProcs;
import org.restserver.database.DbConnect;

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

    private String getSnippet(String tabItem, Integer itemNr) throws SQLException {
        DbConnect myConn = new DbConnect();
        myConn.connect(); 
        return myConn.fetchSql("select * from voorthuishtmlpages.tb120_html_snippets where id = ? and itemNr = ?", tabItem + ";" + itemNr.toString(), "HtmlCode");
    }
}

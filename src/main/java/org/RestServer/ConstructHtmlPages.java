package org.restserver;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.*;

public class ConstructHtmlPages {
    DbConnect myConn = new DbConnect();
    FuncsAndProcs fps = new FuncsAndProcs();

    public String getHtmlPage(String tabItem) throws SQLException {
        //String[] placeholders;
        myConn.connect(1); 
        ResultSet rs = myConn.openSql("select * from tb100_htmlpaginas where id = ?", tabItem);

        rs.next();
        String htmlPage = rs.getString("InlineHtml");

        //htmlPage = setActivePage(htmlPage, tabItem);
        //fps.writeToLog(htmlPage);
        myConn.close();
        return htmlPage;
    }
    public String constructTrafoLayoutPage(String tabItem, Integer layOutValue) throws SQLException {
        myConn.connect(1); 
        String htmlString = myConn.fetchSql("select * from tb120_html_snippets where id = ? and itemNr = 0", tabItem, "HtmlCode");

        for (Integer i = 0; i < 7; i++) {
            Integer bitValue = (int)Math.pow(2, i);
            if((layOutValue & bitValue) == bitValue) {
                htmlString = htmlString.replace("$snippet"+ bitValue +"$", getSnippet(tabItem, bitValue));
            } else {
                htmlString = htmlString.replace("$snippet"+ bitValue +"$", "");
            }
        }

        myConn.close();
        return htmlString;
    }

    private String getSnippet(String tabItem, Integer itemNr) throws SQLException {
        return myConn.fetchSql("select * from tb120_html_snippets where id = ? and itemNr = ?", tabItem + ";" + itemNr.toString(), "HtmlCode");
    }

}

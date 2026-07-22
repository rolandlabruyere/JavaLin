package org.restserver;

import java.sql.ResultSet;
import java.sql.SQLException;

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
    public String constructTrafoLayoutPage(String tabItem, int layOutValue) throws SQLException {
        myConn.connect(1); 
        String htmlString = myConn.fetchSql("select * from tb120_html_snippets where id = ?", tabItem, "HtmlCode");

        myConn.close();
        return htmlString;
    }

}

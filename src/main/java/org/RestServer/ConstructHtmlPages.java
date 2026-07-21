package org.restserver;

import java.sql.ResultSet;

public class ConstructHtmlPages {
    DbConnect myConn = new DbConnect();
    FuncsAndProcs fps = new FuncsAndProcs();

    public String getHtmlPage(String tabItem) throws Throwable {
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

}

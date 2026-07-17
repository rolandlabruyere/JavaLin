package org.RestServer;

import java.sql.ResultSet;
//import java.sql.SQLException;

public class ConstructHtmlPages {
    DbconHpages myConn = new DbconHpages();
    FuncsAndProcs fps = new FuncsAndProcs();

    public String getHtmlPage(String tabItem) throws Throwable {
        //String[] placeholders;
        myConn.connect();
        ResultSet rs = myConn.openSql("select * from tb100_htmlpaginas where id = ?", tabItem);

        rs.next();
        String htmlPage = rs.getString("InlineHtml");

        //htmlPage = setActivePage(htmlPage, tabItem);
        //fps.writeToLog(htmlPage);
        myConn.close();
        return htmlPage;
    }

}

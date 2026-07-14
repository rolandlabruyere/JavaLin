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
        fps.writeToLog(htmlPage);
        myConn.close();
        return htmlPage;
    }
    public String getCssPage(String tabItem) throws Throwable {
        //String[] placeholders;
        myConn.connect();
        String css = myConn.fetchSql("select * from tb110_css_files where id = ?", tabItem, "cssFile");

        myConn.close();
        return css;
    }
    // private String setActivePage(String htmlPage, String tabItem) throws Throwable {
    //     myConn.connect();
    //         String placeholders = myConn.fetchSql("select placeholderstring from tb910_properties where functionName = ?", tabItem, "placeholderstring");
    //         String[] placeholder = placeholders.split(";");

    //         htmlPage = htmlPage.replace(placeholder[0], "class='active'");
    //         for(int t = 1; t < placeholder.length; t++) {
    //             htmlPage = htmlPage.replace(placeholder[t], "");
    //         }
    //     myConn.close();
    //     return htmlPage;
    // }

}

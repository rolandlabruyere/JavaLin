package org.RestServer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ConstructHtmlPages {
    MySQL myConn = new MySQL();
    FuncsAndProcs fps = new FuncsAndProcs();

    public String getHtmlPage(String pageNames, String tabItem) throws Throwable {
        String[] placeholders;
        String[] pageTitle = pageNames.split(";");
        myConn.connect();
        ResultSet rs = myConn.openSql("select * from tb100_htmlpaginas where id = ?", pageTitle[0]);

        rs.next();
        String jsPlaceHolders = rs.getString("jsPlaceHolder");
        String cssPlaceHolders = rs.getString("cssPlaceHolder");
        String htmlPlaceHolders = rs.getString("htmlPlaceHolder");
        String imgPlaceHolders = rs.getString("imgPlaceHolder");
        String htmlPage = rs.getString("InlineHtml");

        if(!jsPlaceHolders.isEmpty()) {
            placeholders = jsPlaceHolders.split(";");
            for (String placeholder : placeholders) {
                String content = myConn.fetchSql("select jsContent from tb120_javaScript where idName = ?", placeholder,"jsContent");
                htmlPage = htmlPage.replace(placeholder, content);
            }
        }

        if(!cssPlaceHolders.isEmpty()) {
            placeholders = cssPlaceHolders.split(";");
            for (String placeholder : placeholders) {
                String content = myConn.fetchSql("select styleContent from tb110_cssStyleScript where idName = ?", placeholder,"styleContent");
                htmlPage = htmlPage.replace(placeholder, content);
            }
        }

        if(!imgPlaceHolders.isEmpty()) {
            placeholders = imgPlaceHolders.split(";");
            for (String placeholder : placeholders) {
                String content = myConn.fetchSql("select imgContent from tb130_images where idName = ?", placeholder,"imgContent");
                htmlPage = htmlPage.replace(placeholder, content);
            }
        }
        for(int t = 1; t < pageTitle.length; t++) {
            if (!htmlPlaceHolders.isEmpty()) {
                placeholders = htmlPlaceHolders.split(";");
                for (String placeholder : placeholders) {
                    String content = getHtmlPage(pageTitle[t], tabItem);
                    htmlPage = htmlPage.replace(placeholder, content);
                }
            }
        }
        htmlPage = setActivePage(htmlPage, tabItem);
        fps.writeToLog(htmlPage);
        myConn.close();
        return htmlPage;
    }

    private String setActivePage(String htmlPage, String tabItem) throws Throwable {
        myConn.connect();
            String placeholders = myConn.fetchSql("select placeholderstring from tb910_properties where functionName = ?", tabItem, "placeholderstring");
            String[] placeholder = placeholders.split(";");

            htmlPage = htmlPage.replace(placeholder[0], "class='active'");
            for(int t = 1; t < placeholder.length; t++) {
                htmlPage = htmlPage.replace(placeholder[t], "");
            }
        myConn.close();
        return htmlPage;
    }

}

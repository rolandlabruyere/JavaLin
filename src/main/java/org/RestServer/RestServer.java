package org.RestServer;

import io.javalin.Javalin;

import javax.swing.*;
import java.sql.SQLException;

import static org.RestServer.FuncsAndProcs.decodeBase64;

public class RestServer {

    public static void main(String[] args) throws Throwable {
        var app = Javalin.create().start(7070);

        app.get("/", ctx -> ctx.html(getRoot( "mainPage;splashPage", "home")));
        app.get("/powertrafo/<ipaddress>", ctx -> ctx.html(startSession(ctx.pathParam("ipaddress"),"mainPage", "powertrafo")));
        app.get("/filterchoke/<ipaddress>", ctx -> ctx.html(startSession(ctx.pathParam("ipaddress"),"mainPage", "filterchoke")));
        app.get("/outputtrafo/<ipaddress>", ctx -> ctx.html(startSession(ctx.pathParam("ipaddress"),"mainPage", "outputtrafo")));
        app.get("/download/<ipaddress>", ctx -> ctx.html(startSession(ctx.pathParam("ipaddress"),"mainPage", "download")));
        app.get("/search/<ipaddress>", ctx -> ctx.html(startSession(ctx.pathParam("ipaddress"),"mainPage", "search")));
        app.get("/contact/<ipaddress>", ctx -> ctx.html(startSession(ctx.pathParam("ipaddress"),"mainPage", "contact")));
        app.get("/about/<ipaddress>", ctx -> ctx.html(startSession(ctx.pathParam("ipaddress"),"mainPage", "about")));
        app.get("/faq/<ipaddress>", ctx -> ctx.html(startSession(ctx.pathParam("ipaddress"),"mainPage", "faq")));

    }

    private static String getRoot(String pageNames, String tabItem) {
        ConstructHtmlPages chp = new ConstructHtmlPages();
        try {
            return chp.getHtmlPage(pageNames, tabItem);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
    private static String startSession(String ipAddress, String pageNames, String tabItem) throws SQLException {
        MySQL conn = new MySQL();
        ipAddress = decodeBase64(ipAddress);
        FuncsAndProcs fps = new FuncsAndProcs();
        conn.connect();
       // conn.execSql("delete from tb900_sessionTracker where ipAddress = ?", ipAddress);
        conn.execSql("insert into tb900_sessionTracker (ipAddress, timestampStart) values (?, ?)", ipAddress + ";" + fps.depositTimestamp(0));
        return getRoot(pageNames, tabItem);
    }
}
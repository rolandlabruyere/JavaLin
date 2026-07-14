package org.RestServer;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

import javax.swing.*;

import static org.RestServer.FuncsAndProcs.decodeBase64;

import java.sql.SQLException;

import static org.RestServer.FuncsAndProcs.decodeBase64;


public class RestServer {

    public static void main(String[] args) throws Throwable {
        Javalin.create(config -> {
            //allow host crossover
            config.bundledPlugins.enableCors(cors -> {cors.addRule(it ->{it.anyHost();});});
            //add public folder to provide static files like css and js
            config.staticFiles.add("/public", Location.CLASSPATH);
            
            //map get and post routes
            config.routes.get("/" , ctx -> ctx.html(getRoot("indexPage")));
            config.routes.get("/powertrafo" , ctx -> ctx.html(startSession(ctx.queryParam("ipAddress"), ctx.path().replace("/", ""))));
            config.routes.get("/filterchoke", ctx -> ctx.html(startSession(ctx.queryParam("ipAddress"), ctx.path().replace("/", ""))));
            config.routes.get("/outputtrafo", ctx -> ctx.html(startSession(ctx.queryParam("ipAddress"), ctx.path().replace("/", ""))));
            config.routes.get("/download"   , ctx -> ctx.html(startSession(ctx.queryParam("ipAddress"), ctx.path().replace("/", ""))));
            config.routes.get("/search"     , ctx -> ctx.html(startSession(ctx.queryParam("ipAddress"), ctx.path().replace("/", ""))));
            config.routes.get("/contact"    , ctx -> ctx.html(startSession(ctx.queryParam("ipAddress"), ctx.path().replace("/", ""))));
            config.routes.get("/about"      , ctx -> ctx.html(startSession(ctx.queryParam("ipAddress"), ctx.path().replace("/", ""))));
            config.routes.get("/home"       , ctx -> ctx.html(startSession(ctx.queryParam("ipAddress"), ctx.path().replace("/", ""))));
        }).start(7070);
    }

    private static String getRoot(String tabItem) {
        ConstructHtmlPages chp = new ConstructHtmlPages();
        try {
            return chp.getHtmlPage(tabItem);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static String startSession(String ipAddress, String tabItem) throws SQLException {
        DbconCsales conn = new DbconCsales();
        ipAddress = decodeBase64(ipAddress);
        FuncsAndProcs fps = new FuncsAndProcs();
        conn.connect();
        conn.execSql("insert into tb980_session_tracker (ipAddress, timestamp, visitedPage) values (?, ?, ?)", ipAddress + ";" + fps.depositTimestamp(0) + ";" + tabItem);
        return getRoot(tabItem);
    }

}
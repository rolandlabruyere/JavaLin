package org.restserver;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import static org.restserver.FuncsAndProcs.decodeBase64;
import java.sql.SQLException;

public class RestServer {

    public static void main(String[] args) throws Throwable {
        Javalin.create(config -> {
            //allow host crossover
            config.bundledPlugins.enableCors(cors -> {cors.addRule(it ->{it.anyHost();});});
            //add public folder to provide static files like css and js
            config.staticFiles.add("/public", Location.CLASSPATH);
            
            //map get routes
            config.routes.get("/"                           , ctx -> ctx.html(getRoot("indexPage")));
            config.routes.get("/voedingstrafo"              , ctx -> ctx.html(trackSession(ctx.queryParam("ipAddress"), ctx.path().replace("/", ""), "")));
            config.routes.get("/smoorspoel"                 , ctx -> ctx.html(trackSession(ctx.queryParam("ipAddress"), ctx.path().replace("/", ""), "")));
            config.routes.get("/uitgangstrafo"              , ctx -> ctx.html(trackSession(ctx.queryParam("ipAddress"), ctx.path().replace("/", ""), "")));
            config.routes.get("/weetjes"                    , ctx -> ctx.html(trackSession(ctx.queryParam("ipAddress"), ctx.path().replace("/", ""), "")));
            config.routes.get("/search"                     , ctx -> ctx.html(trackSession(ctx.queryParam("ipAddress"), ctx.path().replace("/", ""), "")));
            config.routes.get("/contact"                    , ctx -> ctx.html(trackSession(ctx.queryParam("ipAddress"), ctx.path().replace("/", ""), "")));
            config.routes.get("/about"                      , ctx -> ctx.html(trackSession(ctx.queryParam("ipAddress"), ctx.path().replace("/", ""), "")));
            config.routes.get("/home"                       , ctx -> ctx.html(trackSession(ctx.queryParam("ipAddress"), ctx.path().replace("/", ""), "")));
            config.routes.get("/powerTrafoLayout/{value}"   , ctx -> ctx.html(trackSession(ctx.queryParam("ipAddress"), ctx.path().replace("/", "").replace(ctx.pathParam("value"), ""), ctx.pathParam("value"))));

            //map post routes
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

    private static String trackSession(String ipAddress, String tabItem, String value) throws SQLException {
        DbConnect conn = new DbConnect();
        ipAddress = decodeBase64(ipAddress);
        FuncsAndProcs fps = new FuncsAndProcs();
        PowerTrafo pt = new PowerTrafo();
        String resultHtml = "";

        conn.connect(0); 
        conn.execSql("insert into tb980_session_tracker (ipAddress, timestamp, visitedPage) values (?, ?, ?)", ipAddress + ";" + fps.depositTimestamp(0) + ";" + tabItem);
        switch (tabItem) {
            case "voedingstrafo"    -> resultHtml = getRoot(tabItem);
            case "smoorspoel"       -> resultHtml = getRoot(tabItem);
            case "uitgangstrafo"    -> resultHtml = getRoot(tabItem);
            case "weetjes"          -> resultHtml = getRoot(tabItem);
            case "search"           -> resultHtml = getRoot(tabItem);
            case "contact"          -> resultHtml = getRoot(tabItem); 
            case "about"            -> resultHtml = getRoot(tabItem);
            case "home"             -> resultHtml = getRoot(tabItem);
            case "powerTrafoLayout" ->resultHtml = pt.powerTrafoLayout(tabItem, ipAddress, Integer.valueOf(value));
        }
        return resultHtml;
    }

}
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
            config.routes.get("/savePtrafoLayout/{value}"   , ctx -> ctx.html(trackSession(ctx.queryParam("ipAddress"), ctx.path().replace("/", ""), ctx.pathParam("value"))));

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

        conn.connect(0); 
        conn.execSql("insert into tb980_session_tracker (ipAddress, timestamp, visitedPage) values (?, ?, ?)", ipAddress + ";" + fps.depositTimestamp(0) + ";" + tabItem);
        switch (tabItem) {
            case "voedingstrafo"    -> getRoot(tabItem);
            case "smoorspoel"       -> getRoot(tabItem);
            case "uitgangstrafo"    -> getRoot(tabItem);
            case "weetjes"          -> getRoot(tabItem);
            case "search"           -> getRoot(tabItem);
            case "contact"          -> getRoot(tabItem); 
            case "about"            -> getRoot(tabItem);
            case "home"             -> getRoot(tabItem);
            case "savePtrafoLayout" -> pt.savePowerTrafoLayout(ipAddress, Integer.valueOf(value));
        }
        return getRoot(tabItem);
    }

}
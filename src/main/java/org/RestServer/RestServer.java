package org.restserver;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import java.sql.SQLException;
import org.restserver.HtmlRenderer.PrintDesignForm;
import org.restserver.common.FuncsAndProcs;
import org.restserver.database.DbConnect;
import org.restserver.htmlConstructor.ConstructHtmlPages;
import org.restserver.trafoClasses.PowerTrafo;
import static org.restserver.common.FuncsAndProcs.decodeBase64;

public class RestServer {

    public static void main(String[] args) throws Throwable {
        Javalin.create(config -> {
            //allow host crossover
            config.bundledPlugins.enableCors(cors -> {cors.addRule(it ->{it.anyHost();});});
            //add public folder to provide static files like css and js
            config.staticFiles.add("/public", Location.CLASSPATH);
            
            //map get routes
            config.routes.get("/"                           , ctx -> ctx.html(getRoot("indexPage")));
            config.routes.get("/clear"                      , ctx -> ctx.html(""));
            config.routes.get("/voedingstrafo"              , ctx -> ctx.html(trackSession(ctx.queryParam("ipAddress"), ctx.path().replace("/", ""), "")));
            config.routes.get("/smoorspoel"                 , ctx -> ctx.html(trackSession(ctx.queryParam("ipAddress"), ctx.path().replace("/", ""), "")));
            config.routes.get("/uitgangstrafo"              , ctx -> ctx.html(trackSession(ctx.queryParam("ipAddress"), ctx.path().replace("/", ""), "")));
            config.routes.get("/weetjes"                    , ctx -> ctx.html(trackSession(ctx.queryParam("ipAddress"), ctx.path().replace("/", ""), "")));
            config.routes.get("/diversen"                   , ctx -> ctx.html(trackSession(ctx.queryParam("ipAddress"), ctx.path().replace("/", ""), "")));
            config.routes.get("/zoeken"                     , ctx -> ctx.html(trackSession(ctx.queryParam("ipAddress"), ctx.path().replace("/", ""), "")));
            config.routes.get("/contact"                    , ctx -> ctx.html(trackSession(ctx.queryParam("ipAddress"), ctx.path().replace("/", ""), "")));
            config.routes.get("/about"                      , ctx -> ctx.html(trackSession(ctx.queryParam("ipAddress"), ctx.path().replace("/", ""), "")));
            config.routes.get("/instellingen"               , ctx -> ctx.html(trackSession(ctx.queryParam("ipAddress"), ctx.path().replace("/", ""), "")));
            config.routes.get("/home"                       , ctx -> ctx.html(trackSession(ctx.queryParam("ipAddress"), ctx.path().replace("/", ""), "")));
            config.routes.get("/powerTrafoLayout"           , ctx -> ctx.html(trackSession(ctx.queryParam("ipAddress"), ctx.path().replace("/", ""), ctx.queryParam("value"))));
            config.routes.get("/prepareSales"               , ctx -> ctx.html(trackSession(ctx.queryParam("ipAddress"), ctx.path().replace("/", ""), "")));
            config.routes.get("/pdfWikkelschema"            , ctx -> ctx.html(trackSession(ctx.queryParam("ipAddress"), ctx.path().replace("/", ""), "")));
            
            //map post routes
            config.routes.post("/powertrafo"             , ctx -> ctx.html(trackSession(ctx.queryParam("ipAddress"), ctx.path().replace("/", ""), ctx.queryParam("savedValues"))));
        }).start(7070);
    }

    private static String trackSession(String ipAddress, String tabItem, String value) throws SQLException {
        DbConnect conn = new DbConnect();
        FuncsAndProcs fps = new FuncsAndProcs();
        PowerTrafo pt = new PowerTrafo();
        PrintDesignForm pdf = new PrintDesignForm();
        String resultHtml = "";
        ipAddress = decodeBase64(ipAddress);

        conn.connect(); 
        conn.execSql("insert into voorthuiscustomersales.tb980_session_tracker (ipAddress, timestamp, visitedPage) values (?, ?, ?)", ipAddress + ";" + fps.depositTimestamp(0) + ";" + tabItem);

        switch (tabItem) {
            case "voedingstrafo"    -> resultHtml = getRoot(tabItem);
            case "smoorspoel"       -> resultHtml = getRoot(tabItem);
            case "uitgangstrafo"    -> resultHtml = getRoot(tabItem);
            case "weetjes"          -> resultHtml = getRoot(tabItem);
            case "zoeken"           -> resultHtml = getRoot(tabItem);
            case "diversen"         -> resultHtml = getRoot(tabItem);
            case "contact"          -> resultHtml = getRoot(tabItem); 
            case "about"            -> resultHtml = getRoot(tabItem);
            case "instellingen"     -> resultHtml = getRoot(tabItem);
            case "home"             -> resultHtml = getRoot(tabItem);
            case "prepareSales"     -> resultHtml = getRoot(tabItem);
            case "powerTrafoLayout" -> resultHtml = pt.powerTrafoLayout(tabItem, ipAddress, Integer.valueOf(value));
            case "powertrafo"       -> resultHtml = pt.postPowerTrafoSpecs(tabItem, ipAddress, value);
            case "pdfWikkelschema"  -> resultHtml = pdf.generatePdfDoc(tabItem, ipAddress);
       }
        return resultHtml;
    }

    private static String getRoot(String tabItem) {
        ConstructHtmlPages chp = new ConstructHtmlPages();
        try {
            return chp.getHtmlPage(tabItem);
        } catch (Throwable e) {
            return "ERROR: deze pagina is nog \"under construction\" ";
        }
    }

}
package org.restserver.HtmlRenderer;
import java.sql.SQLException;

import org.restserver.ConstructHtmlPages;
import org.restserver.DbConnect;
import org.restserver.FuncsAndProcs;

public class PrintDesignForm {
    FuncsAndProcs fps = new FuncsAndProcs();
    ConstructHtmlPages chp = new ConstructHtmlPages();
    String sourcePath = "src/main/resources/public/html/";
    HtmlToPdfOpenPDF h2p = new HtmlToPdfOpenPDF();

    public String generatePdfDoc(String tabItem, String ipAddress) throws SQLException{
        DbConnect conn = new DbConnect();
        conn.connect();
        String htmlPage = chp.getHtmlPage(tabItem);
        String trafoNumber = conn.fetchSql("select max(orderNum) as orderNumber from voorthuishtmlpages.vw810_full_csv_layout_turn_schem  where ip = ?", ipAddress, "orderNumber");
        String[] dataItems = conn.fetchSql("select * from voorthuishtmlpages.vw810_full_csv_layout_turn_schem where ip = ? and orderNum = ?", ipAddress + ";" + trafoNumber); 
        String[] placeholders = chp.getPlaceholders(tabItem).split(";");
        String[] hidePlaceholders = chp.getPlaceholders(tabItem + "_bools").split(";");

        for (int t = 0; t < placeholders.length; t++ ){
            htmlPage = htmlPage.replace(placeholders[t], dataItems[t + 1]);
        }
        for (String placeholder: hidePlaceholders ){
            htmlPage = htmlPage.replace(placeholder, "hidden");
        }
        
        htmlPage = htmlPage.replace("hidden hidden", "hidden");

        h2p.convertHtml2Pdf(trafoNumber, htmlPage);

        try{
            fps.deleteFile(sourcePath + trafoNumber + ".html");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return "do something";
    }
}

package org.restserver.HtmlRenderer;
import java.sql.SQLException;

import org.restserver.ConstructHtmlPages;
import org.restserver.DbConnect;
import org.restserver.FuncsAndProcs;

public class PrintDesignForm {
    FuncsAndProcs       fps = new FuncsAndProcs();
    ConstructHtmlPages  chp = new ConstructHtmlPages();
    PdfGenerator        pg = new PdfGenerator();

    String sourcePath   = "src/main/resources/public/html/";
    String destPath     = "src/main/resources/public/download/";

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

        for (int t = 0; t < dataItems.length; t++ ){
            switch (t){
                case 4, 5, 9 ->  {
                    if(dataItems[t].equalsIgnoreCase( "nee")) {
                        htmlPage = htmlPage.replace(hidePlaceholders[t - 4], "hidden");
                    }  else {
                        htmlPage = htmlPage.replace(hidePlaceholders[t - 4], "");
                    }
                }
                case 6, 7, 8 ->  {
                    if(dataItems[t].equals( "0.00")) {
                        htmlPage = htmlPage.replace(hidePlaceholders[t - 4], "hidden");
                    }  else {
                        htmlPage = htmlPage.replace(hidePlaceholders[t - 4], "");
                    }
                }
            }
        }
        
        htmlPage = htmlPage.replace("hidden hidden", "hidden");
        fps.writeToAnyFile(sourcePath + trafoNumber + ".html", htmlPage);

        return pg.createPdf(trafoNumber);

    }
}


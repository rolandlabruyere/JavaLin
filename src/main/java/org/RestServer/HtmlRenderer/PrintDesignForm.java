package org.restserver.HtmlRenderer;
import org.restserver.FuncsAndProcs;

public class PrintDesignForm {
    FuncsAndProcs fps = new FuncsAndProcs();
    String sourcePath = "src/main/resources/public/html/";

    public String generatePdfDoc(String tabItem, String ipAddress){
        
        return "anything";
    }

    public void createPdf(String trafoNumber){
        PdfGenerator pg = new PdfGenerator();
        try{
            pg.createPdf(trafoNumber + ".html", trafoNumber + ".pdf");
            fps.deleteFile(sourcePath + trafoNumber + ".html");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }        
}

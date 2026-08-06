package org.restserver.HtmlRenderer;
import com.itextpdf.text.*;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.tool.xml.*;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.StringReader;
import org.restserver.FuncsAndProcs;

public class HtmlToPdfOpenPDF {
    String destPath = "src/main/resources/public/downloads/";
    String sourcePath = "src/main/resources/public/html/";
    FuncsAndProcs fps = new FuncsAndProcs();

    @SuppressWarnings("deprecation")
    public String convertHtml2Pdf(String trafoNumber, String htmlContent) {
        Document document = new Document();

        fps.writeToAnyFile(sourcePath + "html_" + trafoNumber + ".html", htmlContent);
        try {
            
            PdfWriter.getInstance(document, new FileOutputStream(destPath + trafoNumber + ".pdf"));

            document.open();
            HTMLWorker htmlWorker = new HTMLWorker(document);
            htmlWorker.parse(new StringReader(htmlContent));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

        return destPath + trafoNumber + ".pdf";
    }

    public String generatePdfFromHtml(String trafoNumber){
        Document document = new Document();
        PdfWriter writer;
        String outputDoc = sourcePath + trafoNumber + ".pdf"; 

        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(outputDoc));
            document.open();
            try {
                XMLWorkerHelper.getInstance().parseXHtml(writer, document, 
                    new FileInputStream(sourcePath + trafoNumber + ".html"), 
                    new FileInputStream("src/main/resources/public/css/pdf.css")
                );
            } catch (Exception e) {
                e.getMessage();
            }
        }catch(Exception e){e.getMessage();}
        document.close();
        return outputDoc;
   }
}
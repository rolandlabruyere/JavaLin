package org.restserver.HtmlRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.restserver.common.FuncsAndProcs;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

public class PdfGenerator {
    String inputPath = "src/main/resources/public/html/";
    String outputPath = "src/main/resources/public/downloads/";
    String relDownloadPath = "downloads/";
    FuncsAndProcs fps = new FuncsAndProcs();

    public String createPdf(String trafoNumber) {
        File inputHTML = new File(inputPath + trafoNumber + ".html"); 

        try {
            Document parsedHtml = parseHtml(inputHTML);   
            convertXhtmlToPdf(parsedHtml, outputPath + trafoNumber + ".pdf"); 
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
        }

        try{
            fps.deleteFile(inputPath + trafoNumber + ".html");
        } catch(Exception e){e.getMessage();}
        
        return relDownloadPath + trafoNumber + ".pdf";
    }

    private Document parseHtml(File htmlFile) throws IOException{
            Document document = Jsoup.parse(htmlFile, "UTF-8");
            document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
            return document;    
        }

    private void convertXhtmlToPdf(Document htmlFile, String outputPdf) throws IOException{
        try (OutputStream fos = new FileOutputStream(outputPdf)) {

            //set a base url from where css files can be imported 
            String baseUrl = FileSystems.getDefault()
                .getPath("src/main/resources/public/")
                .toUri().toURL().toString();
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withUri(outputPdf);
            builder.withW3cDocument(new W3CDom().fromJsoup(htmlFile), baseUrl);
            builder.toStream(fos);
            builder.run();
        }  
    }
}
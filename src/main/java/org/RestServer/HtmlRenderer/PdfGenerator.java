package org.restserver.HtmlRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

public class PdfGenerator {
    String inputPath = "src/main/resources/public/html/";
    String outputPath = "src/main/resources/public/downloads";

    public void createPdf(String src, String dest) {
        File inputHTML = new File(inputPath + src); 
        Document parsedHtml = null;

        try {
            parsedHtml = parseHtml(inputHTML);   
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            FileOutputStream pdfFile =(FileOutputStream)convertXhtmlToPdf(parsedHtml, outputPath + dest); 
            pdfFile.write(pdfFile.toString().length());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private Document parseHtml(File htmlFile) throws IOException{
            Document document = Jsoup.parse(htmlFile, "UTF-8");
            document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
            return document;    
        }

    private OutputStream convertXhtmlToPdf(Document htmlFile, String outputPdf) throws IOException{
        try (OutputStream fos = new FileOutputStream(outputPdf)) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withUri(outputPdf);
            builder.toStream(fos);
            builder.withW3cDocument(new W3CDom().fromJsoup(htmlFile), "/");
            builder.run();
            return fos;  
        }  
    }    
}
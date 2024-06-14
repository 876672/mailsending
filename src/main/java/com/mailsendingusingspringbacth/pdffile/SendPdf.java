package com.mailsendingusingspringbacth.pdffile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.springframework.stereotype.Component;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

@Component
public class SendPdf {

    public File generatePdf() {
        File htmlFile = new File("src/main/resources/static/BicReminderCL01.html");
        File pdfFile = new File("target/estatement.pdf");

        if (!htmlFile.exists()) {
            System.out.println("========= HTML file not found ===========");
            throw new RuntimeException(new FileNotFoundException("HTML file not found: " + htmlFile.getAbsolutePath()));
        }

        try (OutputStream outputStream = new FileOutputStream(pdfFile)) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withFile(htmlFile);
            builder.toStream(outputStream);
            builder.run();
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
		return pdfFile;
    
}
    
}

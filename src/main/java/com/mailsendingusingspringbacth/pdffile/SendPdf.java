package com.mailsendingusingspringbacth.pdffile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.mailsendingusingspringbacth.model.Student;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SendPdf {

	private final SpringTemplateEngine templateEngine;

	public File generatePdf(Student student) throws IOException {
		
		System.out.println("===================student"+student);
	
//		Resource resource = new ClassPathResource("/BicReminderCL01.html");
//		File htmlFile = resource.getFile();
		File pdfFile = new File("target/estatement.pdf");
//		if (!htmlFile.exists()) {
//			System.out.println("========= HTML file not found ===========");
//			throw new RuntimeException(new FileNotFoundException("HTML file not found: " + htmlFile.getAbsolutePath()));
//		}
//		String htmlContent = new String(
//				Files.readAllBytes(Paths.get("src/main/resources/templates/BicReminderCL01.html")));
		try (OutputStream outputStream = new FileOutputStream(pdfFile)) {
			PdfRendererBuilder builder = new PdfRendererBuilder();
			Context context = new Context();
		
			context.setVariable("code",student.getCode());
			String htmlContent1 = templateEngine.process("BicReminderCL01", context);
			builder.withHtmlContent(htmlContent1,"classpath:/static/");
			
			builder.toStream(outputStream);
			builder.run();

		} catch (IOException e) {
			System.err.println("I/O error: " + e.getMessage());
		}
		return pdfFile; 

	}

}

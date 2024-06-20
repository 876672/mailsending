package com.mailsendingusingspringbacth.emailService;

import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.mailsendingusingspringbacth.model.Student;
import com.mailsendingusingspringbacth.pdffile.SendPdf;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class EmailServiceImpl {

    @Autowired
    private JavaMailSender emailSender;
    
    @Autowired
    private SendPdf sendPdf;

    /**
     * Send mime message.
     *
     * @param student the student
     * @param subject the subject
     * @param pathToAttachment the path to attachment
     * @throws Exception the exception
     */
    public void sendMimeMessage(Student student, String subject, String pathToAttachment) throws Exception {
         MimeMessage message = emailSender.createMimeMessage();

         MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(student.getEmail());
        helper.setSubject(subject);
        helper.setText("Dear " + student.getFullname() + ",\n\n" +
                "The PDF provided below is your bank statement.", true);

          File pdfFile = sendPdf.generatePdf(student);
        
          if (!pdfFile.exists()) {
            throw new MessagingException("Attachment file not found: " + pathToAttachment);
        }

        FileSystemResource attachment = new FileSystemResource(pdfFile);
        try (PDDocument doc = PDDocument.load(pdfFile)) {
            AccessPermission accessPermission = new AccessPermission();
            accessPermission.setCanExtractContent(true);
            accessPermission.setCanModify(true);
            accessPermission.setCanModifyAnnotations(true);
            accessPermission.setCanPrint(true);
            accessPermission.setCanPrintDegraded(true);
            accessPermission.setCanAssembleDocument(true);
            accessPermission.setCanExtractForAccessibility(true);
            accessPermission.setCanFillInForm(true);

            String password = student.getFullname();
            StandardProtectionPolicy spp = new StandardProtectionPolicy(password, password, accessPermission);
            spp.setEncryptionKeyLength(128);
            spp.setPermissions(accessPermission);

            doc.protect(spp);

            File encryptedFile = new File("target/estatement.pdf");
            doc.save(pdfFile);
            attachment = new FileSystemResource(encryptedFile);

        }  catch (IOException e) {
            throw new MessagingException("Error processing PDF file: " + e.getMessage(), e);
        
        }
       
        helper.addAttachment(attachment.getFilename(), attachment);
        log.info("----------------------- email send successfully ----------------------");
        emailSender.send(message);

    }
}
        


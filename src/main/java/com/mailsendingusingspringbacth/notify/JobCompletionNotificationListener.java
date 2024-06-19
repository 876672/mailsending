//package com.mailsendingusingspringbacth.notify;
//
//import java.io.IOException;
//import java.util.List;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.batch.core.BatchStatus;
//import org.springframework.batch.core.JobExecution;
//import org.springframework.batch.core.JobExecutionListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.mailsendingusingspringbacth.model.Student;
//import com.mailsendingusingspringbacth.pdffile.ExcelGenerator;
//import com.mailsendingusingspringbacth.processor.ItemProcessorFile;
//
//
//
//
//@Component
//public class JobCompletionNotificationListener implements JobExecutionListener {
//
//	
//	private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);
//
//    @Autowired
//    private ItemProcessorFile itemProcessorFile;
//
//    @Autowired
//    private ExcelGenerator excelGenerator;
//
//    @Override
//    public void afterJob(JobExecution jobExecution) {
//        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
//            try {
//                List<Student> invalidEmailStudents = itemProcessorFile.getInvalidEmailStudents();
//                excelGenerator.generateExcel(invalidEmailStudents);
//                log.info("Job finished, invalid emails saved to Excel file.");
//            } catch (IOException e) {
//                log.error("Error writing invalid emails workbook: {}", e.getMessage());
//            }
//        }
//    }
//}

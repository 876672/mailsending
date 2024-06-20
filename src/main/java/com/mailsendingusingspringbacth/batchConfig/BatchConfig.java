package com.mailsendingusingspringbacth.batchConfig;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import com.mailsendingusingspringbacth.model.Student;
//import com.mailsendingusingspringbacth.notify.JobCompletionNotificationListener;
import com.mailsendingusingspringbacth.pdffile.ExcelGenerator;
//import com.mailsendingusingspringbacth.pdffile.SendPdf;
import com.mailsendingusingspringbacth.processor.ItemProcessorFile;
import com.mailsendingusingspringbacth.repositories.StuRepository;

import lombok.RequiredArgsConstructor;




@Configuration
@RequiredArgsConstructor
public class BatchConfig {

 	private final StuRepository stuRepository;
	 
 	private final PlatformTransactionManager transactionManager;
	
//    private final JobCompletionNotificationListener listener;
//    private final ExcelGenerator excelGenerator; 

	/**
 * Email sender job.
 *
 * @param jobRepository the job repository
 * @param dataSource the data source
 * @return the job
 */
@Bean
	public Job emailSenderJob(JobRepository jobRepository,DataSource dataSource) {
		System.out.println("job-------------------");
		return new JobBuilder("send-mail", jobRepository)
				.start(emailSenderStep(jobRepository))
				.next(createExcelWithInvalidEmail(jobRepository,dataSource))
				.incrementer(new RunIdIncrementer())
				.build();
	}

	/**
	 * Email sender step.
	 *
	 * @param jobRepository the job repository
	 * @return the step
	 */
	@Bean
	public Step emailSenderStep(JobRepository jobRepository) {
		System.out.println("Step-------------------11133");

		return new StepBuilder("step1", jobRepository)
				.<Student, Student>chunk(10, transactionManager)
				.reader(readFile())
				.writer(writeFile())
				.allowStartIfComplete(true)
				.build();
	}
	
	/**
	 * Creates the excel with invalid email.
	 *
	 * @param jobRepository the job repository
	 * @param dataSource the data source
	 * @return the step
	 */
	@Bean
	public Step createExcelWithInvalidEmail(JobRepository jobRepository,DataSource dataSource) {
		System.out.println("Step-------------------11133");

		return new StepBuilder("step2", jobRepository)
				.<Student, Student>chunk(10, transactionManager)
                .reader(readFile())
				.processor(stuProcessor())
				.writer(writeInExcel())
				.build();
	}
			

                  

 
	    /**
    	 * Write in excel.
    	 *
    	 * @return the item writer<? super student>
    	 */
    	@Bean
	   public ItemWriter<? super Student> writeInExcel() {
		return new WriterExcel();
	}

//	    @Bean
//		public JdbcCursorItemReader<Student> databaseReader(DataSource dataSource) {
////	        JdbcCursorItemReader<Student> reader = new JdbcCursorItemReader<>();
////	        reader.setName("databaseReader"); 
////	        reader.setDataSource(dataSource);
////	        
////	        
////	        reader.setSql("SELECT id, fullname, email FROM student"); 
////	        reader.setRowMapper(new BeanPropertyRowMapper<>(Student.class));
////	        return reader;
//	    	 return new JdbcCursorItemReaderBuilder<Student>()
//	                 .name("personItemReader")
//	                 .dataSource(dataSource)
//	                 .sql("SELECT id, fullname, email FROM student")
//	                 .beanRowMapper(Student.class)
//	                 .build();
//	                 
//	                 
//	    }
//	        
	
	

//	@Bean
//	public Step pdfStep(JobRepository jobRepository) {
//	    System.out.println("Step-------------------");
//
//	    return new StepBuilder("step1133", jobRepository)
//				.allowStartIfComplete(true)
//
//	            .tasklet((contribution, chunkContext) -> {
//	                sendPdf.generatePdf();
//	                return RepeatStatus.FINISHED;
//	            }, new ResourcelessTransactionManager())
//	            .build();
//	}

	
	/**
 * Stu processor.
 *
 * @return the item processor
 */
@Bean
	public ItemProcessor<Student, Student> stuProcessor() {
		System.out.println("ItemProcessor-------------------");
		return new ItemProcessorFile();
	}



	/**
	 * Write file.
	 *
	 * @return the repository item writer
	 */
	@Bean
	public RepositoryItemWriter<Student> writeFile() {
		System.out.println("RepositoryItemWriter-------------------");
		RepositoryItemWriter<Student> itemWriter = new RepositoryItemWriter<Student>();
		itemWriter.setMethodName("save");
		itemWriter.setRepository(stuRepository);
		
		return itemWriter;
	}



	
	

	/**
	 * Read file.
	 *
	 * @return the flat file item reader
	 */
	@Bean
	public FlatFileItemReader<Student> readFile() {
		System.out.println("FlatFileItemReader-------------------");

//		FlatFileItemReader<Student> fileItemReader = new FlatFileItemReader<>();
//		
//		fileItemReader.setResource(new FileSystemResource("src/main/resources/students.txt"));
	//		fileItemReader.setLineMapper(lineMapper());
	//        fileItemReader.setName("reader");
	//		fileItemReader.setLinesToSkip(1);
	//		return fileItemReader;
		
	    return new FlatFileItemReaderBuilder<Student>()
	    		 .name("reader")
	             .resource(new FileSystemResource("src/main/resources/students.txt"))
	             .lineMapper(lineMapper())
	             .linesToSkip(1)
	             .build();
	    		
 
	}

	/**
	 * Line mapper.
	 *
	 * @return the line mapper
	 */
	public LineMapper<Student> lineMapper() {
		DefaultLineMapper<Student> lineMapper = new DefaultLineMapper<>();

		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(",");
		lineTokenizer.setNames("id", "code", "email", "fullname");
		
		BeanWrapperFieldSetMapper<Student> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(Student.class);
		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);
		return lineMapper;
	}
}



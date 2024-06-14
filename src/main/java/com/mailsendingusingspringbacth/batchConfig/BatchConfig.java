package com.mailsendingusingspringbacth.batchConfig;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.mailsendingusingspringbacth.model.Student;
//import com.mailsendingusingspringbacth.pdffile.SendPdf;
import com.mailsendingusingspringbacth.processor.ItemProcessorFile;
import com.mailsendingusingspringbacth.repositories.StuRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

	private final StuRepository stuRepository;
	private final PlatformTransactionManager transactionManager;
//	private final SendPdf sendPdf;

	@Bean
	public Job emailSenderJob(JobRepository jobRepository) {
		System.out.println("job-------------------");
		return new JobBuilder("send-mail1331", jobRepository)
				.start(emailSenderStep(jobRepository))
//				.next(pdfStep(jobRepository))
				.incrementer(new RunIdIncrementer())
				.build();
	}

	@Bean
	public Step emailSenderStep(JobRepository jobRepository) {
		System.out.println("Step-------------------11133");

		return new StepBuilder("step223", jobRepository)
				.<Student, Student>chunk(10, transactionManager)
				.reader(readFile())
				.writer(writeFile())
				
				.processor(stuProcessor())
				.allowStartIfComplete(true)
				.build();
	}
	
	
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

	
	@Bean
	public ItemProcessor<Student, Student> stuProcessor() {
		System.out.println("ItemProcessor-------------------");
		return new ItemProcessorFile();
	}



	@Bean
	public RepositoryItemWriter<Student> writeFile() {
		System.out.println("RepositoryItemWriter-------------------");

		RepositoryItemWriter<Student> itemWriter = new RepositoryItemWriter<Student>();
		itemWriter.setMethodName("save");
		itemWriter.setRepository(stuRepository);

		return itemWriter;

	}

	@Bean
	public FlatFileItemReader<Student> readFile() {
		System.out.println("FlatFileItemReader-------------------");

		FlatFileItemReader<Student> fileItemReader = new FlatFileItemReader<>();
		fileItemReader.setResource(new FileSystemResource("src/main/resources/students.txt"));
		fileItemReader.setLineMapper(lineMapper());
        fileItemReader.setName("reader");
		fileItemReader.setLinesToSkip(1);
		return fileItemReader;
	}

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

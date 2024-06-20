package com.mailsendingusingspringbacth.batchConfig;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.mailsendingusingspringbacth.model.Student;
import com.mailsendingusingspringbacth.pdffile.ExcelGenerator;



@Component
public class WriterExcel  implements ItemWriter<Student>{
  

	@Autowired
	ExcelGenerator excelGenerator; 
	
	/**
	 * Write.
	 *
	 * @param chunk the chunk
	 * @throws Exception the exception
	 */
	@Override
	public void write(Chunk<? extends Student> chunk) throws Exception {
		
		excelGenerator.generateExcel(chunk);
		  
	}
		
}
		
	
	


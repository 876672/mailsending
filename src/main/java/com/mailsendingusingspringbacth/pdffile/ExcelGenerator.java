package com.mailsendingusingspringbacth.pdffile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.item.Chunk;
import org.springframework.stereotype.Component;
import com.mailsendingusingspringbacth.model.Student;




@Component
public class ExcelGenerator {

	/**
	 * Generate excel.
	 *
	 * @param chunk the chunk
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void generateExcel(Chunk<? extends Student> chunk) throws IOException {

		System.out.println("generateExcel---------------");
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Invalid Emails");

		int rownum = 0;
		Row headerRow = sheet.createRow(rownum++);

		headerRow.createCell(0).setCellValue("Name");
		headerRow.createCell(1).setCellValue("Email");

		for (Student student : chunk.getItems()) {
        
			boolean isCheck = checkValidEmail(student.getEmail());
			if (!isCheck) {
				Row row = sheet.createRow(rownum++);

				row.createCell(1).setCellValue(student.getEmail() != null ? student.getEmail() : "");
				row.createCell(0).setCellValue(student.getFullname());

			}
		}

		try (FileOutputStream fileOut = new FileOutputStream("invalid_emails.xlsx")) {
			workbook.write(fileOut);
		}
		workbook.close();
	}

	/**
	 * Check valid email.
	 *
	 * @param email the email
	 * @return true, if successful
	 */
	// -------------------------------------------------------------------------------------------------
	private boolean checkValidEmail(String email) {

		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";

		Pattern pat = Pattern.compile(emailRegex);
		if (email == null)
			return false;
		return pat.matcher(email).matches();
	}

}

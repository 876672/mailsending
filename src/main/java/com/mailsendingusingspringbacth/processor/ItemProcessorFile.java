package com.mailsendingusingspringbacth.processor;

import java.util.regex.Pattern;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mailsendingusingspringbacth.emailService.EmailServiceImpl;
import com.mailsendingusingspringbacth.model.Student;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ItemProcessorFile implements ItemProcessor<Student, Student> {

	@Autowired
	EmailServiceImpl emailService;


	/**
	 * Process.
	 *
	 * @param student the student
	 * @return the student
	 * @throws Exception the exception
	 */
	@Override
	public Student process(Student student) throws Exception {
		try {
			if (checkValidEmail(student.getEmail())) {
				emailService.sendMimeMessage(student, "Bank Statement", pathToAttachment);
				log.debug("processor: {}", student);

			} else {
				log.info("Invalid email: {}", student.getEmail());
				System.out.println("student---------------" + student);

			}

		} catch (Exception exception) {
			log.info("error: {}", exception.getMessage());
		}
		return student;
	}

	/** The path to attachment. */
	String pathToAttachment = "target/estatement.pdf";

	// ----------------------------------------------------------------------------------------------------

	/**
	 * Check valid email.
	 *
	 * @param email the email
	 * @return true, if successful
	 */
	private boolean checkValidEmail(String email) {

		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";

		Pattern pat = Pattern.compile(emailRegex);
		if (email == null)
			return false;
		return pat.matcher(email).matches();
	}

//	public List<Student> getInvalidEmailStudents() {
//		System.out.println("invalidEmailStudents-------" + invalidEmailStudents);
//		return invalidEmailStudents;
//	}

}

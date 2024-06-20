package com.mailsendingusingspringbacth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mailsendingusingspringbacth.model.Student;


/**
 * The Interface StuRepository.
 */
public interface StuRepository extends JpaRepository<Student, String>{

}

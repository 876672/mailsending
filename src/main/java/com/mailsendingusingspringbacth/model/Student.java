package com.mailsendingusingspringbacth.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
public class Student {
	
	@Id
private String id;
private String fullname;
private String code;
private String email;

}

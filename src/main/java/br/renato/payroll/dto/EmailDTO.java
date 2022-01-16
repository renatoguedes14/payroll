package br.renato.payroll.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Data
public class EmailDTO implements Serializable {

	@NotBlank
	private String ownerRef;
	@NotBlank
	@Email
	private String emailFrom;
	@NotBlank
	@Email
	private String emailTo;
	@NotBlank
	private String subject;
	@NotBlank
	private String text;
	private String company;
	private List<String> employees;
}
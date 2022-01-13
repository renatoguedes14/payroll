package br.renato.payroll.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ErrorDTO implements Serializable {

	public static final long serialVersionUID = 1L;

	private Long timestamp;
	private Integer status;
	private String error;
}

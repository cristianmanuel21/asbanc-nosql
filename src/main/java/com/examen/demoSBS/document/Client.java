package com.examen.demoSBS.document;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Client {
	
	private String dni;
	
	private String numberAccount;
	
	private Double availableAmount;
	
	private LocalDate  withdrawalDate;
	
	private Afp afp;	

}

package com.examen.demoSBS.document;

import java.time.LocalDate;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Document(collection="withdrawalRequests")
@Data
public class WithdrawalRequest {
	
	@Id
	private String id;
	
	@NotBlank(message="must not be empty")
	@Size(min = 8,max=8, message="Size must be 8")
	private String dni;
	
	@NotNull
	private Double withdrawalAmount;
	
	@NotNull(message="must not be empty")
	private String afpName;
	
	private String numberAccount;
	
	private LocalDate  withdrawalDate;
	

}

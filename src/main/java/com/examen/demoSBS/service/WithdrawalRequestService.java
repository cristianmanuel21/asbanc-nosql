package com.examen.demoSBS.service;

import java.util.List;
import java.util.Optional;

import com.examen.demoSBS.document.WithdrawalRequest;

public interface WithdrawalRequestService {
	
	public List<WithdrawalRequest> getAll();
	
	public Optional<WithdrawalRequest> getByDni(String dni);
	
	public WithdrawalRequest save(WithdrawalRequest withdrawalRequest);
	
	public void deleteWithdrawalRequest(String dni);

}

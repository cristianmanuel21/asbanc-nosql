package com.examen.demoSBS.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.examen.demoSBS.document.WithdrawalRequest;

public interface WithdrawalRequestRepository extends MongoRepository<WithdrawalRequest, String>{
	
	//public Optional<WithdrawalRequest> find(String dni);
	public Optional<WithdrawalRequest> findByDni(String dni);

}

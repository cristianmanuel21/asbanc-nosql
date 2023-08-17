package com.examen.demoSBS.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.examen.demoSBS.document.WithdrawalRequest;
import com.examen.demoSBS.repository.WithdrawalRequestRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WithdrawalRequestServiceImpl implements WithdrawalRequestService{
	
	final private WithdrawalRequestRepository withdrawalRequestRepository;

	@Override
	public List<WithdrawalRequest> getAll() {
		return withdrawalRequestRepository.findAll();
	}

	@Override
	public Optional<WithdrawalRequest> getByDni(String dni) {
		return withdrawalRequestRepository.findByDni(dni);
	}

	@Override
	public WithdrawalRequest save(WithdrawalRequest withdrawalRequest) {
		return withdrawalRequestRepository.save(withdrawalRequest);
	}

	@Override
	public void deleteWithdrawalRequest(String dni) {
		withdrawalRequestRepository.deleteById(dni);
	}

}

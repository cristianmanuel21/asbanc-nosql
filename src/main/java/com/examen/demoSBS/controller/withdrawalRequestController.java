package com.examen.demoSBS.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.examen.demoSBS.clients.AfpClientFeign;
import com.examen.demoSBS.document.Afp;
import com.examen.demoSBS.document.Client;
import com.examen.demoSBS.document.WithdrawalRequest;
import com.examen.demoSBS.service.WithdrawalRequestService;

import feign.FeignException;

@RestController
@RequestMapping("/withdrawalRequest")
public class withdrawalRequestController {
	
	@Autowired
	private WithdrawalRequestService withdrawalRequestService;
	
	@Autowired
	private AfpClientFeign afpClientFeign;
	
	
	@RequestMapping
	public ResponseEntity<?> getAll(){
		return ResponseEntity.ok(withdrawalRequestService.getAll());
	}
	
	@RequestMapping("/dni/{dni}")
	public ResponseEntity<?> getByDniFinal(@PathVariable("dni") String dni){
		WithdrawalRequest w=this.getByDni(dni);
		if(w!=null)	return ResponseEntity.ok(w);
		return ResponseEntity.notFound().build();
	}
	
	@RequestMapping("/afp/{nameAfp}")
	public ResponseEntity<?> getAllByAfp(@PathVariable("nameAfp") String nameAfp){
		if (afpClientFeign.getAllClient(nameAfp).size()>0)return ResponseEntity.ok(afpClientFeign.getAllClient(nameAfp));
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping
	public ResponseEntity<?> saveWithdrawalRequest(@Valid @RequestBody WithdrawalRequest withdrawalRequest, BindingResult result){
		Client client=null;
		Afp afp=null;
		Map<String,Object> response= new HashMap<>();

		if(result.hasErrors()) {
			return validar(result,response);
		}
		
		try {
			client=afpClientFeign.findByDni(withdrawalRequest.getDni());
			afp=afpClientFeign.getAfp(withdrawalRequest.getAfpName());
		}catch(FeignException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje","El cliente no esta registrado en la base de datos o hay un error en la comunicacion "+e.getMessage()));
		}
		
		// validar que la afp de la peticion sea la misma de la afp que traigo del cliente
		
		if(client!=null && afp!=null) {
			//1. el monto a retirar no debe ser mayor al monto total que obtengo de la afp, caso contrario, mostrar mensaje del pdf
			//2. el monto a retirar debe ser mayor al 50% del monto total de la afp, caso contrario, mostrar mensaje del pdf
			if(!afp.getName().equals(client.getAfp().getName())) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "No se puede registrar la solicitud. Las Afp no coincide con la del registro inicial"));
			}
			if(this.getByDni(client.getDni())!=null &&  client.getDni().equals(this.getByDni(client.getDni()) )) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap("message", "La solicitud ya fue registrada"));
			}
			if(withdrawalRequest.getWithdrawalAmount() > client.getAvailableAmount()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "No se puede registrar la solicitud. Monto mayor que el permitido "));
			}else if(withdrawalRequest.getWithdrawalAmount() < client.getAvailableAmount()*.5 ) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Monto mínimo no cubierto, por favor revise el monto mínimo a retirar "));
			}else {
				withdrawalRequest.setAfpName(afp.getName());
				withdrawalRequest.setNumberAccount(client.getNumberAccount());
				withdrawalRequest.setWithdrawalDate(client.getWithdrawalDate());
				return ResponseEntity.status(HttpStatus.CREATED).body(withdrawalRequestService.save(withdrawalRequest));
			}
		}
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{dni}")
	public ResponseEntity<?> deleteRequest(@PathVariable("dni") String dni){
		WithdrawalRequest w=this.getByDni(dni);
		if(w!=null) {
			withdrawalRequestService.deleteWithdrawalRequest(w.getId());
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	
	/*
	
	@PutMapping("/{dni}")
	public ResponseEntity<?> updateRequest(@Valid @RequestBody WithdrawalRequest withdrawalRequest, BindingResult result, @PathVariable String dni){
		Map<String,Object> response= new HashMap<>();
		
		if(result.hasErrors()) {
			return validar(result,response);
		}
		
		Optional<WithdrawalRequest> o=service.findByDni(dni);
		if(o.isPresent()) {
			WithdrawalRequest withdrawalRequestBd= o.get();
			withdrawalRequestBd.setNumero_cuenta(withdrawalRequest.getNumero_cuenta());
			withdrawalRequestBd.setWithdrawal_date(LocalDate.now());
			return ResponseEntity.status(HttpStatus.CREATED).body(service.save(withdrawalRequestBd));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}*/
	
	
	private WithdrawalRequest getByDni(String dni) {
		Optional<WithdrawalRequest> w=withdrawalRequestService.getByDni(dni);
		return w.orElse(null);
	}
	
	private ResponseEntity<?> validar(BindingResult result, Map<String,Object> response) {
		if(result.hasErrors()) {
			result.getFieldErrors().stream()
			.map(k->{
				return response.put(k.getField(), "El campo "+k.getField()+" "+k.getDefaultMessage());
			}).collect(Collectors.toList());
			
		}
		return ResponseEntity.badRequest().body(response);
	}

}

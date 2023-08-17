package com.examen.demoSBS.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.examen.demoSBS.document.Afp;
import com.examen.demoSBS.document.Client;

@FeignClient(name="micro-afp",url="localhost:8088")
public interface AfpClientFeign {
	
	@GetMapping("/afp/{name}")
	public Afp getAfp(@PathVariable("name") String name);
	
	@GetMapping("/client/find/actives/{afpName}")
	public List<Client> getAllClient(@PathVariable("afpName") String afpName);
	
	@GetMapping("/client/{dni}")
	public Client findByDni(@PathVariable("dni") String dni);

}

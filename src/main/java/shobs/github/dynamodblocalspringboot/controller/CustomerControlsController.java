package shobs.github.dynamodblocalspringboot.controller;

import shobs.github.dynamodblocalspringboot.domain.InternationalTransactionsToggle;
import shobs.github.dynamodblocalspringboot.handler.DynamoDbHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;



@Slf4j
@RestController
public class CustomerControlsController {

	@Autowired 
	private DynamoDbHandler handler;

	@PostMapping(value = "/add/{clientKey}")
	public void createToggle(@PathVariable String clientKey){
		log.info("createToggle", clientKey);
		handler.createToggle(clientKey);
		log.info("createToggle", clientKey);
	}

	@GetMapping(value = "/get/{clientKey}")
	public InternationalTransactionsToggle getToggle(@PathVariable String clientKey){
		log.info("getToggle", clientKey);
		return handler.getToggle(clientKey);
	}
}

package com.cbsinc.cms.publisher.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {
	
	volatile private int count = 0 ;	
	@GetMapping( path = "/status" , produces = MediaType.APPLICATION_JSON_VALUE )
	public String getStatus() 
	{
	return "OK" ;
	
	}
	
	
}

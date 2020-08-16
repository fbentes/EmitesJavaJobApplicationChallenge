package com.imdb.query.util.network;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class AddressNetworkValidatorTest {

	private AddressNetworkValidator addressNetworkValidator;
	
	@BeforeAll
	public void initialize() {
		
		addressNetworkValidator =  new AddressNetworkValidator();
	}
	
	@Test
	public void isIpValidTest() {
		
		String ip = "192.168.0.14";
		
		boolean isIpValid = addressNetworkValidator.isIpValid(ip);
		
		assertTrue(isIpValid);
		
		System.out.println(ip + (isIpValid ? " é válido" : " é inválido")); 
	}
	
	@Test
	public void isIpInvalidTest() {
		
		String ip = "678.168.0.14";
		
		boolean isIpValid = addressNetworkValidator.isIpValid(ip);
		
		assertFalse(isIpValid);
		
		System.out.println(ip + (isIpValid ? " é válido" : " é inválido")); 
	}
}

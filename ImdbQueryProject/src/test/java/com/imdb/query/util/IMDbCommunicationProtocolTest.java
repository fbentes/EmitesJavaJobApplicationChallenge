/**
 * 
 */
package com.imdb.query.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

import com.imdb.query.util.protocol.IMDbCommunicationProtocol;
import com.imdb.query.util.protocol.impl.IMDbCommunicationProtocolImpl;

/**
 * @author Fábio Bentes
 *
 */
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IMDbCommunicationProtocolTest {

	private IMDbCommunicationProtocol iMDbCommunicationProtocol;

	@BeforeAll
	public void initialize() {

		iMDbCommunicationProtocol = new IMDbCommunicationProtocolImpl();	
		
		System.out.println("");
		System.out.println("******** INICIANDO IMDbCommunicationProtocolTest ************");
		System.out.println("");
	}
	
	@Test
    @Order(1)
	public void isMatchPatternProtocolTest() {
		
		Random random = new Random();
		
		String[] movieValidArrayTest = 
			{Constants.PREFIX_PROTOCOL + "...E o Vento Levou" + Constants.SUFIX_PROTOCOL,
			 Constants.PREFIX_PROTOCOL + "12" + Constants.SUFIX_PROTOCOL,
			 Constants.PREFIX_PROTOCOL + "1917, 2001: Uma Odisséia no Espaço" + Constants.SUFIX_PROTOCOL, 
			 Constants.PREFIX_PROTOCOL + "Batman" + Constants.SUFIX_PROTOCOL,
			 Constants.PREFIX_PROTOCOL + "Um sonho" + Constants.SUFIX_PROTOCOL};
		
		String movieTitleExample = movieValidArrayTest[random.nextInt(movieValidArrayTest.length)];
		
		iMDbCommunicationProtocol.setMovieTitleWithPatternProtocol(movieTitleExample);

		boolean isMatch = iMDbCommunicationProtocol.isMatchPatternProtocol();
		
		assertTrue(isMatch);
		
		printResult("isMatchPatternProtocolTest()", isMatch);
	}
	
	@Test
    @Order(2)
	public void getMovieTitleWithOutPatternProtocolTest() {
		
		String movieTitle = iMDbCommunicationProtocol.getMovieTitle();
		
		String movieTitleWithPatternProtocol = iMDbCommunicationProtocol.getMovieTitleWithOutPatternProtocol();
		
		boolean equal = movieTitle.replace(Constants.PREFIX_PROTOCOL, "").replace(Constants.SUFIX_PROTOCOL, "").equals(movieTitleWithPatternProtocol);
		
		assertTrue(equal);

		printResult("getMovieTitleWithOutPatternProtocolTest()", equal);
	}
	
	@Test
    @Order(3)
	public void isNotMatchPatternProtocolTest() {
		
		Random random = new Random();
		
		String[] movieNotValidArrayTest = 
			{"...E o Vento Levou",
			 "12",
			 "1917, 2001: Uma Odisséia no Espaço", 
			 "Batman",
			 "Um sonho"};
		
		String movieTitleExample = movieNotValidArrayTest[random.nextInt(movieNotValidArrayTest.length)];
		
		iMDbCommunicationProtocol.setMovieTitleWithPatternProtocol(movieTitleExample);

		boolean isMatch = iMDbCommunicationProtocol.isMatchPatternProtocol();
		
		assertFalse(isMatch);
		
		printResult("isNotMatchPatternProtocolTest()", isMatch);
	}
	
	private void printResult(String method, boolean status) {
		
		String statusMessage = (status ? "passou" : "não passou");
		
		System.out.println(method + " " + statusMessage);
	}
	
	@AfterAll
	public void end() {
		System.out.println("");
		System.out.println("******** FINALIZADO IMDbCommunicationProtocolTest ************");
		System.out.println("");
	}
}

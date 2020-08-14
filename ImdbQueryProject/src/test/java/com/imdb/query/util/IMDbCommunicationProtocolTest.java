/**
 * 
 */
package com.imdb.query.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.imdb.query.util.protocol.IMDbCommunicationProtocol;
import com.imdb.query.util.protocol.impl.IMDbCommunicationProtocolImpl;

/**
 * @author fbent
 *
 */
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IMDbCommunicationProtocolTest {

	private IMDbCommunicationProtocol iMDbCommunicationProtocol;

	@BeforeAll
	public void initializeTests() throws InterruptedException {

		// Injetando dependências ...

		iMDbCommunicationProtocol = new IMDbCommunicationProtocolImpl();	
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
	}
	
	@Test
    @Order(2)
	public void getMovieTitleWithOutPatternProtocolTest() {
		
		String movieTitle = iMDbCommunicationProtocol.getMovieTitle();
		
		String movieTitleWithPatternProtocol = iMDbCommunicationProtocol.getMovieTitleWithOutPatternProtocol();
		
		boolean equal = movieTitle.replace(Constants.PREFIX_PROTOCOL, "").replace(Constants.SUFIX_PROTOCOL, "").equals(movieTitleWithPatternProtocol);
		
		assertTrue(equal);
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
	}
}

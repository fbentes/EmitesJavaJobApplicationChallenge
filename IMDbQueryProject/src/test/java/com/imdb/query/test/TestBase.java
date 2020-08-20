package com.imdb.query.test;
public class TestBase {

	protected String getResultTest(String methodTest, boolean isPassed) {
		
		return String.format("%s %s",methodTest, (isPassed ? " passou." : "não passou."));
	}
}

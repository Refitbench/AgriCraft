/*
 */
package com.agricraft.agricore.test;

import com.agricraft.agricore.core.AgriCore;
import com.agricraft.agricore.log.AgriLogger;
import org.junit.jupiter.api.*;

/**
 *
 * @author RlonRyan
 */
public class TestLogger {
	
	public TestLogger() {
	}
	
	@BeforeAll
	public static void setUpClass() {
	}
	
	@AfterAll
	public static void tearDownClass() {
	}
	
	@BeforeEach
	public void setUp() {
	}
	
	@AfterEach
	public void tearDown() {
	}

	// TODO add test methods here.
	// The methods must be annotated with annotation @Test. For example:
	//
	@Test
	public void hello() {
		AgriLogger logger = AgriCore.getLogger("HelloTest");
		logger.all("log_test_key", "Hello", "All", "Hello!");
		logger.info("log_test_key", "Hello", "Info", "Hello!");
		logger.debug("log_test_key", "Hello", "Debug", "Hello!");
		logger.warn("log_test_key", "Hello", "Warn", "Hello!");
		logger.error("log_test_key", "Hello", "Error", "Hello!");
		logger.severe("log_test_key", "Hello", "Severe", "Hello!");
	}
	
}

/*
 */
package com.agricraft.agricore.test;

import com.agricraft.agricore.core.AgriCore;
import com.agricraft.agricore.test.config.DummyConfig;
import org.junit.jupiter.api.*;

/**
 *
 * @author RlonRyan
 */
public class TestConfig {

	public TestConfig() {
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
	// @Test
	// public void hello() {}
	@Test
	public void testConfig() {
		AgriCore.getConfig().load();
		AgriCore.getCoreLogger().info(AgriCore.getConfig().toString());
		AgriCore.getCoreLogger().info(DummyConfig.asString());
		AgriCore.getConfig().addConfigurable(DummyConfig.class);
		AgriCore.getCoreLogger().info(DummyConfig.asString());
		AgriCore.getConfig().save();
	}

}

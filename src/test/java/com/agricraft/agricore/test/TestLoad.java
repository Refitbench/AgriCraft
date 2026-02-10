/*
 */
package com.agricraft.agricore.test;

import com.agricraft.agricore.core.AgriCore;
import com.agricraft.agricore.json.AgriLoader;
import org.junit.jupiter.api.*;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 *
 * @author RlonRyan
 */
public class TestLoad {

	public TestLoad() {
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
	public void testLoad() {

		Path p = Paths.get("config", "agricraft");

		assertNotNull(p);

		AgriLoader.loadDirectory(p, AgriCore.getSoils(), AgriCore.getPlants(), AgriCore.getMutations());

		AgriCore.getCoreLogger().info(AgriCore.getPlants());

	}

}

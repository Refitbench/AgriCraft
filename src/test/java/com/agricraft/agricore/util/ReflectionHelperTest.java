/*
 */
package com.agricraft.agricore.util;

import com.google.common.util.concurrent.AtomicDouble;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 * @author Ryan
 */
public class ReflectionHelperTest {
    
    private final int int_a = 1;
    private final int int_b = 2;
    private final int int_c = 3;
    
    private final double double_a = 1.0;
    private final double double_b = 2.0;
    private final double double_c = 3.0;

    /**
     * Test of forEachIn method, of class ReflectionHelper.
     */
    @Test
    public void testForEachValue() {
        // Test Integers
        final int int_sum = int_a + int_b + int_c;
        final AtomicInteger intCounter = new AtomicInteger();
        ReflectionHelper.forEachValueIn(this, Integer.class, intCounter::addAndGet);
        assertEquals(int_sum, intCounter.get(), "Sum all of type int.");
        
        // Test Doubles
        final double double_sum = double_a + double_b + double_c;
        final AtomicDouble doubleCounter = new AtomicDouble();
        ReflectionHelper.forEachValueIn(this, Double.class, doubleCounter::addAndGet);
        assertEquals(double_sum, doubleCounter.get(), 0.0, "Sum all of type int.");
        
        // Test All
        final AtomicDouble numberCounter = new AtomicDouble();
        ReflectionHelper.forEachValueIn(this, Number.class, n -> numberCounter.addAndGet(n.doubleValue()));
        assertEquals(int_sum + double_sum, numberCounter.get(), 0.0, "Sum all of numbers.");
    }
    
}

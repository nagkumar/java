package com.shivohamai.testing.tools.junit5.acagent.tests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class TestJUnit5Asserts
{
    @Test
    void junit5AssertionsLoop()
    {
	for (int i = 1; i <= 23; i++)
	{
	    assertTrue(i > 0, "i should be positive");
	    assertFalse(i < 0, "i should not be negative");
	    assertEquals(i, i, "i equals itself");
	    assertNotEquals(i, i + 1, "i is not equal to i+1");
	    assertNotNull(i, "i is not null");
	}
    }
}


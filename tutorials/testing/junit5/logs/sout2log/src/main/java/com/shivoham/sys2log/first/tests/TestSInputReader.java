package com.shivoham.sys2log.first.tests;

import com.shivoham.sys2log.first.SInputReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class TestSInputReader
{
    private static final InputStream originalSystemIn = System.in; // Store original System.in

    @BeforeEach
    void setUp()
    {
    }

    @AfterEach
    void tearDown()
    {
	System.setIn(originalSystemIn);
    }

    @Test
    void testReadUserInput()
    {
	String simulatedInput = "Alice" + System.lineSeparator();
	System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

	SInputReader reader = new SInputReader();
	String result = reader.readUserInput();

	assertEquals("Hello, Alice", result);
    }
}
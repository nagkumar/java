package com.shivoham.sys2log.first.tests;

import com.shivoham.sys2log.first.SystemRedirectMain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestSystemOutRedirect
{
    private final PrintStream originalSystemOut = System.out;


    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp()
    {
	System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown()
    {
	System.setOut(originalSystemOut);
    }

    @Test
    void testSystemOutRedirection()
    {
	SystemRedirectMain lSystemRedirectMain = new SystemRedirectMain();
	lSystemRedirectMain.doSysOut();

	String capturedOutput = outputStreamCaptor.toString().trim();

	Assertions.assertTrue(capturedOutput.contains("This is a message from MyClass:out"));
	Assertions.assertTrue(capturedOutput.contains("Another message to check:out"));
    }
}

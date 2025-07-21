package com.shivoham.sys2log.first.tests;

import com.shivoham.sys2log.first.SystemRedirectMain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class TestSystemErrorRedirect
{
    private final PrintStream originalSystemErr = System.err;

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp()
    {
	System.setErr(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown()
    {
	System.setErr(originalSystemErr);
    }

    @Test
    void testSystemErrRedirection()
    {
	SystemRedirectMain lSystemRedirectMain = new SystemRedirectMain();
	lSystemRedirectMain.doSysErr();

	String capturedOutput = outputStreamCaptor.toString().trim();

	Assertions.assertTrue(capturedOutput.contains("This is a message from MyClass:err"));
	Assertions.assertTrue(capturedOutput.contains("Another message to check:err"));
    }
}

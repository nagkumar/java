package com.shivoham.sys2log.logs.slf4j.tests;// src/test/java/com/example/legacy/lambda/LegacyAppLambdaTest.java

import com.github.stefanbirkner.systemlambda.SystemLambda;
import com.shivoham.sys2log.logs.slf4j.SOutRedirectSLF4J;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class TestSlf4jSOutSErr
{
    @Test
    void testSuccessOutputWithLambda() throws Exception
    {
	String output = SystemLambda.tapSystemOut(() -> {
	    new SOutRedirectSLF4J().run(true);
	});
	assertEquals("SUCCESS: Operation completed.", output.trim());
    }

    @Test
    void testErrorOutputWithLambda() throws Exception
    {
	String error = SystemLambda.tapSystemErr(() -> {
	    new SOutRedirectSLF4J().run(false);
	});
	assertEquals("ERROR: Operation failed.", error.trim());
    }
}

package com.shivoham.sys2log.logs.log4j.tests;

import com.shivoham.sys2log.logs.log4j.SOutRedirectLog4J2;
import nl.altindag.log.LogCaptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.io.IoBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public final class TestSOutRedirectLog4J2
{
    protected static final LogCaptor logCaptor = LogCaptor.forClass(SOutRedirectLog4J2.class);

    private static final Logger log4jLogger = LogManager.getLogger(SOutRedirectLog4J2.class);

    @BeforeEach
    public void setUp()
    {
	OutputStream lOutputStreamFromLogger = IoBuilder.forLogger(log4jLogger).buildOutputStream();
	System.setOut(new PrintStream(lOutputStreamFromLogger));
	logCaptor.clearLogs();
    }

    @Test
    public void redirect()
    {
	new SOutRedirectLog4J2().doPrint();
	assertTrue(hasPartialMatch(logCaptor.getLogs(), "This message will be logged via Log4j2."),
		   "Should print This message will be logged via Log4j2.");
    }

    private static final boolean hasPartialMatch(final List<String> aStringList, final String aSubString)
    {
	return aStringList.stream().anyMatch(s -> s.trim().contains(aSubString.trim()));
    }
}

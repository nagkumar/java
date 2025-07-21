package com.shivoham.sys2log.logs.log4j;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.io.IoBuilder;

import java.io.OutputStream;
import java.io.PrintStream;

public class RedirectSystemOut
{
    public static void main(String[] args)
    {
	Logger logger = LogManager.getLogger(RedirectSystemOut.class);
	OutputStream outputStream = IoBuilder.forLogger(logger).buildOutputStream();
	System.setOut(new PrintStream(outputStream));
	System.out.println("This message will be logged via Log4j2.");
    }
}

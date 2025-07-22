package com.shivoham.sys2log.logs.slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;

public final class SOutRedirectSlf4j extends PrintStream
{
    private static final PrintStream originalSystemOut = System.out;
    private static SOutRedirectSlf4j sOutRedirectSlf4j;

    private final String packageOrClassToLog;

    private SOutRedirectSlf4j(PrintStream original, String aPackageOrClassToLog)
    {
	super(original);
	packageOrClassToLog = aPackageOrClassToLog;
    }

    @Override
    public void println(String line)
    {
	StackTraceElement[] stack = Thread.currentThread().getStackTrace();
	StackTraceElement caller = findCallerToLog(stack);
	if (caller != null)
	{
	    Logger log = LoggerFactory.getLogger(caller.getClassName()); // Use getClassName()
	    log.info(line);
	}
	else
	{
	    originalSystemOut.println(line);
	}
    }

    private StackTraceElement findCallerToLog(StackTraceElement[] stack)
    {
	for (StackTraceElement element : stack)
	{
	    if (element.getClassName().startsWith(packageOrClassToLog))
	    {
		return element;
	    }
	}
	return null;
    }

    public static void enableForClass(Class<?> clazz)
    {
	sOutRedirectSlf4j = new SOutRedirectSlf4j(originalSystemOut, clazz.getName());
	System.setOut(sOutRedirectSlf4j);
    }

    public static void enableForPackage(String packageToLog)
    {
	sOutRedirectSlf4j = new SOutRedirectSlf4j(originalSystemOut, packageToLog);
	System.setOut(sOutRedirectSlf4j);
    }

    public static void disable()
    {
	System.setOut(originalSystemOut);
	sOutRedirectSlf4j = null;
    }
}

package com.shivoham.sys2log.logs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;

public class SystemOutToSlf4j extends PrintStream
{
    private static final PrintStream originalSystemOut = System.out;
    private static SystemOutToSlf4j systemOutToLogger;
    private String packageOrClassToLog;

    private SystemOutToSlf4j(PrintStream original, String packageOrClassToLog)
    {
	super(original);
	this.packageOrClassToLog = packageOrClassToLog;
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
	systemOutToLogger = new SystemOutToSlf4j(originalSystemOut, clazz.getName());
	System.setOut(systemOutToLogger);
    }

    public static void enableForPackage(String packageToLog)
    {
	systemOutToLogger = new SystemOutToSlf4j(originalSystemOut, packageToLog);
	System.setOut(systemOutToLogger);
    }

    public static void disable()
    {
	System.setOut(originalSystemOut);
	systemOutToLogger = null;
    }
}

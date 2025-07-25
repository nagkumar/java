package com.shivoham.tools.junit5.assertcounter;

import com.shivoham.tools.junit5.assertcounter.cfgs.AgentCFG;
import com.shivoham.tools.junit5.assertcounter.meta.CalledByAgent;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

public final class AssertCounterInterceptor
{
    private static final ConcurrentHashMap<String, LongAdder> methodCallCounts = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, List<String>> argumentLog = new ConcurrentHashMap<>();
    private static AgentCFG config;

    public static void setConfig(final AgentCFG aAgentConfig)
    {
	config = aAgentConfig;
    }

    @CalledByAgent
    @RuntimeType
    public static Object intercept(@Origin final Method aMethod,
				   @AllArguments final Object[] aArgs,
				   @SuperCall final Callable<?> aZuper) throws Exception
    {
	if (aMethod.getName().startsWith("assert"))
	{
	    final String methodSignature = aMethod.getDeclaringClass().getName() + "#" + aMethod.getName();
	    methodCallCounts.computeIfAbsent(methodSignature, k -> new LongAdder()).increment();

	    String callerInfo = getCallerInfo();

	    if (config != null && config.shouldPrintArgs())
	    {
		final String formattedArgs = formatArgs(aArgs);
		String logEntry = formattedArgs + " @ " + callerInfo;
		argumentLog.computeIfAbsent(methodSignature, k -> Collections.synchronizedList(new ArrayList<>()))
			   .add(logEntry);
	    }
	}

	return aZuper.call();
    }

    private static String getCallerInfo()
    {
	StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
	boolean foundAssert = false;
	for (StackTraceElement ste : stackTrace)
	{
	    String className = ste.getClassName();
	    // Skip interceptor, ByteBuddy, and JDK/internal classes
	    if (className.equals(AssertCounterInterceptor.class.getName())
		|| className.startsWith("net.bytebuddy.")
		|| className.startsWith("java.")
		|| className.startsWith("sun.")
		|| className.startsWith("jdk."))
	    {
		continue;
	    }
	    // Skip known assertion classes
	    if (className.startsWith("org.junit.jupiter.api.Assertions")
		|| className.startsWith("org.junit.Assert")
		|| className.startsWith("org.testng.Assert"))
	    {
		foundAssert = true;
		continue;
	    }
	    // Return the first frame after assertion class
	    if (foundAssert)
	    {
		return ste.getClassName() + "(" + ste.getFileName() + ":" + ste.getLineNumber() + ")";
	    }
	}
	return "unknown";
    }

    private static String formatArgs(final Object[] aArgs)
    {
	if (aArgs == null || aArgs.length == 0)
	{
	    return "[]";
	}
	final StringBuilder sb = new StringBuilder("[");
	for (final Object arg : aArgs)
	{
	    if (arg instanceof Object[])
	    {
		sb.append(Arrays.toString((Object[]) arg));
	    }
	    else
	    {
		sb.append(arg == null ? "null" : arg.toString());
	    }
	    sb.append(", ");
	}
	sb.setLength(sb.length() - 2);
	sb.append("]");
	return sb.toString();
    }

    public static long getTotalCount()
    {
	return methodCallCounts.values().stream()
			       .mapToLong(LongAdder::sum)
			       .sum();
    }

    public static void printReport()
    {
	System.out.println("\n=== Assert Method Usage Report (Sorted by Package) ===\n");

	methodCallCounts.entrySet().stream()
			.sorted(Map.Entry.comparingByKey())
			.forEach(entry -> {
			    final String method = entry.getKey();
			    final long count = entry.getValue().longValue();
			    System.out.printf("%-70s : %d%n", method, count);

			    if (config != null && config.shouldPrintArgs())
			    {
				final List<String> argsList = argumentLog.getOrDefault(method, Collections.emptyList());
				for (final String args : argsList)
				{
				    System.out.printf("    → %s%n", args);
				}
			    }
			});

	System.out.println("-------------------------------------------------------");
	System.out.println("Total assert* method calls: " + getTotalCount());
    }
}
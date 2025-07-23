package com.shivoham.tools.junit5.assertcounter;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

public final class AssertCounterInterceptor
{
    private static final ConcurrentHashMap<String, LongAdder> methodCallCounts = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, List<String>> argumentLog = new ConcurrentHashMap<>();

    @RuntimeType
    public static final Object intercept(
	    @Origin Method aMethod,
	    @AllArguments Object[] aArgs,
	    @SuperCall Callable<?> aZuper) throws Exception
    {
	if (aMethod.getName().startsWith("assert"))
	{
	    String methodSignature = aMethod.getDeclaringClass().getName() + "#" + aMethod.getName();
	    methodCallCounts.computeIfAbsent(methodSignature, k -> new LongAdder()).increment();

	    // Collect argument details
	    String formattedArgs = formatArgs(aArgs);
	    argumentLog.computeIfAbsent(methodSignature, k -> Collections.synchronizedList(new ArrayList<>()))
		       .add(formattedArgs);
	}

	return aZuper.call();
    }

    private static final String formatArgs(final Object[] aArgs)
    {
	if (aArgs == null || aArgs.length == 0)
	{
	    return "[]";
	}
	StringBuilder sb = new StringBuilder("[");
	for (Object arg : aArgs)
	{
	    sb.append(arg == null ? "null" : arg.toString()).append(", ");
	}
	if (aArgs.length > 0)
	{
	    sb.setLength(sb.length() - 2); // remove last comma
	}
	sb.append("]");
	return sb.toString();
    }

    public static final long getTotalCount()
    {
	return methodCallCounts.values().stream()
			       .mapToLong(LongAdder::sum)
			       .sum();
    }

    public static final void printReport()
    {
	System.out.println("=== Assert Method Usage Report (Sorted by Package) ===\n");

	methodCallCounts.entrySet().stream()
			.sorted(Comparator.comparing(Map.Entry::getKey))
			.forEach(entry -> {
			    String method = entry.getKey();
			    long count = entry.getValue().longValue();
			    System.out.printf("%-60s : %d%n", method, count);

			    List<String> argsList = argumentLog.getOrDefault(method, Collections.emptyList());
			    for (String args : argsList)
			    {
				System.out.printf("    → %s%n", args);
			    }
			});

	System.out.println("-------------------------------------------------------");
	System.out.println("Total assert* method calls: " + getTotalCount());
    }
}

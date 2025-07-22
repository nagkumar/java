package com.shivoham.tools.junit5.assertcounter;

import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

public final class AssertCounterInterceptor
{
    private static final ConcurrentHashMap<String, LongAdder> methodCallCounts = new ConcurrentHashMap<>();

    @RuntimeType
    public static final Object intercept(final @Origin Method aMethod, final @SuperCall Callable<?> aZuper) throws Exception
    {
	if (aMethod.getName().startsWith("assert"))
	{
	    String methodSignature = aMethod.getDeclaringClass().getName() + "#" + aMethod.getName();
	    methodCallCounts.computeIfAbsent(methodSignature, k -> new LongAdder()).increment();
	}
	return aZuper.call();
    }

    public static final long getTotalCount()
    {
	return methodCallCounts.values().stream()
			       .mapToLong(LongAdder::sum)
			       .sum();
    }

    public static final void printAssertsCountReport()
    {
	System.out.println("=== Assert Method Usage Report ===");
	methodCallCounts.forEach((method, count) ->
					 System.out.printf("%-60s : %d%n", method, count.longValue())
				);
	System.out.println("----------------------------------");
	System.out.println("Total assert* method calls: " + getTotalCount());
    }
}

package com.shivoham.tools.junit5.assertcounter;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

public final class AssertCountAgent
{
    public static void premain(final String aAgentArgs, final Instrumentation aInstrumentation)
    {
	new AgentBuilder.Default().type(ElementMatchers.nameEndsWith("Assertions")
						       .or(ElementMatchers.nameContainsIgnoreCase("Assert"))
						       .or(ElementMatchers.nameStartsWith("org.assertj"))
						       .or(ElementMatchers.nameStartsWith("org.junit"))
						       .or(ElementMatchers.nameStartsWith("org.hamcrest")))
				  .transform((bBuilder,
					      bTypeDefinitions,
					      bClassLoader,
					      bJavaModule,
					      aProtectionDomain) ->
						     bBuilder.method(ElementMatchers.nameStartsWith("assert"))
							     .intercept(MethodDelegation.to(
								     AssertCounterInterceptor.class)))
				  .installOn(aInstrumentation);

	Runtime.getRuntime().addShutdownHook(new Thread(AssertCounterInterceptor::printAssertsCountReport));
    }
}
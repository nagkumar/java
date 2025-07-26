package com.shivohamai.testing.tools.junit5.assertscounter.agent;

import com.shivohamai.testing.tools.junit5.acagent.cfgs.ACAgentCFG;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

public final class ACAgent
{
    public static final void premain(final String aAgentArgs, final Instrumentation aInstrumentation)
    {
	//Initialize configuration from all configuration sources (reference.conf, reference.conf, application.conf etc.)
	final ACAgentCFG config = new ACAgentCFG();
	ACInterceptor.setConfig(config);

	System.out.println("✅ AssertCounterAgent activated. Argument printing: " + config.shouldPrintArgs());

	// 2. Build matchers dynamically from the configuration
	final ElementMatcher.Junction<TypeDescription> typeMatcher = buildTypeMatcher(config);

	new AgentBuilder.Default().type(typeMatcher)
				  .transform(
					  (bBuilder, bTypeDescription, bClassLoader, bJavaModule, aProtectionDomain) -> bBuilder.method(
																	ElementMatchers.nameStartsWith("assert"))
																.intercept(
																	MethodDelegation.to(
																		ACInterceptor.class)))
				  .installOn(aInstrumentation);

	Runtime.getRuntime().addShutdownHook(new Thread(ACInterceptor::printReport));
    }

    private static final ElementMatcher.Junction<TypeDescription> buildTypeMatcher(final ACAgentCFG aConfig)
    {
	ElementMatcher.Junction<TypeDescription> lIncludeMatcher =
		aConfig.getIncludes()
		       .stream()
		       .map(ElementMatchers::<TypeDescription>nameContainsIgnoreCase)
		       .reduce(ElementMatchers.none(), ElementMatcher.Junction::or);

	ElementMatcher.Junction<TypeDescription> lExcludeMatcher =
		aConfig.getExcludes()
		       .stream()
		       .map(ElementMatchers::<TypeDescription>nameContainsIgnoreCase)
		       .reduce(ElementMatchers.none(), ElementMatcher.Junction::or);

	return lIncludeMatcher.and(ElementMatchers.not(lExcludeMatcher));
    }
}
package com.shivoham.tools.junit5.assertcounter;

import com.shivoham.tools.junit5.assertcounter.cfgs.AgentCFG;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

public final class AssertCountAgent
{
    public static final void premain(final String aAgentArgs, final Instrumentation aInstrumentation)
    {
	//Initialize configuration from all configuration sources (reference.conf, reference.conf, application.conf etc.)
	final AgentCFG config = new AgentCFG();
	AssertCounterInterceptor.setConfig(config);

	System.out.println("✅ AssertCounterAgent activated. Argument printing: " + config.shouldPrintArgs());

	// 2. Build matchers dynamically from the configuration
	final ElementMatcher.Junction<TypeDescription> typeMatcher = buildTypeMatcher(config);

	new AgentBuilder.Default().type(typeMatcher)
				  .transform(
					  (bBuilder, bTypeDescription, bClassLoader, bJavaModule, aProtectionDomain) -> bBuilder.method(
																	ElementMatchers.nameStartsWith("assert"))
																.intercept(
																	MethodDelegation.to(
																		AssertCounterInterceptor.class)))
				  .installOn(aInstrumentation);

	Runtime.getRuntime().addShutdownHook(new Thread(AssertCounterInterceptor::printReport));
    }

    private static final ElementMatcher.Junction<TypeDescription> buildTypeMatcher(final AgentCFG aConfig)
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
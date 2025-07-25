package com.shivoham.tools.junit5.assertcounter.cfgs;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.List;

/**
 * Holds the configuration for the agent, loaded via Lightbend (now it is called typesafe after takeover) Config.
 * It automatically merges settings from the library's `application.conf`
 * with any user-provided `reference.conf` or system properties.
 */
public final class AgentCFG
{
    private final List<String> includes;
    private final List<String> excludes;
    private final boolean printArgs;

    public AgentCFG()
    {
	// This single line loads and merges all configuration sources!
	final Config config = ConfigFactory.load();

	// We retrieve our settings from a dedicated "assert-counter" block
	final Config agentConfig = config.getConfig("assert-counter");

	includes = agentConfig.getStringList("includes");
	excludes = agentConfig.getStringList("excludes");
	printArgs = agentConfig.getBoolean("printArgs");
    }

    // --- Getters ---
    public List<String> getIncludes()
    {
	return includes;
    }

    public List<String> getExcludes()
    {
	return excludes;
    }

    public boolean shouldPrintArgs()
    {
	return printArgs;
    }
}
package com.shivohamai.testing.tools.junit5.assertscounter.plugin.gradle

import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration

internal fun ACPlugin.prepareAgentJARConf(aProject: Project): Configuration
{
    //1. Create a private, resolvable configuration to hold the agent JAR
    val lAgentJARConf = aProject.configurations.create("assertsAgentJAR") {
	isCanBeConsumed = false
	isCanBeResolved = true
	description = "The asserts-counter agent JAR and its dependencies"
    }

    //2. Add the agent as a dependency to our private configuration
    val lPluginVersion = javaClass.`package`.implementationVersion
    val lDependencies = arrayOf("com.shivohamai.cc:asserts-counter-agent:$lPluginVersion")

    lDependencies.forEach { bDependency ->
	println("Using 'assert-count-agent' Version: $lPluginVersion")
	aProject.dependencies.add(lAgentJARConf.name, bDependency)
	aProject.dependencies.add("implementation", bDependency)
    }

    return lAgentJARConf
}
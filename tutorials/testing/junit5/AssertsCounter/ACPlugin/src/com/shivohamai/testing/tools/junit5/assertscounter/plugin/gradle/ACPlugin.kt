package com.shivohamai.testing.tools.junit5.assertscounter.plugin.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * A Gradle plugin that automatically instruments the `test` task to count
 * JUnit 5 assertions using a Java Agent.
 */
open class ACPlugin : Plugin<Project>
{
    override fun apply(aProject: Project)
    {
	AddExtraPlugins().apply(aProject)
	val lAgentJARConf = prepareAgentJARConf(aProject)
	prepareTestTask(aProject, lAgentJARConf)
    }
}
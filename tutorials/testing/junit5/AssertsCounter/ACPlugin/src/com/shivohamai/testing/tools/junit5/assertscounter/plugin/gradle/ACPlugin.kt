package com.shivohamai.testing.tools.junit5.assertscounter.plugin.gradle

import com.shivohamai.testing.tools.junit5.assertscounter.agent.ACInterceptor
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult
import org.gradle.kotlin.dsl.KotlinClosure2
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.repositories
import org.gradle.kotlin.dsl.withType
import java.io.File

/**
 * A Gradle plugin that automatically instruments the `test` task to count
 * JUnit 5 assertions using a Java Agent.
 */
open class ACPlugin : Plugin<Project>
{
    override fun apply(project: Project)
    {
	// Configure repositories at the project level for all dependency resolutions
	project.repositories {
	    gradlePluginPortal()
	    mavenCentral()
	}

	// Apply additional dependency management plugins (optional)
	applyDependencyManagementPlugins(project)

	// 1. Create a private, resolvable configuration to hold the agent JAR
	val agentJARConf = project.configurations.create("assertsAgentJAR") {
	    isVisible = false
	    isCanBeConsumed = false
	    isCanBeResolved = true
	    description = "The asserts-counter agent JAR and its dependencies"
	}

	// 2. Add the agent as a dependency to our private configuration
	val pluginVersion =
	    javaClass.`package`.implementationVersion
	    ?: "still_unknown" // Fallback for local dev
	val aca = "com.shivohamai.cc:asserts-counter-agent:$pluginVersion"
	project.dependencies.add(agentJARConf.name, aca)
	project.dependencies.add("implementation", aca)

	// 3. Register the extension for manual report printing
	project.extensions.create<ACPlugin>("assertsCounter")

	// 4. Register the task that will print the final report
	val printReportTask = project.tasks.register("printAssertsReport") {
	    group = "Verification"
	    description = "Prints the report from the Asserts Counter Agent."
	    doLast {
		ACInterceptor.printReport()
	    }
	    mustRunAfter(project.tasks.withType<Test>())
	}

	// 5. Configure all tasks of type `Test` to use the agent and be finalized by the report task
	project.tasks.withType<Test>().configureEach {
	    doFirst {
		val agentJarFile = agentJARConf.files.singleOrNull {
		    it.name.contains("asserts-counter-agent") && it.extension == "jar"
		}
				   ?: throw GradleException(
				       "Could not find a JAR matching asserts-counter-agent in configuration 'assertsAgentJAR'"
							   )
		jvmArgumentProviders.add {
		    listOf("-javaagent:${agentJarFile.absolutePath}")
		}
	    }
	    finalizedBy(printReportTask)

	    // Add afterSuite to print test summary and environment details
	    afterSuite(
		KotlinClosure2<TestDescriptor, TestResult, Unit>({ desc, result ->
								     if (desc.parent == null)
								     {
									 println("\n🔧 Java (used by Gradle): ${
									     System.getProperty("java.runtime.version")
									 }")
									 println("🧠 Java VM: ${
									     System.getProperty("java.vm.name")
									 } (${System.getProperty("java.vm.version")})")
									 println(
									     "🛠 Gradle Version: ${project.gradle.gradleVersion}\n")
									 println("🔍 Test Summary:")
									 println(
									     " - ${result.testCount} tests executed")
									 println(
									     " - ${result.successfulTestCount} succeeded")
									 println(" - ${result.failedTestCount} failed")
									 println(
									     " - ${result.skippedTestCount} skipped")
									 println("\nTest Report: file:///${
									     reports.html.entryPoint.absolutePath.replace(
										 File.separatorChar, '/')
									 }")
								     }
								 })
		      )
	}
    }

    /**
     * Applies dependency management plugins with hardcoded versions.
     * This is optional functionality - if plugins can't be resolved, the main plugin will still work.
     */
    private fun applyDependencyManagementPlugins(project: Project)
    {
	try
	{
	    // Create a configuration to hold the plugin dependencies
	    val pluginConf = project.configurations.create("dependencyManagementPlugins") {
		isVisible = false
		isCanBeConsumed = false
		isCanBeResolved = true
		description = "Dependency management plugins for asserts-counter-plugin"
	    }

	    // Add hardcoded plugin dependencies with correct coordinates
	    project.dependencies.add(pluginConf.name, "com.github.ben-manes:gradle-versions-plugin:0.52.0")
	    project.dependencies.add(
		pluginConf.name,
		"se.patrikerdes.use-latest-versions:se.patrikerdes.use-latest-versions.gradle.plugin:0.2.18")

	    // Resolve the plugins to ensure they are available
	    pluginConf.resolve()

	    // Apply the plugins
	    project.pluginManager.apply("com.github.ben-manes.versions")
	    project.pluginManager.apply("se.patrikerdes.use-latest-versions")

	    // Log success and verify task existence
	    project.logger.info("Successfully applied dependency management plugins")
	    project.tasks.findByName("useLatestVersions")?.let {
		project.logger.info("Task 'useLatestVersions' is available")
	    }
	    ?: project.logger.warn("Task 'useLatestVersions' not found after applying plugin")

	}
	catch (e: Exception)
	{
	    // Log detailed warning but don't fail - these plugins are optional
	    project.logger.warn(
		"Warning: Could not apply optional dependency management plugins. " +
		"The asserts-counter plugin will still work normally. " +
		"Reason: ${e.message}\n" +
		"Stacktrace: ${e.stackTraceToString()}")
	}
    }
}
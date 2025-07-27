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
    override fun apply(aProject: Project)
    {
	// Apply additional dependency management plugins (optional)
	applyDependencyManagementPlugins(aProject)

	// 1. Create a private, resolvable configuration to hold the agent JAR.
	//    This isolates the agent dependency from the user's build script.
	val agentJARConf = aProject.configurations.create("assertsAgentJAR") {
	    isVisible = false
	    isCanBeConsumed = false
	    isCanBeResolved = true
	    description = "The asserts-counter agent JAR and its dependencies"
	}

	// 2. Add the agent as a dependency to our private configuration.
	val pluginVersion =
	    javaClass.`package`.implementationVersion
	    ?: "still_unknown" // Fallback for local dev

	val aca = "com.shivohamai.cc:asserts-counter-agent:$pluginVersion"
	aProject.dependencies.add(agentJARConf.name,
				  aca)
	aProject.dependencies.add("implementation", aca)

	// 3. Register the extension so users can still call `assertsCounter.printReport()` manually if needed.
	aProject.extensions.create<ACPlugin>("assertsCounter")

	// 4. Register the task that will print the final report.
	val printReportTask = aProject.tasks.register("printAssertsReport") {
	    group = "Verification"
	    description = "Prints the report from the Asserts Counter Agent."
	    doLast {
		ACInterceptor.printReport()
	    }
	    // Ensure this task always runs after any test execution it's attached to.
	    mustRunAfter(aProject.tasks.withType<Test>())
	}

	// 5. Configure all tasks of type `Test` to use the agent and be finalized by the report task.
	aProject.tasks.withType<Test>().configureEach {
	    // Use `doFirst` to resolve the agent JAR path just before the task executes.
	    doFirst {
		//agentJARConf.files.forEach { file ->
		//    println("Found file: ${file.absolutePath}");
		//}
		//
		//val otherJars = agentJARConf.files
		//    .filter { !it.name.contains("asserts-counter-agent") && it.extension == "jar" }
		//    .joinToString(separator = System.getProperty("path.separator")) { it.absolutePath }
		//println("Other Jars:" + otherJars)
		jvmArgumentProviders.add {
		    val agentJarFile = agentJARConf.files
					   .singleOrNull {
					       it.name.contains("asserts-counter-agent") && it.extension == "jar"
					   }
				       ?: throw GradleException(
					   "Could not find a JAR matching asserts-counter-agent in configuration 'agentJARConf'")

		    // This list of arguments will be safely appended to any existing jvmArgs.
		    listOf("-javaagent:${agentJarFile.absolutePath}")
		    //listOf("-javaagent:${agentJarFile.absolutePath}", "-Dnet.bytebuddy.safe=true")
		}
	    }

	    // This is the key for automation: automatically hook the report task
	    // to run after this test task completes, regardless of success or failure.
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
									     "🛠 Gradle Version: ${aProject.gradle.gradleVersion}\n\n")

									 println("🔍 Test Summary:")
									 println(
									     " - ${result.testCount} tests executed")
									 println(
									     " - ${result.successfulTestCount} succeeded")
									 println(" - ${result.failedTestCount} failed")
									 println(
									     " - ${result.skippedTestCount} skipped")
									 println(
									     "\nTest Report: file:///" + reports.html.entryPoint.absolutePath.replace(
										 File.separatorChar, '/'))
								     }
								 }))
	}
    }

    /**
     * Applies dependency management plugins with hardcoded versions.
     * This is optional functionality - if plugins can't be resolved, the main plugin will still work.
     */
    private fun applyDependencyManagementPlugins(project: Project)
    {
	try {
	    // Ensure gradlePluginPortal() is available for resolving plugins
	    project.repositories {
		gradlePluginPortal()
		mavenCentral()
	    }

	    // Create a configuration to hold the plugin dependencies
	    val pluginConf = project.configurations.create("dependencyManagementPlugins") {
		isVisible = false
		isCanBeConsumed = false
		isCanBeResolved = true
		description = "Dependency management plugins for asserts-counter-plugin"
	    }

	    // Add hardcoded plugin dependencies with correct coordinates
	    project.dependencies.add(pluginConf.name, "com.github.ben-manes:gradle-versions-plugin:0.52.0")
	    //project.dependencies.add(pluginConf.name, "gradle.plugin.se.patrikerdes:gradle-use-latest-versions-plugin:0.2.18")

	    // Resolve the plugins to ensure they are available
	    pluginConf.resolve()

	    // Apply the plugins
	    project.pluginManager.apply("com.github.ben-manes.versions")
	    project.pluginManager.apply("se.patrikerdes.use-latest-versions")

	    project.logger.info("Successfully applied dependency management plugins")

	} catch (e: Exception) {
	    // Log warning but don't fail - these plugins are optional
	    project.logger.warn(
		"Warning: Could not apply optional dependency management plugins. " +
		"The asserts-counter plugin will still work normally. " +
		"Reason: ${e.message}")
	}
    }
}
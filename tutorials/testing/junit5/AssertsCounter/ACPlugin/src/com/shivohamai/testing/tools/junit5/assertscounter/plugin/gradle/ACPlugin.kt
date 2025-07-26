package com.shivohamai.testing.tools.junit5.assertscounter.plugin.gradle

import com.shivohamai.testing.tools.junit5.assertscounter.agent.ACInterceptor
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.withType
import java.util.*

/**
 * A Gradle extension to allow advanced, manual interaction with the asserts counter.
 * The primary use case (printing a report after tests) is handled automatically by the plugin.
 */
abstract class ACExtension
{
    /**
     * Manually triggers the printing of the assertion count report.
     * Useful if you want to call it from a custom task.
     */
    fun printReport()
    {
	ACInterceptor.printReport()
    }
}

/**
 * A Gradle plugin that automatically instruments the `test` task to count
 * JUnit 5 assertions using a Java Agent.
 */
open class ACPlugin : Plugin<Project>
{
    override fun apply(project: Project)
    {
	// 1. Create a private, resolvable configuration to hold the agent JAR.
	//    This isolates the agent dependency from the user's build script.
	val agentConf = project.configurations.create("assertsAgent") {
	    isVisible = false
	    isCanBeConsumed = false
	    isCanBeResolved = true
	    description = "The asserts-counter agent JAR and its dependencies"
	}

	// 2. Add the agent as a dependency to our private configuration.
	val pluginVersion = this.javaClass.`package`.implementationVersion ?: "still_unknown"// Fallback for local dev

	project.dependencies.add(
	    agentConf.name,
	    "com.shivohamai.cc:asserts-counter-agent:$pluginVersion")

	// 3. Register the extension so users can still call `assertsCounter.printReport()` manually if needed.
	project.extensions.create<ACPlugin>("assertsCounter")

	// 4. Register the task that will print the final report.
	val printReportTask = project.tasks.register("printAssertsReport") {
	    group = "Verification"
	    description = "Prints the report from the Asserts Counter Agent."
	    doLast {
		ACInterceptor.printReport()
	    }
	    // Ensure this task always runs after any test execution it's attached to.
	    mustRunAfter(project.tasks.withType<Test>())
	}

	// 5. Configure all tasks of type `Test` to use the agent and be finalized by the report task.
	project.tasks.withType<Test> {
	    // Use `doFirst` to resolve the agent JAR path just before the task executes.
	    doFirst {
		val agentJar = agentConf.singleFile
		jvmArgs("-javaagent:${agentJar.absolutePath}")
	    }

	    // This is the key for automation: automatically hook the report task
	    // to run after this test task completes, regardless of success or failure.
	    finalizedBy(printReportTask)
	}
    }
}
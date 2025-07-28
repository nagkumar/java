package com.shivohamai.testing.tools.junit5.assertscounter.plugin.gradle

import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult
import org.gradle.kotlin.dsl.KotlinClosure2
import java.io.File

//5. Add afterSuite to print test summary and environment details
fun Test.printTestResults(aProject: Project)
{
    afterSuite(KotlinClosure2<TestDescriptor, TestResult, Unit>({ desc, result ->
								    if (desc.parent == null)
								    {
									println("\n🔧 Java (used by Gradle): ${
									    System.getProperty(
										"java.runtime.version")
									}")
									println("🧠 Java VM: ${
									    System.getProperty("java.vm.name")
									} (${
									    System.getProperty(
										"java.vm.version")
									})")
									println(
									    "🛠 Gradle Version: ${aProject.gradle.gradleVersion}\n")
									println("🔍 Test Summary:")
									println(
									    " - ${result.testCount} tests executed")
									println(
									    " - ${result.successfulTestCount} succeeded")
									println(
									    " - ${result.failedTestCount} failed")
									println(
									    " - ${result.skippedTestCount} skipped")
									println("\nTest Report: file:///${
									    reports.html.entryPoint.absolutePath.replace(
										File.separatorChar, '/')
									}")
								    }
								}))
}
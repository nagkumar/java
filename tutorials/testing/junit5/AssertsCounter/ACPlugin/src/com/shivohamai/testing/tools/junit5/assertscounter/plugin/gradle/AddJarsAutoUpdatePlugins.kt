package com.shivohamai.testing.tools.junit5.assertscounter.plugin.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class AddJarsAutoUpdatePlugins : Plugin<Project>
{
    override fun apply(aProject: Project)
    {
	aProject.pluginManager.apply("com.github.ben-manes.versions")
	aProject.pluginManager.apply("se.patrikerdes.use-latest-versions")

	aProject.afterEvaluate {
	    println("These below 2 Plugins are added to auto update jars")
	    println("    1. ben-manes.versions")
	    println("    2. use-latest-versions")
	    println("So the available tasks are:")
	    println("- ./gradle dependencyUpdates")
	    println("- ./gradle useLatestVersions")
	}
    }
}
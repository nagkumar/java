package com.shivohamai.testing.tools.junit5.assertscounter.plugin.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class AddExtraPlugins : Plugin<Project>
{

    companion object
    {
	const val VERSIONS_PLUGIN_VERSION = "0.52.0"
	const val USE_LATEST_VERSIONS_PLUGIN_VERSION = "0.2.18"
    }

    override fun apply(project: Project)
    {
	// Use the plugins block approach - this requires the plugins to be available
	// in the plugin's own classpath, not added dynamically
	project.pluginManager.apply("com.github.ben-manes.versions")
	project.pluginManager.apply("se.patrikerdes.use-latest-versions")

	project.afterEvaluate {
	    println("MyPls plugin applied successfully with hardcoded versions:")
	    println("- ben-manes.versions: $VERSIONS_PLUGIN_VERSION")
	    println("- use-latest-versions: $USE_LATEST_VERSIONS_PLUGIN_VERSION")
	    println("Available tasks:")
	    println("- ./gradlew dependencyUpdates")
	    println("- ./gradlew useLatestVersions")
	}
    }
}
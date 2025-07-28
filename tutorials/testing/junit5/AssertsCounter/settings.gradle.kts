rootProject.name = "asserts-counter"

include("ACAgent")
include("ACPlugin")

project(":ACAgent").name = "asserts-counter-agent"
project(":ACPlugin").name = "asserts-counter-plugin"

pluginManagement {
    repositories {
	maven {
	    name = "GitHubPackages"
	    url = uri("https://maven.pkg.github.com/nagkumar/java")
	    credentials {
		username = System.getenv("GITHUB_ACTOR")
		password = System.getenv("GITHUB_TOKEN")
	    }
	}
	gradlePluginPortal()
	mavenCentral()
    }
}

plugins {
    id("org.ajoberstar.reckon.settings") version "0.19.2"
}

extensions.configure<org.ajoberstar.reckon.gradle.ReckonExtension> {
    setDefaultInferredScope("patch")
    stages("beta", "rc", "final")
    setScopeCalc(calcScopeFromProp().or(calcScopeFromCommitMessages()))
    setStageCalc(calcStageFromProp())
}
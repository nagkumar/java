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
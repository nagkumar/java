```
repositories {
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/nagkumar/java")
        credentials {
        username =
        System.getenv("GITHUB_ACTOR")
        ?: "nagkumar"
        password = System.getenv("GITHUB_TOKEN")
    }
}

dependencies {
    testImplementation("com.shivoham.tools.junit5.assertcounter:asserts-agent:1.0.0")
}

tasks.test {
    useJUnitPlatform()
    jvmArgumentProviders.add(CommandLineArgumentProvider {
        var dd = configurations.testRuntimeClasspath.get().files.find {
        it.name.contains("asserts-agent")
        }
    }
}

<dependency>
  <groupId>com.shivoham.tools.junit5.assertcounter</groupId>
  <artifactId>asserts-agent</artifactId>
  <version>1.0.0</version>
</dependency>
```

https://github.com/nagkumar/java/packages/2589016

# how to make sure publish of GitHub takes this file to display about the library???
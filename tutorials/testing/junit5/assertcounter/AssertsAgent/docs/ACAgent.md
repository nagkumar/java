java -javaagent:assert-counter-agent.jar -jar junit-platform-console-standalone.jar ...

tasks.test {
useJUnitPlatform()

    // Adjust the path as per your actual location
    val agentPath = "${rootDir}/libs/assert-counter-agent.jar"

    jvmArgs("-javaagent:$agentPath")

    // Optional: Print output at the end
    doLast {
        println("You can check logs/output for total assert count")
    }

}
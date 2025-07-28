@file:Suppress("SpellCheckingInspection")

plugins {
    id("com.github.ben-manes.versions") version ("0.52.0")
    id("se.patrikerdes.use-latest-versions") version ("0.2.18")
}

dependencies {
    implementation("com.typesafe:config:1.4.4")
    implementation("net.bytebuddy:byte-buddy:1.17.6")
    implementation("net.bytebuddy:byte-buddy-agent:1.17.6")
    implementation("org.ow2.asm:asm:9.8") //fix to bug https://github.com/tginsberg/junit5-system-exit/issues/34

    testImplementation(platform("org.junit:junit-bom:6.0.0-M2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.platform:junit-platform-launcher")

    testImplementation("org.assertj:assertj-core:4.0.0-M1")
    testImplementation("org.hamcrest:hamcrest:3.0")
}

tasks.test {
    useJUnitPlatform()
    val agentJarPath = tasks.jar.get().archiveFile.get().asFile.absolutePath
    jvmArgs("-javaagent:$agentJarPath")
}

tasks.jar {
    manifest {
	attributes["Premain-Class"] = "com.shivohamai.testing.tools.junit5.assertscounter.agent.ACAgent"
	attributes["Can-Redefine-Classes"] = "true"
	attributes["Can-Retransform-Classes"] = "true"
    }
}

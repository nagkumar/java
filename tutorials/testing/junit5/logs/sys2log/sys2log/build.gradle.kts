plugins {
    id("java")
}

group = "com.shivoham.sys2log"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.slf4j:slf4j-api:2.0.17")
    implementation("org.apache.logging.log4j:log4j-core:2.25.1")
    implementation("org.apache.logging.log4j:log4j-api:2.25.1")
    implementation("org.apache.logging.log4j:log4j-iostreams:2.25.1")

    //intellij does not support testImplementation for the test files within main folder, hence as work
    //arround making the test jars part of main
    implementation(platform("org.junit:junit-bom:5.11.0"))
    implementation("org.junit.jupiter:junit-jupiter")
    implementation("org.junit.platform:junit-platform-launcher")
    implementation("com.github.stefanbirkner:system-lambda:1.2.1")
    implementation("io.github.hakky54:logcaptor:2.11.0")
}

tasks.test {
    useJUnitPlatform()
}
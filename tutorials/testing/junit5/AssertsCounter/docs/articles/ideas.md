1. Diference between properties in gradle.properties and settings.properties
2. siginficance of settings.properties in in multy build enviornment - as the top root finding location for childern
3. id("net.saliman.properties") version "1.6.0" write about this plugin and make it default for every project through this plugin
4. make gradle.properties compulsory for every project a way to take confifurable options for build
5. make default tasks in plugin ac
6. How to handle app.properties away from gradle.properties as gradle.properties may be configuration of gradle build don't mix
7. Maven Publish Plugin, its evolution, its support to various repo types (Maven Central Portal, Gradle Poral, Google, Github packages, maven local ) and various artifact types such as aar, ear, war, etc.. say good about each, cons and how next one is overcoming the best aviable one as of 2025 July, tone of the article should be as per Product Architect with 20 years having seen many products suffer not automting at least 200 words, disagrames as requierd, images as required, works with any langaues of java i.e JDK 24, kotlin, groovy, scala, groovy etc

https://medium.com/@nagendra.raja/counting-asserts-in-junit-tests-what-started-as-a-script-became-a-plugin-64ac682e92b0

maven plugin
maven-publish plugin
publish-plugin - gradle
vanniktech


I want to use com.vanniktech.maven.publish plugin to publish helloword gradle plugin to all 4 the repos

1. Maven Central
2. Sonatype Central Portal
3. github packages
4. jitpack

can you write full hellow world gradle plugin that demos the power of using this com.vanniktech.maven.publish plugin to publish.

make sure all the required infot publish come from group, id details of the gradle.kts script and from systemenvironment and/or gradle.properties.

intent is to have this helloworld code and write the medium article there on..so first get solid and simple code that can publish any of the repo with developer giving the min needed details as properties and no additional .kts script blocks are needed using this com.vanniktech.maven.publish plugin


Make sure it works with latest jdk 24 and gradle 8.14.1 and every framework is used is of the latest version i.e. junit 5 etc
Also, jitpack git commits also should happen on its own when this jiobpack publish happens

give me the final zip of the code
make sure tag for jitpack is same as verstion variable value in gradle kotlin script

make sure resources and properties file are also included in zip

How to publish to google, azure, aws that also talked about


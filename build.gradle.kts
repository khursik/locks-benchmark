plugins {
    java
    id("java-library")
    id("me.champeau.jmh") version "0.7.0"
}

group = "ru.sbt"
version = "1.0"

val javaDocVersion: String by project

val slf4jVersion: String by project
val logbackVersion: String by project

val commonsCliVersion: String by project

val junitVersion: String by project

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.maven.plugins:maven-javadoc-plugin:$javaDocVersion")

    implementation("org.slf4j:slf4j-api:$slf4jVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("ch.qos.logback:logback-core:$logbackVersion")
    implementation("org.openjdk.jmh:jmh-archetypes:1.35")
    implementation("org.openjdk.jmh:jmh-parent:1.35")
    implementation("org.openjdk.jmh:jmh-core:1.35")
    implementation("org.openjdk.jmh:jmh-generator-bytecode:1.35")
    implementation("org.openjdk.jmh:jmh-generator-annprocess:1.35")
    implementation("org.openjdk.jmh:jmh-generator-asm:1.35")
    implementation("org.openjdk.jmh:jmh-java-benchmark-archetype:1.35")

    implementation("commons-cli:commons-cli:$commonsCliVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.test {
    useJUnitPlatform()
}

jmh {
    warmupIterations.set(10)
    iterations.set(3)
    fork.set(1)
}
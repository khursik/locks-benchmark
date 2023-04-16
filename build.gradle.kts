plugins {
    java
    idea
    id("maven-publish")
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

    implementation("commons-cli:commons-cli:$commonsCliVersion")

    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
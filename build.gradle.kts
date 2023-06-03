plugins {
    java
    id("com.github.johnrengelman.shadow") version "6.0.0"
    id("me.champeau.jmh") version "0.7.0"
}

sourceSets {
    main {
        java {
            //include("ru/somepackage/**")
        }

        resources {

        }
    }

    jmh {

    }
}

repositories {
    mavenCentral()
}

group = "ru.sbt"
version = "1.0"

val slf4jVersion: String by project
val logbackVersion: String by project

val commonsCliVersion: String by project


dependencies {
    implementation("org.slf4j:slf4j-api:$slf4jVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("ch.qos.logback:logback-core:$logbackVersion")
    implementation("org.openjdk.jmh:jmh-archetypes:1.35")
    implementation("org.openjdk.jmh:jmh-parent:1.35")
    implementation("org.openjdk.jmh:jmh-core:1.35")
    //implementation("org.openjdk.jmh:jmh-generator-bytecode:1.35")
    implementation("org.openjdk.jmh:jmh-generator-annprocess:1.35")
    //implementation("org.openjdk.jmh:jmh-generator-asm:1.35")
    implementation("org.openjdk.jmh:jmh-java-benchmark-archetype:1.35")

    implementation("commons-cli:commons-cli:$commonsCliVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks {
    jmhCompileGeneratedClasses {

    }

    task("copyJmhItems") {
        dependsOn(jmhCompileGeneratedClasses)
        doLast {
            copy {
                from("build/classes/java/jmh") {
                    include("**/*")
                }
                into("build/classes/java/main")
            }

            copy {
                from("build/jmh-generated-classes") {
                    include("**/*")
                }
                into("build/classes/java/main")
            }

            copy {
                from("build/jmh-generated-resources") {
                    include("**/*")
                }
                into("build/resources/main")
            }
        }
    }

    var mainClass = "ru.somepackage.LockTester"
    jar {
        dependsOn("copyJmhItems")
        manifest.attributes["Main-Class"] = mainClass;
    }

    task("fatJar", type = Jar::class) {
        dependsOn.addAll(listOf("compileJava", "processResources", "copyJmhItems")) // We need this for Gradle optimization to work
        archiveClassifier.set("standalone")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest { attributes(mapOf("Main-Class" to mainClass)) } // Provided we set it up in the application plugin configuration
        val sourcesMain = sourceSets.main.get()
        val contents =
                configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) } +
                        configurations.jmh.get().map { if (it.isDirectory) it else zipTree(it) } +
                        sourcesMain.output
        from(contents)
    }

    build {
        dependsOn("fatJar")
    }
}

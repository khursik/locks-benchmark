rootProject.name = "locks-benchmark"

buildscript {
    val prop = java.util.Properties()
    prop.load(java.io.FileReader("${rootDir.absolutePath}/gradle.properties"))

    repositories {
        mavenCentral()
    }
}

System.setProperty("http.socketTimeout", "60000")
System.setProperty("http.connectionTimeout", "60000")
System.setProperty("org.gradle.internal.http.socketTimeout", "60000")
System.setProperty("org.gradle.internal.http.connectionTimeout", "60000")


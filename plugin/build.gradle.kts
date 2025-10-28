plugins {
    id("java-library")
    id("com.gradleup.shadow") version "8.3.9"
}

dependencies {
    implementation(project(":api"))
}

tasks.processResources {
    val props = mapOf("version" to project.version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}

tasks.shadowJar {
    archiveBaseName.set("MenuInv")
    archiveClassifier.set("")
    archiveVersion.set("")
}

artifacts {
    archives(tasks.shadowJar)
}
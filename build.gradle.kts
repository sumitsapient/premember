plugins {
    id("com.neva.fork")
}

apply(from ="gradle/fork.gradle.kts")

allprojects {
    group = "com.mirumagency.uhc.premember"
    version = "0.0.1-SNAPSHOT"
}

defaultTasks(":all:packageDeploy")

subprojects {
    repositories {
        jcenter()
        maven { url = uri("https://repo.adobe.com/nexus/content/groups/public") }
    }

    plugins.withId("java") {
        tasks.withType<JavaCompile>().configureEach {
            with(options) {
                sourceCompatibility = "1.8"
                targetCompatibility = "1.8"
                encoding = "UTF-8"
            }
        }
    }
}

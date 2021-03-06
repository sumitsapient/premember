plugins {
    `kotlin-dsl`
}

repositories {
    jcenter()
    gradlePluginPortal()
    maven("https://dl.bintray.com/neva-dev/maven-public")
}

dependencies {
    "implementation"("com.cognifide.gradle:aem-plugin:10.1.5")
    "implementation"("com.neva.gradle:fork-plugin:4.2.0")
    "implementation"("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.70")
    "implementation"("com.github.node-gradle:gradle-node-plugin:2.2.1")
}
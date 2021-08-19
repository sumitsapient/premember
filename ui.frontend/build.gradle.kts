import com.moowork.gradle.node.npm.NpmTask

plugins {
    id("base")
    id("com.github.node-gradle.node")
}

group = "com.company.example.aem"
description = "Example - AEM UI Frontend"

node {
    version = "10.13.0"
    npmVersion = "6.9.0"
    download = true
}

tasks {
    val outputDir = file("dist")
    register<NpmTask>("npmBuildProd") {
        setNpmCommand("run", "build:prod")
        inputs.file("clientlib.config.js")
        inputs.file("package.json")
        inputs.file("package-lock.json")
        inputs.file("yarn.lock")
        inputs.file("webpack.common.js")
        inputs.file("webpack.dev.js")
        inputs.file("webpack.prod.js")
        inputs.dir("src")
        outputs.dir(outputDir)
        dependsOn("npmInstall")
    }

    register<NpmTask>("npmBuildDev") {
        setNpmCommand("run", "build:dev")
        inputs.file("clientlib.config.js")
        inputs.file("package.json")
        inputs.file("package-lock.json")
        inputs.file("yarn.lock")
        inputs.file("webpack.common.js")
        inputs.file("webpack.dev.js")
        inputs.file("webpack.prod.js")
        inputs.dir("src")
        dependsOn("npmInstall")
    }
    named<Task>("clean") {
        doLast {
            delete(outputDir)
        }
    }
    named("build") {
        dependsOn("npmBuildDev")
    }
}
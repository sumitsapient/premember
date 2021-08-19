plugins {
    id("com.cognifide.aem.package")
}

description = "UHC EnI Pre Member - UI apps"

aem {
    tasks {
        packageCompose {
            vaultDefinition { // this allows Packages build by Gradle to match these build by Maven
                group = "com.mirumagency.uhc"
            }
            dependsOn(":ui.frontend:build")
            fromProject(":core")
        }
    }
}


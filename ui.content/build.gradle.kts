plugins {
    id("com.cognifide.aem.package")
    id("com.cognifide.aem.package.sync")
}

description = "UHC EnI Pre Member - UI content"

aem {
    tasks {
        packageCompose {
            vaultDefinition { // this allows Packages build by Gradle to match these build by Maven
                group = "com.mirumagency.uhc"
            }
        }
    }
}


plugins {
    id("com.cognifide.aem.package")
}

description = "UHC EnI Pre Member - All"

aem {
    tasks {
        packageCompose {
            fromSubpackage(":ui.apps:packageCompose")
            fromSubpackage(":ui.content:packageCompose")
        }
    }
}
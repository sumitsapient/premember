import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    id("com.cognifide.aem.bundle")
    id("io.freefair.lombok") version "5.0.0"
}

description = ("UHC EnI Pre Member - Core")

aem {
    tasks {
        bundleCompose {
            symbolicName = "premember.core"
        }
    }
}

tasks {
    withType<Test>().configureEach {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
            exceptionFormat = TestExceptionFormat.SHORT
        }
    }
}

dependencies {
    compileOnly("org.osgi:org.osgi.annotation.versioning:1.1.0")
    compileOnly("org.osgi:org.osgi.annotation.bundle:1.0.0")
    compileOnly("org.osgi:org.osgi.service.metatype.annotations:1.4.0")
    compileOnly("org.osgi:org.osgi.service.component.annotations:1.4.0")
    compileOnly("org.osgi:org.osgi.service.component:1.4.0")
    compileOnly("org.osgi:org.osgi.service.cm:1.6.0")
    compileOnly("org.osgi:org.osgi.service.event:1.3.1")
    compileOnly("org.osgi:org.osgi.service.log:1.4.0")
    compileOnly("org.osgi:org.osgi.framework:1.9.0")
    compileOnly("org.osgi:org.osgi.resource:1.0.0")
    compileOnly("org.slf4j:slf4j-api:1.7.21")
    compileOnly("javax.jcr:jcr:2.0")
    compileOnly("javax.servlet:javax.servlet-api:3.1.0")
    compileOnly("javax.annotation:javax.annotation-api:1.3.2")
    compileOnly("com.adobe.aem:uber-jar:6.5.0:apis")
    compileOnly("com.adobe.cq:core.wcm.components.core:2.8.0")
    compileOnly("org.apache.sling:org.apache.sling.models.api:1.3.6")
    testImplementation("org.junit.jupiter:junit-jupiter:5.4.1")
    testImplementation("org.mockito:mockito-core:2.25.1")
    testImplementation("org.mockito:mockito-junit-jupiter:2.25.1")
    testImplementation("junit-addons:junit-addons:1.4")
    testImplementation("io.wcm:io.wcm.testing.aem-mock.junit5:2.5.2")
    testImplementation("uk.org.lidalia:slf4j-test:1.0.1")
}


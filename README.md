# E&I Pre Member (EIPM)

Testing my coomit

This is the Pre Member project, built for AEM 6.5.x, for the Employer and Individual line of business at UHC. Pre Member sites allow employer accounts with UHC to present their customized plans with UHC to their employers.

You can find more about the project on Confluence: https://confluence.uhub.biz/display/MIRMINUPMR/Getting+started

- [General information](#general-information)
- [Configure access to Mirums "Unity Framework"](#configure-access-to-mirums-unity-framework)
- [Setup local development with Vagrant](#set-up-local-development-with-vagrant)
- [Setup local development with Gradle](#set-up-local-development-with-gradle)
- [How to build with Maven](#how-to-build-with-maven)
- [How to build with Gradle](#how-to-build-with-gradle)
- [How to sync content with Gradle](#how-to-sync-content-with-gradle)
- [Testing](#testing)
- [Maven settings](#maven-settings)
- [Submodules](#submodules)
- [Specifying CRX Variables](#specifying-crx-variables)
- [Generated using Maven AEM Project Archetype](#generated-using-maven-aem-project-archetype)

---

## General information

This is an agile project where you can use both Maven and Gradle as a build tool. Maven is the primary and is used by
Bamboo for our CI builds. Gradle is secondary build - in practice it is much faster (thanks to parallelization and caching)
and can be happily used for your local development.

In a similar way, you can use both Vagrant or Gradle for your local instance setup. The main difference between them is that Vagrant has already dispatcher configured.

---

## Configure access to Mirums "Unity Framework"

Before getting started, get the Unity RSA private key set up on your machines:

https://unity.mirum.agency/getting-started/authentication/

Username: mirum

Password: unity

You'll need the key and SSH config addition to be able to pull those dependencies with NPM from BitBucket on UHUB.
Read.me has everything else you need to get started.

---

## Set up local development with Vagrant

Please refer to [uhc-vagrant](https://bitbucket.uhub.biz/projects/MIRSANUHC/repos/uhc-ei-pre-member-vagrant-box/browse)

---

## Set up local development with Gradle

Please refer to [Local AEM with Gradle](./aem/README.md)

---

## How to build with Maven

Notice tat this project uses [Maven Wrapper](https://github.com/takari/maven-wrapper) to prevent any Maven compatibility issues.
Instead of invoking `mvn` command please use `mvnw` scripts. You don't need to have Maven installed at all.

To build all the modules run in the project root directory the following command with Maven 3:

    `sh mvnw clean install`

If you have a running AEM instance you can build and package the whole project and deploy into AEM with

    `sh mvnw clean install -PautoInstallPackage`

Or to deploy it to a publish instance, run

    `sh mvnw clean install -PautoInstallPackagePublish`

Or alternatively

    `sh mvnw clean install -PautoInstallPackage -Daem.port=4503`

Or to deploy only the bundle to the author, run

    `sh mvnw clean install -PautoInstallBundle`

---

## How to build with Gradle

To build & deploy the app using Gradle simply run:

    `sh gradlew`

## How to sync content with Gradle

To sync content from your local instance simply type in the commandline:

    `sh gradlew :ui.content:packageSync -Pfilter.roots=/content/premember/employers/demo/corner-case-employer/en/home/fsa-plans`

You can change filtering path as needed. Also, you can sync with other instances than your local. DEV instance is already configured in `gradle.properties` file so you can syn with it by executing:
`sh gradlew :ui.content:packageSync -Pinstance.list="[https://dev-aem-author.eipm.uhc.mirum.agency]" -Pfilter.roots=/content/premember/employers/demo/corner-case-employer/en/home/fsa-plans`

## How to sync your local instance content with DEV instance content

This will sync `/content/premember` & `/content/dam/premember`

    `sh gradlew :aem:syncDevAuthorToLocalInstance -Pforce`
    `sh gradlew :aem:syncDevPublishToLocalInstance -Pforce`

---

## Testing

There are three levels of testing contained in the project:

- unit test in core: this show-cases classic unit testing of the code contained in the bundle. To test, execute:

  `cd core ; sh ../mvnw clean test`

- server-side integration tests: this allows us to run unit-like tests in the AEM-environment, ie on the AEM server. To test, execute:

  `sh mvnw clean verify -PintegrationTests`

---

## Maven settings

The project comes with the auto-public repository configured. To set up the repository in your Maven settings, refer to
[Set Up The Adobe Maven Repository](http://helpx.adobe.com/experience-manager/kb/SetUpTheAdobeMavenRepository.html).

---

## Submodules

- core: Java bundle containing all core functionality like OSGi services, listeners or schedulers, as well as component-related Java code such as servlets or request filters.
- ui.apps: contains the /apps (and /etc) parts of the project, ie JS&CSS clientlibs, components, templates, run-mode specific configs
- ui.content: contains sample content using the components from the ui.apps
- ui.tests: Java bundle containing JUnit tests that are executed server-side. This bundle is not to be deployed into production.
- ui.launcher: contains glue code that deploys the ui.tests bundle (and dependent bundles) to the server and triggers the remote JUnit execution
- ui.frontend: an optional dedicated front-end build mechanism based on Webpack

---

## Specifying CRX Variables

Append one or more of the following parameters after the build command to override the defaults

- Change the AEM host (defaults to localhost)
  - `-Daem.host=remotehost`
- Change the AEM port (defaults to 4502)
  - `-Daem.port=4512`
- Change the AEM password (defaults to admin)
  - `-Daem.password=sadmin`

## Generated using Maven AEM Project Archetype

[Maven AEM Project Archetype](https://github.com/adobe/aem-project-archetype) version 22 based on guide [Getting Started with AEM Sites - WKND Tutorial](https://docs.adobe.com/content/help/en/experience-manager-learn/getting-started-wknd-tutorial-develop/project-setup.html).

```shell script
mvn archetype:generate -B \
  -DarchetypeGroupId=com.adobe.granite.archetypes \
  -DarchetypeArtifactId=aem-project-archetype \
  -DarchetypeVersion=22 \
  -DgroupId=com.mirumagency.uhc \
  -Dversion=0.0.1-SNAPSHOT \
  -DappsFolderName=premember \
  -DartifactId=premember \
  -Dpackage=com.mirumagency.uhc.premember \
  -DartifactName="UHC EnI Pre Member" \
  -DcomponentGroupName="UHC EnI Pre Member" \
  -DconfFolderName=premember \
  -DcontentFolderName=premember \
  -DcssId=premember.css \
  -DisSingleCountryWebsite=n \
  -Dlanguage_country=en_us \
  -DoptionAemVersion=6.5.0 \
  -DoptionDispatcherConfig=none \
  -DoptionIncludeErrorHandler=y \
  -DoptionIncludeExaples=y \
  -DoptionIncludeFrontendModule=y \
  -DpackageGroup=premember \
  -DsiteName="UHC EnI Pre Member"
```

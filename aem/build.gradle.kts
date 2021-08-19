plugins {
    id("com.neva.fork")
    id("com.cognifide.aem.instance")
}

aem {
    tasks {
        instanceProvision {
            step("enable-crxde") {
                description = "Enables CRX DE"
                condition { once() && instance.environment != "prod" }
                action {
                    sync {
                        osgiFramework.configure("org.apache.sling.jcr.davex.impl.servlets.SlingDavExServlet", mapOf(
                                "alias" to "/crx/server"
                        ))
                    }
                }
            }
        }
        instanceSatisfy {
            packages {
                "core.wcm.components.all"("https://github.com/adobe/aem-core-wcm-components/releases/download/core.wcm.components.reactor-2.8.0/core.wcm.components.all-2.8.0.zip")
                "brightcove_connector.ui.apps"("https://github.com/brightcove/Adobe-AEM-Brightcove-Connector/releases/download/5.5.4/brightcove_connector.ui.apps-5.5.4.zip")
            }
        }
        fun downloadAndInstallPackage(
                source: com.cognifide.gradle.aem.common.instance.Instance,
                destination: com.cognifide.gradle.aem.common.instance.Instance
        ) {
            val packageName = "${source.name}.content"
            logger.lifecycle("Fetching content from ${source.name}")
            source.sync {
                repository {
                    val nodes = node("/content/premember").children + node("/content/dam/premember").children
                    nodes.forEach { node ->
                        logger.lifecycle("${node.path} creating package")
                        val pkg = source.sync {
                            packageManager {
                                download {
                                    name = packageName
                                    description = "${node.path} package from ${source.name}"
                                    filter(node.path)
                                }
                            }
                        }
                        logger.lifecycle("${node.path} downloaded")
                        destination.sync {
                            packageManager {
                                deploy(pkg)
                            }
                        }
                        logger.lifecycle("${node.path} installed")
                    }
                }
            }
        }
        register("syncDevAuthorToLocalInstance") {
            checkForce()
            doLast {
                downloadAndInstallPackage(
                        source = aem.instance("https://dev-aem-author.eipm.uhc.mirum.agency/"),
                        destination = aem.instance("http://localhost:4502/")
                )
            }
        }
        register("syncDevPublishToLocalInstance") {
            checkForce()
            doLast {
                downloadAndInstallPackage(
                        source = aem.instance("https://dev-aem-publish.eipm.uhc.mirum.agency/"),
                        destination = aem.instance("http://localhost:4503/")
                )
            }
        }
    }
}

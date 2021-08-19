//https://jira.uhub.biz/browse/MIRMINUHCEIPREMEM-755

def disabledFieldsThatHaveToBePopulated = [
        "dental" : ["comparisonSearchCoverageLinkText", "comparisonFullBenefitsLinkText"],
        "health" : ["prescriptionSearchLink", "providersComparisonSearchCoverageLinkText", "prescriptionsComparisonSearchCoverageLinkText", "comparisonFullBenefitsLinkText"],
        "vision" : ["comparisonSearchCoverageLinkText", "comparisonFullBenefitsLinkText"]
]

disabledFieldsThatHaveToBePopulated.each {planType, properties ->
    def options = getResource("/content/dam/premember/plans/options/${planType}")
    options.children.findAll{it.name != "jcr:content"}.each {
        child ->
            def optionData = getNode("/content/dam/premember/plans/options/${planType}/${child.name}/jcr:content/data/master")
            properties.each {
                property ->
                    if(!optionData.hasProperty(property)) {
                        println "Updating ${optionData.path} with ${property}"
                        optionData.setProperty(property, true)
                    }
            }
    }
}

 save()


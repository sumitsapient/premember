package com.mirumagency.uhc.premember.core.services.federal.repository.jcr;

import com.adobe.cq.dam.cfm.ContentElement;
import com.adobe.cq.dam.cfm.ContentFragment;
import com.mirumagency.uhc.premember.core.domain.federal.PlanName;
import com.mirumagency.uhc.premember.core.domain.federal.PlanPDFFiles;
import com.mirumagency.uhc.premember.core.domain.federal.PlanType;
import com.mirumagency.uhc.premember.core.services.federal.repository.HealthDocumentsAndResourcesRepo;
import com.mirumagency.uhc.premember.core.services.federal.repository.PlanNameRepo;
import com.mirumagency.uhc.premember.core.services.federal.repository.jcr.paths.PlanPath;
import com.mirumagency.uhc.premember.core.services.migration.Constants;
import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;
import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.ResourceGentleman;
import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.core.fs.FileSystem;
import org.apache.jackrabbit.vault.util.JcrConstants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component( service = HealthDocumentsAndResourcesRepo.class )
public class HealthDocumentsAndResourcesRepoJcr implements HealthDocumentsAndResourcesRepo {

    private static final String HEALTH_DOCUMENT_AND_RESOURCES_CFM_PATH = "/conf/premember/premium-rate/settings/dam/" +
            "cfm/models/health-documents-and-resources";
    private static final String CQ_MODEL = "cq:model";

    @Reference
    private ResourceGentleman resourceGentleman;

    @Reference
    private PlanNameRepo planNameRepo;

    @Override
    public List<PlanPDFFiles> load(String regionCode, String pageUrl) {
        PlanPath planPath = new PlanPath(pageUrl);
        final String titleProp = JcrConstants.JCR_CONTENT +  FileSystem.SEPARATOR + JcrConstants.JCR_TITLE;
        List<PlanName> planNames = planNameRepo.loadPlanTypes(PlanType.HEALTH);
        return resourceGentleman.withResolver(resourceResolver ->
            resourceResolver.getResourceOrThrow(Constants.FEDERAL_REGIONS_PATH)
            .getChild(regionCode)
            .map(resource -> resource.getChildren()
                    .filter(this::isHealthDocumentsAndResourcesContentFragment)
                    .filter(plan -> planNames.stream().anyMatch(p -> plan.getString(titleProp).equals(p.getId()))
                            && plan.getString(titleProp).equals(planPath.getPlanOptionName())))
            .flatMap(this::loadHealthDocumentsAndResourcesContentFragment)
            .map(this::loadHealthDocumentsAndResources)
            .orElse(Collections.emptyList()));
    }

    private boolean isHealthDocumentsAndResourcesContentFragment(NiceResource niceResource){
        String dataNodePath = JcrConstants.JCR_CONTENT + FileSystem.SEPARATOR + "data";
        return niceResource.getChild(dataNodePath)
                .map(dataResource -> dataResource.getString(CQ_MODEL).equals(HEALTH_DOCUMENT_AND_RESOURCES_CFM_PATH))
                .orElse(false);
    }

    private Optional<ContentFragment> loadHealthDocumentsAndResourcesContentFragment(Stream<NiceResource> resourceStream) {
        return resourceStream
                .findFirst()
                .map(resource -> resource.adaptTo(ContentFragment.class));
    }

    private List<PlanPDFFiles> loadHealthDocumentsAndResources(ContentFragment contentFragment) {
        List<PlanPDFFiles> planPDFFiles = new ArrayList<>();
        PlanPDFFiles section1 = getPDFFilesList(contentFragment, "1");
        PlanPDFFiles section2 = getPDFFilesList(contentFragment, "2");
        if (section1 != null) {
            planPDFFiles.add(section1);
        }
        if (section2 != null) {
            planPDFFiles.add(section2);
        }
        return planPDFFiles;
    }

    private  PlanPDFFiles getPDFFilesList(ContentFragment contentFragment, String sectionNumber) {
        List<PlanPDFFiles.PDFFileDTO> pdfFileList = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            PlanPDFFiles.PDFFileDTO pdfFile = getPlanPDFFile(contentFragment, String.valueOf(i), sectionNumber );
            if (pdfFile != null) {
                pdfFileList.add(pdfFile);
            }
        }
        return PlanPDFFiles.builder()
                .sectionTitle(getContentFragmentValue(contentFragment, String.format("section%sTitle", sectionNumber)))
                .pdfFiles(pdfFileList)
                .build();
    }

    private PlanPDFFiles.PDFFileDTO getPlanPDFFile(ContentFragment contentFragment, String documentNumber, String sectionNumber) {
        String title = getContentFragmentValue(contentFragment, String.format("document%1$sSection%2$sTitle", documentNumber, sectionNumber));
        String path = getContentFragmentValue(contentFragment, String.format("document%1$sSection%2$sPdf", documentNumber, sectionNumber));
        PlanPDFFiles.PDFFileDTO pdfFileDTO = null;
        if (!StringUtils.isEmpty(title) && !StringUtils.isEmpty(path)) {
            pdfFileDTO = PlanPDFFiles.PDFFileDTO.builder()
                    .title(title)
                    .path(path)
                    .build();
        }
        return pdfFileDTO;
    }

    private String getContentFragmentValue(ContentFragment contentFragment, String propertyName) {
        return Optional.of(contentFragment)
                .map(c -> c.getElement(propertyName))
                .map(ContentElement::getContent)
                .orElse(null);
    }

}

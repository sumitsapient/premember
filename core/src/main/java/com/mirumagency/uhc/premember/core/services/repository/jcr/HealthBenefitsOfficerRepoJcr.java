package com.mirumagency.uhc.premember.core.services.repository.jcr;

import com.adobe.cq.dam.cfm.ContentElement;
import com.adobe.cq.dam.cfm.ContentFragment;
import com.day.crx.JcrConstants;
import com.mirumagency.uhc.premember.core.domain.federal.HealthBenefitsOfficer;
import com.mirumagency.uhc.premember.core.services.migration.Constants;
import com.mirumagency.uhc.premember.core.services.repository.HealthBenefitsOfficerRepo;
import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;
import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.ResourceGentleman;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.core.fs.FileSystem;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component(service = HealthBenefitsOfficerRepo.class)
public class HealthBenefitsOfficerRepoJcr implements HealthBenefitsOfficerRepo {

    private static final String HEALTH_BENEFITS_OFFICERS_CFM_PATH = "/conf/premember/premium-rate/settings/dam/cfm" +
            "/models/health-benefits-officers";
    private static final String CQ_MODEL = "cq:model";
    @Reference
    private ResourceGentleman resourceGentleman;

    @Override
    public List<HealthBenefitsOfficer> load(String regionCode) {
        return resourceGentleman.withResolver(
                resolver -> resolver
                        .getResourceOrThrow(Constants.FEDERAL_REGIONS_PATH)
                        .getChild(regionCode)
                        .map(regionResource -> regionResource.getChildren().filter(this::isOfficersContentFragment))
                        .flatMap(this::loadOfficersContentFragment)
                        .map(this::loadOfficers)
                        .orElse(Collections.emptyList()));
    }

    private boolean isOfficersContentFragment(NiceResource regionResource) {
        String dataNodePath = JcrConstants.JCR_CONTENT + FileSystem.SEPARATOR + "data";
        return regionResource.getChild(dataNodePath)
                .map(dataResource -> dataResource.getString(CQ_MODEL).equals(HEALTH_BENEFITS_OFFICERS_CFM_PATH))
                .orElse(false);
    }

    private Optional<ContentFragment> loadOfficersContentFragment(Stream<NiceResource> officersStream) {
        return officersStream
                .findFirst()
                .map(officerResource -> officerResource.adaptTo(ContentFragment.class));
    }

    private List<HealthBenefitsOfficer> loadOfficers(ContentFragment contentFragment) {
        List<HealthBenefitsOfficer> list = new ArrayList<>();
        HealthBenefitsOfficer officerOne = loadOfficer(contentFragment, "One");
        HealthBenefitsOfficer officerTwo = loadOfficer(contentFragment, "Two");
        if (officerOne != null) {
            list.add(officerOne);
        }
        if (officerTwo != null) {
            list.add(officerTwo);
        }
        return list;
    }

    private HealthBenefitsOfficer loadOfficer(ContentFragment contentFragment, String officerNumber) {
        String officerName = getContentFragmentValue(contentFragment,
                String.format("officer%sName", officerNumber));
        String officerDescription = getContentFragmentValue(contentFragment,
                String.format("officer%sDescription", officerNumber));
        String officerOneEmail = getContentFragmentValue(contentFragment,
                String.format("officer%sEmail", officerNumber));
        String officerPhoneNumber = getContentFragmentValue(contentFragment,
                String.format("officer%sPhoneNumber", officerNumber));
        String officerPhotoPath = getContentFragmentValue(contentFragment,
                String.format("officer%sPhotoPath", officerNumber));

        boolean hasContactInfo = !StringUtils.isEmpty(officerOneEmail) || !StringUtils.isEmpty(officerPhoneNumber);
        boolean isOfficerNameEmpty = StringUtils.isEmpty(officerName);

        if (isOfficerNameEmpty || !hasContactInfo) {
            return null;
        }
        return HealthBenefitsOfficer.builder()
                .name(officerName)
                .description(officerDescription)
                .email(officerOneEmail)
                .phoneNumber(officerPhoneNumber)
                .photoFilePath(officerPhotoPath)
                .build();
    }

    private String getContentFragmentValue(ContentFragment contentFragment, String propertyName) {
        return Optional.of(contentFragment)
                .map(c -> c.getElement(propertyName))
                .map(ContentElement::getContent)
                .orElse(null);
    }
}

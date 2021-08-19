package com.mirumagency.uhc.premember.core.services.migration;

import com.adobe.aemds.guide.utils.JcrResourceConstants;
import com.adobe.cq.dam.cfm.ContentFragment;
import com.adobe.cq.dam.cfm.FragmentTemplate;
import com.adobe.cq.dam.cfm.ContentFragmentException;
import com.adobe.cq.dam.cfm.ContentElement;
import com.adobe.cq.dam.cfm.FragmentData;
import com.day.cq.commons.jcr.JcrConstants;
import com.mirumagency.uhc.premember.core.domain.federal.PlanName;
import com.mirumagency.uhc.premember.core.domain.federal.PlanType;
import com.mirumagency.uhc.premember.core.exceptions.InvalidPremiumRateException;
import com.mirumagency.uhc.premember.core.services.federal.repository.PlanNameRepo;
import com.mirumagency.uhc.premember.core.util.DamUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.jackrabbit.core.fs.FileSystem;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import static org.apache.http.util.Asserts.notNull;

@Component(service = PremiumRatesMigratorService.class)
public class PremiumRatesMigratorService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Reference
    PlanNameRepo planNameRepo;

    private static final String PREMIUM_RATE_CF_MODEL_PATH = "/conf/premember/premium-rate/settings/dam/cfm/models" +
            "/premium-rate";
    private static final String CF_MODEL_CONFIG_PATH = "/conf/premember/premium-rate";
    private static final String CQ_CONFIG = "cq:conf";
    private static final int SELF_ONLY_ROW = 2;
    private static final int SELF_PLUS_ONE_ROW = 3;
    private static final int SELF_AND_FAMILY_ROW = 4;
    private static final int ENROLLMENT_CODE_CELL = 1;
    private static final int NON_POSTAL_PREMIUM_BIWEEKLY_CELL = 2;
    private static final int NON_POSTAL_PREMIUM_MONTHLY_CELL = 3;
    private static final int NON_POSTAL_PREMIUM_BIWEEKLY_CATEGORY_1_CELL = 4;
    private static final int NON_POSTAL_PREMIUM_BIWEEKLY_CATEGORY_2_CELL = 5;

    /**
     * Migrate premium rates from files to AEM as content fragments.
     * The files should be in .xlsx format.
     *
     * @param resourceResolver the resource resolver
     * @param premiumRatesPath the folder path where the .xlsx files reside with the premium rates
     * @return the number of migrated files
     * @throws FileNotFoundException       thrown if the provided path does not exist
     * @throws InvalidPremiumRateException thrown if invalid data comes in a file
     */
    public int migrate(ResourceResolver resourceResolver, String premiumRatesPath)
            throws FileNotFoundException, InvalidPremiumRateException {

        Resource parentFolderResource = getParentFolderResource(resourceResolver, premiumRatesPath);
        try {
            List<PlanName> planNames = planNameRepo.loadPlanTypes(PlanType.HEALTH);
            int migratedFiles = 0;
            logger.info("Migrating premium rates from folder [{}]", premiumRatesPath);
            for (Resource pricingFileResource : parentFolderResource.getChildren()) {
                InputStream fileInputStream = DamUtil.getFile(resourceResolver, pricingFileResource.getPath());
                if (fileInputStream == null) {
                    continue;
                }
                migrateFile(resourceResolver, fileInputStream, pricingFileResource, planNames);
                migratedFiles++;
            }
            if (migratedFiles > 0) {
                resourceResolver.commit();
            }
            logger.info("Number of premium rates files migrated [{}]", migratedFiles);
            return migratedFiles;
        } catch (IOException | ContentFragmentException e) {
            throw new RuntimeException("An error has occurred when trying to create or update premium rates ", e);
        }
    }

    private Resource getParentFolderResource(ResourceResolver resourceResolver, String premiumRatesPath)
            throws FileNotFoundException {
        Resource parentFolderResource = resourceResolver.getResource(premiumRatesPath);
        if (parentFolderResource == null) {
            throw new FileNotFoundException("Asset folder configured for premium rates not found.");
        }
        return parentFolderResource;
    }

    private void migrateFile(ResourceResolver resourceResolver, InputStream fileInputStream,
                             Resource pricingFileResource, List<PlanName> planNames)
            throws IOException, ContentFragmentException, InvalidPremiumRateException {
        String filePath = pricingFileResource.getPath();
        logger.debug("Migrating premium rate file [{}]", filePath);
        Resource regionFolder = tryToCreateRegionFolder(resourceResolver, pricingFileResource.getName());
        try (XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream)) {
            Iterator<Sheet> sheetIterator = workbook.sheetIterator();
            while (sheetIterator.hasNext()) {
                XSSFSheet sheet = (XSSFSheet) sheetIterator.next();
                String planName = sheet.getSheetName().toLowerCase();
                boolean isPremiumRatePlan = planNames
                        .stream()
                        .anyMatch(type -> type.getId().equalsIgnoreCase(planName));
                if (isPremiumRatePlan) {
                    migratePlanSheet(sheet, resourceResolver, regionFolder, filePath);
                }
            }
        }
        tryToSetRegionFolderConfig(resourceResolver, regionFolder);
    }

    private void tryToSetRegionFolderConfig(ResourceResolver resourceResolver, Resource regionFolderResource)
            throws PersistenceException {
        final Map<String, Object> contentProperties = new HashMap<>();
        contentProperties.put(JcrConstants.JCR_PRIMARYTYPE, JcrConstants.NT_UNSTRUCTURED);
        contentProperties.put(CQ_CONFIG, CF_MODEL_CONFIG_PATH);
        String contentPath = regionFolderResource.getPath() + FileSystem.SEPARATOR + JcrConstants.JCR_CONTENT;
        ResourceUtil.getOrCreateResource(resourceResolver, contentPath,  contentProperties,
                JcrResourceConstants.NT_SLING_FOLDER, false);
    }

    private void migratePlanSheet(XSSFSheet sheet, ResourceResolver resourceResolver, Resource regionFolderResource,
                                  String filePath) throws ContentFragmentException,
            InvalidPremiumRateException {
        String planName = sheet.getSheetName().toLowerCase();
        logger.debug("Plan name [{}]", planName);

        Resource templateResource = resourceResolver.getResource(PREMIUM_RATE_CF_MODEL_PATH);
        notNull(templateResource, "templateResource");
        FragmentTemplate fragmentTemplate = templateResource.adaptTo(FragmentTemplate.class);
        notNull(fragmentTemplate, "fragmentTemplate");
        ContentFragment contentFragment;

        Resource contentFragmentResource = resourceResolver.getResource(regionFolderResource, planName);
        if (contentFragmentResource == null) {
            contentFragment = fragmentTemplate.createFragment(regionFolderResource, planName, planName);
        } else {
            contentFragment = contentFragmentResource.adaptTo(ContentFragment.class);
        }
        PremiumRateSource premiumRateSource = new PremiumRateSource();
        premiumRateSource.setFilePath(filePath);
        premiumRateSource.setPlanName(planName);
        premiumRateSource.setSheet(sheet);
        setContentFragmentValues(contentFragment, premiumRateSource);
    }

    private void setContentFragmentValues(ContentFragment contentFragment, PremiumRateSource source)
            throws InvalidPremiumRateException, ContentFragmentException {
        try {
            setSelfOnly(contentFragment, source);
            setSelfPlusOne(contentFragment, source);
            setSelfAndFamily(contentFragment, source);
        } catch (IllegalStateException e) {
            logger.error("An error has occurred when trying to get premium rates values from file", e);
            throw new InvalidPremiumRateException("Invalid value in file", source.getFilePath(), source.getPlanName());
        }
    }

    private void setSelfOnly(ContentFragment contentFragment, PremiumRateSource source)
            throws ContentFragmentException {
        XSSFRow selfOnlyRow = source.getSheet().getRow(SELF_ONLY_ROW);

        String selfOnlyEnrollmentCode = selfOnlyRow.getCell(ENROLLMENT_CODE_CELL).getStringCellValue();
        Double selfOnlyNonPostalPremiumBiweekly =
                selfOnlyRow.getCell(NON_POSTAL_PREMIUM_BIWEEKLY_CELL).getNumericCellValue();
        Double selfOnlyNonPostalPremiumMonthly =
                selfOnlyRow.getCell(NON_POSTAL_PREMIUM_MONTHLY_CELL).getNumericCellValue();
        Double selfOnlyPostalPremiumBiweeklyCategory1 =
                selfOnlyRow.getCell(NON_POSTAL_PREMIUM_BIWEEKLY_CATEGORY_1_CELL).getNumericCellValue();
        Double selfOnlyPostalPremiumBiweeklyCategory2 =
                selfOnlyRow.getCell(NON_POSTAL_PREMIUM_BIWEEKLY_CATEGORY_2_CELL).getNumericCellValue();

        setElementValue(contentFragment, "selfOnlyEnrollmentCode",
                selfOnlyEnrollmentCode);
        setElementValue(contentFragment, "selfOnlyNonPostalPremiumBiweekly",
                selfOnlyNonPostalPremiumBiweekly);
        setElementValue(contentFragment, "selfOnlyNonPostalPremiumMonthly",
                selfOnlyNonPostalPremiumMonthly);
        setElementValue(contentFragment, "selfOnlyPostalPremiumBiweeklyCategory1",
                selfOnlyPostalPremiumBiweeklyCategory1);
        setElementValue(contentFragment, "selfOnlyPostalPremiumBiweeklyCategory2",
                selfOnlyPostalPremiumBiweeklyCategory2);
    }

    private void setSelfPlusOne(ContentFragment contentFragment, PremiumRateSource source)
            throws ContentFragmentException {
        XSSFRow selfPlusRow = source.getSheet().getRow(SELF_PLUS_ONE_ROW);
        String selfPlusOneEnrollmentCode = selfPlusRow.getCell(ENROLLMENT_CODE_CELL).getStringCellValue();
        Double selfPlusOneNonPostalPremiumBiweekly =
                selfPlusRow.getCell(NON_POSTAL_PREMIUM_BIWEEKLY_CELL).getNumericCellValue();
        Double selfPlusOneNonPostalPremiumMonthly =
                selfPlusRow.getCell(NON_POSTAL_PREMIUM_MONTHLY_CELL).getNumericCellValue();
        Double selfPlusOnePostalPremiumBiweeklyCategory1 =
                selfPlusRow.getCell(NON_POSTAL_PREMIUM_BIWEEKLY_CATEGORY_1_CELL).getNumericCellValue();
        Double selfPlusOnePostalPremiumBiweeklyCategory2 =
                selfPlusRow.getCell(NON_POSTAL_PREMIUM_BIWEEKLY_CATEGORY_2_CELL).getNumericCellValue();

        setElementValue(contentFragment, "selfPlusOneEnrollmentCode",
                selfPlusOneEnrollmentCode);
        setElementValue(contentFragment, "selfPlusOneNonPostalPremiumBiweekly",
                selfPlusOneNonPostalPremiumBiweekly);
        setElementValue(contentFragment, "selfPlusOneNonPostalPremiumMonthly",
                selfPlusOneNonPostalPremiumMonthly);
        setElementValue(contentFragment, "selfPlusOnePostalPremiumBiweeklyCategory1",
                selfPlusOnePostalPremiumBiweeklyCategory1);
        setElementValue(contentFragment, "selfPlusOnePostalPremiumBiweeklyCategory2",
                selfPlusOnePostalPremiumBiweeklyCategory2);
    }

    private void setSelfAndFamily(ContentFragment contentFragment, PremiumRateSource source)
            throws ContentFragmentException {
        XSSFRow selfAndFamilyRow = source.getSheet().getRow(SELF_AND_FAMILY_ROW);
        String selfAndFamilyEnrollmentCode =
                selfAndFamilyRow.getCell(ENROLLMENT_CODE_CELL).getStringCellValue();
        Double selfAndFamilyNonPostalPremiumBiweekly =
                selfAndFamilyRow.getCell(NON_POSTAL_PREMIUM_BIWEEKLY_CELL).getNumericCellValue();
        Double selfAndFamilyNonPostalPremiumMonthly =
                selfAndFamilyRow.getCell(NON_POSTAL_PREMIUM_MONTHLY_CELL).getNumericCellValue();
        Double selfAndFamilyPostalPremiumBiweeklyCategory1 =
                selfAndFamilyRow.getCell(NON_POSTAL_PREMIUM_BIWEEKLY_CATEGORY_1_CELL).getNumericCellValue();
        Double selfAndFamilyPostalPremiumBiweeklyCategory2 =
                selfAndFamilyRow.getCell(NON_POSTAL_PREMIUM_BIWEEKLY_CATEGORY_2_CELL).getNumericCellValue();

        setElementValue(contentFragment, "selfAndFamilyEnrollmentCode",
                selfAndFamilyEnrollmentCode);
        setElementValue(contentFragment, "selfAndFamilyNonPostalPremiumBiweekly",
                selfAndFamilyNonPostalPremiumBiweekly);
        setElementValue(contentFragment, "selfAndFamilyNonPostalPremiumMonthly",
                selfAndFamilyNonPostalPremiumMonthly);
        setElementValue(contentFragment, "selfAndFamilyPostalPremiumBiweeklyCategory1",
                selfAndFamilyPostalPremiumBiweeklyCategory1);
        setElementValue(contentFragment, "selfAndFamilyPostalPremiumBiweeklyCategory2",
                selfAndFamilyPostalPremiumBiweeklyCategory2);
    }

    private void setElementValue(ContentFragment contentFragment, String name, Object value)
            throws ContentFragmentException {
        logger.debug("Element name:[{}], Element value:[{}]", name, value);
        ContentElement element = contentFragment.getElement(name);
        FragmentData fragmentData = element.getValue();
        fragmentData.setValue(value);
        element.setValue(fragmentData);
    }

    private Resource tryToCreateRegionFolder(ResourceResolver resourceResolver, String pricingFileName)
            throws PersistenceException {
        String regionFolderPath = Constants.FEDERAL_REGIONS_PATH + FileSystem.SEPARATOR + getRegionName(pricingFileName);
        return ResourceUtil.getOrCreateResource(resourceResolver, regionFolderPath,
                JcrResourceConstants.NT_SLING_FOLDER, JcrResourceConstants.NT_SLING_FOLDER, false);
    }

    private String getRegionName(String pricingFileName) {
        String[] nameParts = pricingFileName.split("-");
        String lastPart = nameParts[nameParts.length - 1];
        return FilenameUtils.removeExtension(lastPart);
    }
}

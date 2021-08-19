package com.mirumagency.uhc.premember.core.services.migration;

import com.adobe.aemds.guide.utils.JcrResourceConstants;
import com.day.cq.commons.jcr.JcrUtil;
import com.mirumagency.uhc.premember.core.util.DamUtil;
import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.sling.api.resource.*;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jcr.RepositoryException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;


@Component(service = ZipCodesMigratorService.class)
public class ZipCodesMigratorService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final int ZIP_CODE_ROW = 0;
    private static final int REGION_ROW = 1;
    private static final String REGION_PROPERTY_KEY = "regionName";
    private static final String ZIP_CODES_PROPERTY_KEY = "zipcodes";

    private String responseText;

    /**
     * Method to Migrate an Excel File with zip codes per region into AEM.
     *
     * @param resourceResolver resource resolver
     * @param zipCodeFile .xlsx file with the zip codes per region
     * @param migrationPath content path to save the migrated data
     * @return the number of migrated files
     */
    public int migrationProcess(ResourceResolver resourceResolver, String zipCodeFile, String migrationPath) {

        this.responseText = StringUtils.EMPTY;
        int regionsNumber = 0;
        try {
            logger.info("Migrating zip codes from file [{}].", zipCodeFile);

            //get file from DAM
            InputStream fileInputStream = DamUtil.getFile(resourceResolver, zipCodeFile);

            if (fileInputStream != null) {
                //Read Sheet and get all the values
                MultiMap zipCodesPerRegion = getZipCodesPerRegion(fileInputStream);

                regionsNumber = zipCodesPerRegion.size();
                //Remove Zip Codes property of the current nodes
                if (zipCodesPerRegion.size() > 1) {
                    removeZipCodesProperty(resourceResolver, migrationPath);
                }

                //Save into AEM
                addZipCodesPerRegion(resourceResolver, zipCodesPerRegion, migrationPath);

                resourceResolver.commit();
            } else {
                logger.info("The path of the excel file is wrong or the file doesn't exist [{}].", zipCodeFile);
            }

        } catch (IOException e) {
            throw new RuntimeException("An error has occurred when trying to create or update zip codes.", e);
        }
        return regionsNumber;
    }


    /**
     * Read the workbook and get all zip codes grouped by region.
     *
     * @param fileInputStream Input Stream of the file with the zip codes per region
     * @return MultiMap with all the zip codes grouped per region
     */
    private MultiMap getZipCodesPerRegion(InputStream fileInputStream) throws IOException {
        MultiMap regionsMap = new MultiValueMap();

        try (XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream)) {

            //get only the first Sheet
            XSSFSheet sheet = workbook.getSheetAt(0);
            int numberOfRows = sheet.getPhysicalNumberOfRows();
            logger.debug("Number of Rows [{}]", numberOfRows);

            appendResponseMessage(String.format("Total Rows Read = %d %n", numberOfRows));

            //read all the rows
            for (int index = 0; index < numberOfRows; index++) {
                XSSFRow row = sheet.getRow(index);
                getRow(row, regionsMap, index);
            }
        } catch (Exception e) {
            String message = String.format("An error has occurred, the file extension must be Excel Workbook with .xlsx extension");
            appendResponseMessage(message);
            logger.error(message, e);
        }

        return regionsMap;
    }


    /**
     * Read the row information and format the zip code and the region name
     *
     * @param row Row of the excel file
     * @param regions Multimap to grouped the zip codes per region
     */
    private void getRow(XSSFRow row, MultiMap regions, int index){

        String message;

        // validate if the row contains two cells
        if (row != null && row.getLastCellNum() >= 2){
            XSSFCell zipCodeCell = row.getCell(ZIP_CODE_ROW);
            XSSFCell regionCell = row.getCell(REGION_ROW);

            if (zipCodeCell.getCellTypeEnum() == NUMERIC && (regionCell.getCellTypeEnum() == STRING ||
                    regionCell.getCellTypeEnum() == NUMERIC)){

                String region = getRegionCellName(regionCell);

                // Format zip code, bunch of dumb stuff here because Apache POI doesn't have a good way
								// to get a numeric value from Excel as a regular Integer.

								// Excel numeric cell is a double
                Double zipCodeDouble = zipCodeCell.getNumericCellValue();

                // Turn that into a String
                String zipCode = zipCodeDouble.toString();

								// Double turned into String has trailing '.0', get rid of that.
                String finishedZipCode = zipCode.replace(".0", "");

								// ZIP codes with leading zeros still have no zeros, add one or two depending on length of string.
								// 5 digit ZIP codes with no leading zeros need no manipulation.
								if (finishedZipCode.length() == 4) {
									finishedZipCode = "0" + finishedZipCode;
								} else if (finishedZipCode.length() == 3) {
									finishedZipCode = "00" + finishedZipCode;
								}

                // Add Zip Code And Region
                regions.put(region, finishedZipCode);
                logger.debug("Region [{}] and Zip Code [{}].", region, finishedZipCode);

            } else {
                message = String.format("An error has occurred, the row %d does not have the correct types of data.",
                        index + 1);
                appendResponseMessage(message);
                logger.info(message);
            }
        } else {
            message = String.format("An error has occurred, the row %d does not have the correct format.", index + 1);
            appendResponseMessage(message);
            logger.info(message);
        }
    }

    /**
     * Creates the sling:folder regions with the properties
     *
     * @param resourceResolver resource resolver
     * @param regionsMap Multimap to grouped the zip codes per region
     * @return totalZipCode number of the zipcodes saved into AEM.
     */
    private void addZipCodesPerRegion(ResourceResolver resourceResolver, MultiMap regionsMap, String contentPath)
            throws IOException {

        int totalZipCodes = 0;
        int totalRegions = 0;
        try {
            if (resourceResolver != null && regionsMap.size() > 0) {
                @SuppressWarnings("unchecked")
                Iterator<String> mapIterator = regionsMap.keySet().iterator();

                //Iterate over the map
                while (mapIterator.hasNext()) {
                    String region = mapIterator.next();

                    @SuppressWarnings("unchecked")
                    List<String> zipCodes = (List<String>) regionsMap.get(region);
                    logger.debug("Region [{}] and  number of Zip Codes [{}]", region, zipCodes.size());

                    //Prepare zip codes to save into JCR
                    String[] zipCodesArray = zipCodes.stream()
                            .toArray(String[]::new);

                    //Save values on JCR
                    createRegionFolder(resourceResolver, contentPath, region, zipCodesArray);

                    totalRegions++;

                    //to get the number of total zip codes Migrated
                    totalZipCodes += zipCodes.size();
                }
                appendResponseMessage(String.format("%n Number of Regions Migrated %s.%n Number of Zip Codes Migrated %d.",
                        totalRegions, totalZipCodes));
            }
        } catch (RepositoryException e) {
            logger.error("An error has occurred when trying to save zip codes per regions into repository.", e);
        }

    }



    /**
     * Creates the sling:folder regions with the properties regionName and zipCodes
     *
     * @param resourceResolver resource resolver
     * @param regionName region Name previously formatted
     * @param zipCodesArray String Array with all the Zip Codes per region
     */
    private void createRegionFolder(ResourceResolver resourceResolver, String contentPath, String regionName,
                                    String[] zipCodesArray) throws PersistenceException, RepositoryException {

        if (resourceResolver != null && StringUtils.isNotBlank(contentPath) &&
                StringUtils.isNotBlank(regionName) && zipCodesArray.length > 0){

            String regionValidName = JcrUtil.createValidName(regionName);
            //Final path into AEM
            String regionFolderPath = contentPath + File.separator + regionValidName;

            //Creates region folder
            Resource regionResource = ResourceUtil.getOrCreateResource(resourceResolver, regionFolderPath,
                    JcrResourceConstants.NT_SLING_FOLDER, JcrResourceConstants.NT_SLING_FOLDER, true);
            ModifiableValueMap properties = regionResource.adaptTo(ModifiableValueMap.class);
            properties.put(REGION_PROPERTY_KEY, regionName);
            properties.put(ZIP_CODES_PROPERTY_KEY, zipCodesArray);

            appendResponseMessage(String.format("The folder by Region %s was created with %d zip codes.",
                    regionName, zipCodesArray.length));
        }

    }


    /**
     * remove zip codes property of the current regions folders
     *
     * @param resourceResolver resource resolver
     */
    private void removeZipCodesProperty(ResourceResolver resourceResolver, String migrationPath) throws PersistenceException {

        Resource regionsFolder = resourceResolver.getResource(migrationPath);
        if (regionsFolder != null) {
            for (Resource regionResource : regionsFolder.getChildren()) {
                ModifiableValueMap properties = regionResource.adaptTo(ModifiableValueMap.class);
                properties.remove(ZIP_CODES_PROPERTY_KEY);
            }

        }
    }

    /**
     * To append the response message
     *
     * @param regionCell
     * @return regionName with the correct format;
     */
    private String getRegionCellName(XSSFCell regionCell) {
        String regionCellName;
        if (regionCell.getCellTypeEnum() == NUMERIC) {
            Double regionNumericValue = regionCell.getNumericCellValue();
            regionCellName = String.valueOf(regionNumericValue.intValue());
        } else {
            regionCellName = regionCell.toString().trim().toLowerCase();
        }
        return regionCellName;
    }


    /**
     * To append the response message
     *
     * @param appendText String to append
     */
    private void appendResponseMessage(String appendText) {
        this.responseText = this.responseText.concat(appendText + "%n");
    }

    /**
     * Return String with response Message
     *
     * @return String
     */
    public String getResponseMessage() {
        return this.responseText;
    }

}

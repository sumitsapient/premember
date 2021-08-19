package com.mirumagency.uhc.premember.core.services;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.resource.LoginException;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.day.cq.reporting.helpers.Const.PN_RESOLVER_PATH;
import static com.day.cq.reporting.helpers.Const.PN_TYPE;
import static com.day.cq.reporting.helpers.Const.PN_VALUE_PROPERTY;
import static com.day.cq.reporting.helpers.Const.PN_FILTER_VALUE;
import static com.day.cq.search.Predicate.PARAM_LIMIT;
import static com.day.cq.search.PredicateConverter.GROUP_PARAMETER_PREFIX;
import static com.mirumagency.uhc.premember.core.services.migration.Constants.FEDERAL_DENTAL_REGIONS_PATH;
import static com.mirumagency.uhc.premember.core.services.migration.Constants.FEDERAL_REGIONS_PATH;
import static org.apache.sling.jcr.resource.api.JcrResourceConstants.NT_SLING_FOLDER;


@Component(service = RegionsService.class, immediate = false)
public class RegionsService {

	private static final Logger log = LoggerFactory.getLogger(RegionsService.class);
	private static final String REGION_PROPERTY_KEY = "regionName";
	private static final String DENTAL_REGION_PROPERTY_KEY = "dentalRegionName";
	private static final String ZIP_CODES_PROPERTY_KEY = "zipcodes";
	private static final String DOT_KEY = ".";
	private static final String DENTAL_REGION_DEFAULT = "5";
	private static final String P_LIMIT_KEY = GROUP_PARAMETER_PREFIX + DOT_KEY + PARAM_LIMIT;
	private static final Map<String, Object> AUTH_INFO = Collections.singletonMap(ResourceResolverFactory.SUBSERVICE,
			"federalServices");

	@Reference
	private ResourceResolverFactory resourceResolverFactory;

	/**
	 * Performs a query based on the parameters sent an returns the region in a map.
	 *
	 * @param zipCode query string
	 *
	 * @return query results in a map
	 */
	public Map<String, String> getRegions(String zipCode) {
		Map<String, String> map = getEmptyResultMap();

		String healthRegion = StringUtils.EMPTY;
		String dentalRegion = DENTAL_REGION_DEFAULT;

		if(StringUtils.isNotBlank(zipCode) && zipCode.matches("\\d{5}")) {

			healthRegion = getRegion(zipCode, FEDERAL_REGIONS_PATH);

			String tempDentalRegion = getDentalRegion(zipCode);
			if (StringUtils.isNotEmpty(tempDentalRegion)) dentalRegion = tempDentalRegion;
		}

		//include regions
		map.put(REGION_PROPERTY_KEY, healthRegion);
		map.put(DENTAL_REGION_PROPERTY_KEY, dentalRegion);

		return map;
	}


	/**
	 * Get dental regions from zipcodes of 5 to 3 characters
	 *
	 * @param zipCode query string
	 *
	 * @return String with the dental Region
	 */
	private String getDentalRegion (String zipCode) {

		String dentalRegion = getRegion(zipCode, FEDERAL_DENTAL_REGIONS_PATH);
		if (StringUtils.isEmpty(dentalRegion) && zipCode.length() > 3) {
			dentalRegion = getDentalRegion(StringUtils.chop(zipCode));
		}
		return dentalRegion;
	}


	/**
	 * Performs a query based on the parameters sent an returns the region in a map.
	 *
	 * @param zipCode query string
	 * @param regionPath path to search
	 *
	 * @return String with the proper Region
	 */
	private String getRegion(String zipCode, String regionPath) {
		String region = StringUtils.EMPTY;
		HashMap<String, String> predicateMap = new HashMap<>();
		ResourceResolver resourceResolver = getResourceResolver();
		if (null != resourceResolver && null != resourceResolver.getResource(regionPath)) {

			QueryBuilder builder = resourceResolver.adaptTo(QueryBuilder.class);
			Session session = resourceResolver.adaptTo(Session.class);

			predicateMap.put(PN_RESOLVER_PATH, regionPath);
			predicateMap.put(PN_TYPE, NT_SLING_FOLDER);

			// search zip codes property
			predicateMap.put(PN_VALUE_PROPERTY, ZIP_CODES_PROPERTY_KEY);
			predicateMap.put(PN_VALUE_PROPERTY + DOT_KEY + PN_FILTER_VALUE, zipCode);

			//get only the first item
			predicateMap.put(P_LIMIT_KEY, "1");
			Query query = builder.createQuery(PredicateGroup.create(predicateMap), session);

			try {
				// getting results
				SearchResult result = query.getResult();
				region = resultRegion(result);
			} catch (Exception e) {
				log.error("There was a problem trying to get the region of the query ", e);
			}finally {
				if (resourceResolver.isLive()){
					resourceResolver.close();
				}
				session.logout();
			}
		} else {
			if (null !=resourceResolver && resourceResolver.isLive()){
				resourceResolver.close();
			}
		}
		return region;
	}


	/**
	 * Converts a SearchResult to a Map (with a format that will be used in the RegionsServlet)
	 * 
	 * @param result SearchResult that is obtained from the execution of the query
	 * @return map that contains the region Name
	 */
	private String resultRegion(final SearchResult result) {
		String region = StringUtils.EMPTY;
		try {
			Iterator<Resource> resources = result.getResources();
			if (resources.hasNext()) {
				Resource resource = resources.next();
				if (null !=resource) {
					ValueMap properties = resource.getValueMap();
					region = properties.get(REGION_PROPERTY_KEY, StringUtils.EMPTY);
				}
				log.debug("Region Service Value [{}].", region);
			}
		} catch (Exception e) {
			log.error("There was a problem trying to get information from query result." , e);
		}
		return region;
	}


	/**
	 * Returns a map with an empty list of news (this should the default map if the region can't be retrieved)
	 *
	 * @return Empty map
	 */
	private Map<String, String> getEmptyResultMap() {
		Map<String, String> emptyMap = Maps.newHashMap();
		emptyMap.put(REGION_PROPERTY_KEY, StringUtils.EMPTY);
		return emptyMap;
	}

	private ResourceResolver getResourceResolver() {
		try {
			ResourceResolver resolver = resourceResolverFactory.getServiceResourceResolver(AUTH_INFO);
			return resolver;
		} catch (LoginException le) {
			log.error("There was a problem trying to get service resource resolver ({})", AUTH_INFO, le);
			return null;
		}
	}

}

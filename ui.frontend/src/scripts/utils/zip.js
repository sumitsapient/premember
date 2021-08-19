import axios from 'axios';
import { setCookie, getCookie, deleteCookie } from './helpers';

export const ZIP_COOKIE = 'zip';
export const REGION_COOKIE = 'federal-region-code';
export const DENTAL_REGION_COOKIE = 'federal-dental-region-code';
export const ZIP_EVENT_NAME = 'zipupdated';

// Synthetic zip updated event (uses depreciated version to keep ie11 compatibility)
const event = document.createEvent('Event');
event.initEvent(ZIP_EVENT_NAME, false, true);

/**
 * Get region information using given Zip Code.
 *
 * @param {Number} zip - Zip Code
 * @returns {String|null} Region value
 */
export const getRegionFromZip = async (zip) => {
	try {
		const response = await axios.get(`/apps/premember/federal/regions.${zip}.json`);

		if (response.data) {
			// check if returned data contains regionName
			if (Object.prototype.hasOwnProperty.call(response.data, 'regionName')) {
				return response.data;
			}
		}

		return null;
	} catch (err) {
		return console.error(err);
	}
};

/**
 * Get region information using given Zip Code.
 *
 * @param {Number} zip - Zip Code
 * @returns {String|null} Region value
 */
export const getDentalRegionFromZip = async (zip) => {
	try {
		const response = await axios.get(`/apps/premember/federal/regions.${zip}.json`);
		if (response.data) {
			// check if returned data contains regionName
			if (Object.prototype.hasOwnProperty.call(response.data, 'regionName')) {
				return response.data;
			}
		}

		return null;
	} catch (err) {
		return console.error(err);
	}
};

/**
 * Set Zip Code cookie
 *
 * @param {Number} zip - Zip Code
 */
export const setZipCookie = zip => setCookie(ZIP_COOKIE, zip, 30);

/**
 * Set region cookie
 * @param {String} region - Region
 */
export const setRegionCookie = region => setCookie(REGION_COOKIE, region, 30);

/**
 * Set region cookie
 * @param {String} region - Region
 */
export const setDentalRegionCookie = region => setCookie(DENTAL_REGION_COOKIE, region, 30);

/**
 * Remove region cookie
 * @param {String} region - Region
 */
export const removeRegionCookie = () => deleteCookie(REGION_COOKIE);


/**
 * Get Zip Code cookie value
 *
 * @returns {String} Zip Code cookie value
 */
export const getZipCookie = () => getCookie(ZIP_COOKIE);

/**
 * Get region cookie value
 *
 * @returns {String} Region cookie value
 */
export const getRegionCookie = () => getCookie(REGION_COOKIE);

/**
 * Checks the first character of the input string to determine if it is a number or not.
 *
 * @param {String} str The input value.
 *
 * @returns {Boolean} True for zip, false for state.
 */

export const isZip = (str) => {
	const char = str.charAt(0);
	return parseInt(char, 10) || char === '0';
};

/**
 * Dispatches synthetic zip code update event
 *
 * @param el {Element|EventTarget} - Element to attach dispatched event to
 */
export const dispatchZipEvent = el => el.dispatchEvent(event);

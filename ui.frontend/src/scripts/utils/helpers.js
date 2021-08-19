/* eslint-disable */
import constants from './constants';

export const throttle = (func, time) => {
	let lastTime = new Date();
	const method = func || (() => { });
	const rate = time || 1000;

	return () => {
		const now = new Date();

		if (now - lastTime >= rate) {
			method();
			lastTime = now;
		}
	};
};

export const isBreakpointMatched = breakpoint => window.innerWidth >= constants.breakpoints[breakpoint];

export const getScrollbarWidth = () => window.innerWidth - document.documentElement.clientWidth;

export const tabbedToNext = event => event.keyCode === constants.keyCodes.tabKey && !event.shiftKey;

export const tabbedToPrevious = event => event.keyCode === constants.keyCodes.tabKey && event.shiftKey;

export const isSafari = () => navigator.vendor && navigator.vendor.indexOf('Apple') > -1
	&& navigator.userAgent
	&& navigator.userAgent.indexOf('CriOS') === -1
	&& navigator.userAgent.indexOf('FxiOS') === -1;

export const isAuthor = () => window.Granite && window.Granite.author;

export const isEmpty = obj => [Object, Array].includes((obj || {}).constructor) && !Object.entries((obj || {})).length;

// /[^\x00-\x7F]/g ==> all non-ascii characters
/* eslint-disable-next-line */
export const normalizeText = text => (text || '').trim().toLowerCase().replace(/\s/g, '_').replace(/[^\x00-\x7F]/g, '');

export const filterArray = (array, predicate) => {
	for (let i = 0; i < array.length; i += 1) {
		const obj = array[i];

		if (predicate(obj)) {
			array.splice(i, 1);
			i -= 1;
		}
	}
};

export const getObjectValuesList = (object, keys) => {
	const valuesObject = {};

	keys.forEach((key, id) => {
		const value = object[key];
		const label = object[`${key}Label`];
		const disclaimer = object[`${key}Disclaimer`];

		// Check if label override exists
		if (label && !value) {
			valuesObject[`value${id}`] = object[`${key}Label`];
		} else {
			valuesObject[`value${id}`] = object[key];
		}

		// Add disclaimer if it exists
		if (disclaimer) {
			valuesObject[`value${id}_disclaimer`] = object[`${key}Disclaimer`];
		}
	});

	return valuesObject;
};

export const getCopayTypeValuesList = (object, keys) => {
	const valuesObject = {};
	valuesObject.copayType = object[keys];

	return valuesObject;
};

export const checkIfArrayHasEmptyElements = objectElem => objectElem.every(item => isEmpty(item));

/**
 * Slugifies given string
 *
 * @param {string} text - String that should be slugified
 *
 * @returns {string} Slugified string
 */
export const slugifyString = (text) => {
	return text.toLowerCase()
		.replace(/[^\w ]+/g,'')
		.replace(/ +/g,'-');
}

/**
 * Converts given string to sentence case format
 *
 * @param {string} str - Text that should be converted to sentence case
 * @param {('camel')} [initial] - Initial text format
 *
 * @returns {string|Boolean} Title cased text
 */
export const sentenceCase = (str, initial) => {
	let sCase;

	if (str === null || str === '') {
		return false;
	}

	sCase = str.toString();

	// add spaces between camel cased words
	if (initial && initial === 'camel') {
		sCase = sCase.replace(/([A-Z])/g, ' $1');
	}

	return sCase.replace(/\w\S*/g, txt => txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase());
};

/**
 * Converts given string to camel case format
 *
 * @param {string} str - Text that should be converted to camel case
 *
 * @returns {string|Boolean} Camel cased text
 */
export const camelCase = (str) => {
	let cCase;

	if (str === null || str === '') {
		return false;
	}

	cCase = str.toString();

	// replace dashes with space
	cCase = cCase.replace(/-/g, ' ');

	return cCase.replace(/(?:^\w|[A-Z]|\b\w)/g, (word, index) => (index === 0 ? word.toUpperCase() : word.toUpperCase())).replace(/\s+/g, '');
};

/**
 * Sets a cookie to expire after the current session.
 *
 * @param {String} cname The cookie name.
 * @param {String} cvalue The cookie value.
 */
export const setCookie = (cname, cvalue, days) => {
	let date;
	let cexpires;
	if (days) {
		date = new Date();
		date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
		cexpires = "; expires=" + date.toUTCString();
	} else {
		cexpires = '';
	}
	document.cookie = `${cname}=${cvalue}${cexpires};path=/`;
};

/**
 * Deletes a cookie.
 *
 * @param {String} cname The cookie name.
 */
export const deleteCookie = (cname) => {
	document.cookie = `${cname}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;`;
};

/**
 * Return Whether current site is federal
 *
 * @return {boolean}
 */
export const isFederalSite = () => document.body.classList.contains('federalpage');

/**
 * Returns the value of a cookie with the given name.
 *
 * @param {String} cname The cookie name.
 *
 * @returns {String} The cookie value.
 */
export const getCookie = (cname) => {
	const name = `${cname}=`;
	const decodedCookie = decodeURIComponent(document.cookie);
	const ca = decodedCookie.split(';');

	for (let i = 0; i < ca.length; i += 1) {
		let cs = ca[i];
		while (cs.charAt(0) === ' ') {
			cs = cs.substring(1);
		}
		if (cs.indexOf(name) === 0) {
			return cs.substring(name.length, cs.length);
		}
	}

	return '';
};

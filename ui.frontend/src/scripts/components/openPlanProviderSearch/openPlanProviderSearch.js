import UHCJS from '../../uhcjs';

const SELECTORS = {
	items: '.open-plan-provider-search__item',
};

/**
 * Gets a random number between two given values
 *
 * @param min {number} - Minimum number
 * @param max {number} - Maximum number
 *
 * @return {number} Random value
 */
function getRandomIntInclusive(min, max) {
	const uMin = Math.ceil(min);
	const uMax = Math.floor(max);
	return Math.floor(Math.random() * (uMax - uMin + 1) + uMin);
}

class OpenPlanProviderSearch {
	constructor(elem) {
		this.els = {
			items: null,
			select: null,
			wrapper: elem,
		};

		this.value = null;
		this.visible = null;

		this.init();
	}

	/**
	 * Displays selected plan providers
	 */
	setValue() {
		const newVal = this.els.select.value;

		if (this.value !== newVal) {
			const newVisible = document.getElementById(newVal);

			this.value = newVal;

			// Hide visible, if it exists
			if (this.visible) {
				this.visible.setAttribute('aria-hidden', true);
			}

			this.visible = newVisible;
			this.visible.setAttribute('aria-hidden', false);
		}
	}

	/**
	 * Adds aria polite and label attributes for accessibility
	 */
	injectAlly() {
		let uniqueId;

		// create a unique id
		do {
			uniqueId = `OpenPlanProviderSearch-${getRandomIntInclusive(1000, 9999)}`;
		} while (document.getElementById(uniqueId));

		this.els.items[0].parentElement.id = uniqueId;
		this.els.select.setAttribute('aria-controls', uniqueId);
	}

	/**
	 * Bind event listener to select input
	 */
	bindEventListeners() {
		this.els.select.addEventListener('change', () => this.setValue());
	}

	/**
	 * Bind component elements
	 */
	bindElements() {
		this.els.items = this.els.wrapper.querySelectorAll(SELECTORS.items);
		this.els.select = this.els.wrapper.querySelector('select');
	}

	/**
	 * Init component instance
	 */
	init() {
		this.bindElements();
		this.bindEventListeners();
		this.injectAlly();
		this.setValue();
	}
}

UHCJS.registerComponent({
	name: 'openPlanProviderSearch',
	selector: '.open-plan-provider-search',
	constr: OpenPlanProviderSearch,
});

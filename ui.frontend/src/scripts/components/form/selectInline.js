import UHCJS from '../../uhcjs';

const SELECTORS = {
	select: '.c-select-inline__select',
	text: '.c-select-inline__selected',
};

class SelectInline {
	/**
	 * @param {Element} elem
	 */
	constructor(elem) {
		this.els = {
			select: null,
			text: null,
			wrapper: elem,
		};

		this.text = null;

		this.init();
	}

	/**
	 * Sets display text based on selected option
	 */
	setText() {
		const newText = this.els.select.options[this.els.select.selectedIndex].text;

		if (this.text !== newText) {
			this.els.text.textContent = newText;
		}
	}

	/**
	 * Bind click event to select input
	 */
	bindEventListeners() {
		this.els.select.addEventListener('change', () => this.setText());
	}

	/**
	 * Bind elements
	 */
	bindElements() {
		this.els.select = this.els.wrapper.querySelector(SELECTORS.select);
		this.els.text = this.els.wrapper.querySelector(SELECTORS.text);
	}

	init() {
		this.bindElements();
		this.bindEventListeners();
		this.setText();
	}
}

UHCJS.registerComponent({
	name: 'selectInline',
	selector: '.c-select-inline',
	constr: SelectInline,
});

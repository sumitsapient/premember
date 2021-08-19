import UHCJS from '../../uhcjs';
import { addClass, removeClass } from '../../utils/dom';

const selectors = {
	genericPlanPicker: '.genericPlanPicker',
	genericPlanPickerSelect: '#generic-plan-picker-select',
	genericPlanPickerValue: '.generic-plan-picker__value',
};

const classes = {
	isFocused: 'is-focused',
};

class GenericPlanPicker {
	constructor(elem) {
		this.init(elem);
	}

	init(elem) {
		this.bindElements(elem);
		this.verifyInitialSelectedOption();
		this.bindEvents();
	}

	bindElements(elem) {
		this.$genericPlanSelect = elem.querySelector(selectors.genericPlanPickerSelect);
		console.log(this.$genericPlanSelect);
		this.$genericPlanSelectValue = elem.querySelector(selectors.genericPlanPickerValue);
	}

	verifyInitialSelectedOption() {
		const selectedOption = this.$genericPlanSelect ? Array.from(this.$genericPlanSelect.options).find(option => option.hasAttribute('selected'))
			: null;
		if (selectedOption && this.$genericPlanSelect.value !== selectedOption.value) {
			this.$genericPlanSelect.value = selectedOption.value;
		}
	}

	bindEvents() {
		if (this.$genericPlanSelect) {
			this.$genericPlanSelect.addEventListener('change', this.onChange.bind(this));
			this.$genericPlanSelect.addEventListener('focus', this.onFocus.bind(this));
			this.$genericPlanSelect.addEventListener('blur', this.onBlur.bind(this));
		}
	}

	onChange() {
		window.location.href = this.$genericPlanSelect.value;
	}

	onFocus() {
		addClass(this.$genericPlanSelectValue, classes.isFocused);
	}

	onBlur() {
		removeClass(this.$genericPlanSelectValue, classes.isFocused);
	}
}

UHCJS.registerComponent({
	name: 'genericPlanPicker',
	selector: selectors.genericPlanPicker,
	constr: GenericPlanPicker,
});

import UHCJS from '../../uhcjs';
import { addClass, removeClass } from '../../utils/dom';

const selectors = {
	planPicker: '.planPicker',
	planPickerSelect: '#plan-picker-select',
	planPickerValue: '.plan-picker__value',
};

const classes = {
	isFocused: 'is-focused',
};

class PlanPicker {
	constructor(elem) {
		this.init(elem);
	}

	init(elem) {
		this.bindElements(elem);
		this.verifyInitialSelectedOption();
		this.bindEvents();
	}

	bindElements(elem) {
		this.$planSelect = elem.querySelector(selectors.planPickerSelect);
		this.$planSelectValue = elem.querySelector(selectors.planPickerValue);
	}

	verifyInitialSelectedOption() {
		const selectedOption = this.$planSelect ? Array.from(this.$planSelect.options).find(option => option.hasAttribute('selected'))
			: null;

		if (selectedOption && this.$planSelect.value !== selectedOption.value) {
			this.$planSelect.value = selectedOption.value;
		}
	}

	bindEvents() {
		if (this.$planSelect) {
			this.$planSelect.addEventListener('change', this.onChange.bind(this));
			this.$planSelect.addEventListener('focus', this.onFocus.bind(this));
			this.$planSelect.addEventListener('blur', this.onBlur.bind(this));
		}
	}

	onChange() {
		const selectedOptionText = this.$planSelect.options[this.$planSelect.selectedIndex].label;

		this.$planSelectValue.innerText = selectedOptionText;
		window.location.href = this.$planSelect.value;
	}

	onFocus() {
		addClass(this.$planSelectValue, classes.isFocused);
	}

	onBlur() {
		removeClass(this.$planSelectValue, classes.isFocused);
	}
}

UHCJS.registerComponent({
	name: 'planPicker',
	selector: selectors.planPicker,
	constr: PlanPicker,
});

import UHCJS from '../../uhcjs';
import { isAuthor } from '../../utils/helpers';

const selectors = {
	enrolmentDates: '#hero-enrollment-one',
	enrolmentDates2: '#hero-enrollment-two',
	enrolmentDates3: '#hero-enrollment-three',
	enrolmentDatesContainer: '#hero-enrollment-container',
	noDatesText: '#hero-enrollment-no-dates',
};

const classes = {
	displayNone: 'u-d-none',
	visuallyHidden: 'u-visually-hidden',
};

class HomepageHero {
	constructor(elem) {
		this.hasDates = false;
		this.init(elem);
	}

	init(elem) {
		if (isAuthor()) {
			return;
		}

		this.bindElements(elem);
		this.updateEnrollmentDatesVisibility();

		if (this.hasDates || this.$noDatesText) {
			this.$enrolmentDatesContainer.classList.remove(classes.visuallyHidden);
		}
	}

	bindElements(elem) {
		this.$enrollmentDates = [
			elem.querySelector(selectors.enrolmentDates),
			elem.querySelector(selectors.enrolmentDates2),
			elem.querySelector(selectors.enrolmentDates3),
		];
		this.$enrolmentDatesContainer = elem.querySelector(selectors.enrolmentDatesContainer);
		this.$noDatesText = elem.querySelector(selectors.noDatesText);
	}

	updateEnrollmentDatesVisibility() {
		const dNow = Date.now();

		this.$enrollmentDates.filter(item => item).forEach((date) => {
			const dFrom = parseInt(date.dataset.showEnrollmentFrom, 10);
			const dTo = parseInt(date.dataset.hideEnrollmentAfter, 10);

			if (dNow < dFrom || dNow > dTo) {
				date.classList.add(classes.displayNone);
			} else {
				this.hasDates = true;
			}
		});
	}
}

UHCJS.registerComponent({
	name: 'homepageHero',
	selector: '.homepageHero',
	constr: HomepageHero,
});

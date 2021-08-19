import UHCJS from '../../uhcjs';
import {
	hasClass,
	removeClass,
	addClass,
	setTabInddex,
} from '../../utils/dom';
import constants from '../../utils/constants';
import {
	isAuthor,
	throttle,
	isBreakpointMatched,
	normalizeText,
} from '../../utils/helpers';
import { getHideElementHeight, expandAnimation, collapseAnimation } from '../../utils/animations';

const selectors = {
	accordionTitle: '.accordion-title',
	accordionBodies: '.accordion-body',
	accordionList: '.accordion-list',
	accordionContent: '.accordion-content',
};

const classes = {
	show: 'show',
	active: 'active',
	accordionMobile: 'accordion-mobile-only',
	viewLessLink: 'accordion-view-less',
	btnIcon: 'c-collapse__button-icon',
};

const throttleTime = 16;

class Accordion {
	constructor(elem) {
		this.init(elem);
	}

	init(elem) {
		if (isAuthor()) {
			return;
		}

		if (this.isEmpty(elem)) {
			return;
		}

		this.bindElements(elem);

		if (this.firstElementExpandAsDefault) {
			const firstItemLink = this.$firstAccordionItemTitle.querySelector('button');
			this.expandAccordion(firstItemLink, this.$firstAccordionItemBody);
		}

		if (hasClass(this.$accordionList, classes.accordionMobile) && isBreakpointMatched('medium')) {
			this.removeAllAnalyticsAttributes();
			this.setAriaAttributesOnDesktop();
		}

		this.attachEvents();
	}

	isEmpty(elem) {
		return !elem.querySelector(selectors.accordionList);
	}

	bindElements(elem) {
		this.$element = elem;
		this.$accordionTitles = Array.from(this.$element.querySelectorAll(selectors.accordionTitle));
		this.$accordionBodies = Array.from(this.$element.querySelectorAll(selectors.accordionBodies));
		this.$accordionList = this.$element.querySelector(selectors.accordionList);
		[this.$firstAccordionItemTitle] = this.$accordionTitles;
		[this.$firstAccordionItemBody] = this.$accordionBodies;
		this.firstElementExpandAsDefault = this.$accordionList.dataset.expandDefault === 'true';
	}

	attachEvents() {
		this.$accordionTitles.forEach((item) => {
			item.addEventListener('click', this.toggleAccordion.bind(this));
			item.addEventListener('keydown', (evt) => {
				const enterKeyPressed = evt.keyCode === constants.keyCodes.enterKey;

				if (enterKeyPressed) {
					this.toggleAccordion(evt);
				}
			}, false);
		});

		this.$accordionBodies.forEach((item) => {
			const accordionContentLink = item.querySelector(`.${classes.viewLessLink}`);

			accordionContentLink.addEventListener('click', this.toggleAccordion.bind(this));
			accordionContentLink.addEventListener('keydown', (evt) => {
				const enterKeyPressed = evt.keyCode === constants.keyCodes.enterKey;

				if (enterKeyPressed) {
					this.toggleAccordion(evt);
				}
			}, false);
		});

		window.addEventListener('resize', throttle(() => {
			if (hasClass(this.$accordionList, classes.accordionMobile)) {
				if (!isBreakpointMatched('medium')) {
					this.$accordionTitles.forEach((item) => {
						const accordionContent = item.nextElementSibling;
						const accordionLink = item.querySelector('button');

						setTabInddex(accordionLink, '');

						this.setAnalyticsAttributes(accordionLink);

						if (!hasClass(accordionLink, classes.active)) {
							accordionLink.setAttribute('aria-expanded', 'false');
							accordionLink.removeAttribute('aria-disabled');
							accordionContent.setAttribute('aria-hidden', 'true');
						}
					});

					return;
				}

				this.removeAllAnalyticsAttributes();
				this.setAriaAttributesOnDesktop();
			}
		}, throttleTime), false);
	}

	toggleAccordion(evt) {
		evt.preventDefault();
		let targetElementLink;
		const targetTagName = evt.target.tagName.toLowerCase();
		const isViewLessLink = evt.target.classList.contains(classes.viewLessLink);
		const isBtnIcon = evt.target.classList.contains(classes.btnIcon);

		if (isViewLessLink) {
			targetElementLink = evt.target.parentNode.previousElementSibling.querySelector('button');
		} else if (isBtnIcon) {
			targetElementLink = evt.target.closest('button');
		} else if (targetTagName === 'button') {
			targetElementLink = evt.target;
		} else if (targetTagName === 'path' || targetTagName === 'svg') {
			const parentEl = evt.target.closest('.accordion-view-less');
			targetElementLink = parentEl.parentNode.previousElementSibling.querySelector('button');
		} else {
			targetElementLink = evt.target.querySelector('button');
		}

		const linkHref = targetElementLink.getAttribute('aria-controls');
		const accordionContent = this.$element.querySelector(`#${linkHref}`);

		if (hasClass(accordionContent, classes.show)) {
			this.collapseAccordion(targetElementLink, accordionContent);

			// set focus to heading
			if (isViewLessLink) {
				targetElementLink.focus();
			}
		} else {
			this.expandAccordion(targetElementLink, accordionContent);
		}
	}

	expandAccordion(link, content) {
		const accordionContent = content;
		const accordionContentHeght = getHideElementHeight(accordionContent);
		const contentLink = content.querySelector(`.${classes.viewLessLink}`);

		addClass(link, classes.active);
		addClass(accordionContent, classes.show);

		expandAnimation(accordionContent, accordionContentHeght, constants.transitionTime);

		link.setAttribute('aria-expanded', 'true');

		if (contentLink) {
			contentLink.setAttribute('aria-expanded', 'true');
		}

		accordionContent.setAttribute('aria-hidden', 'false');
	}

	collapseAccordion(link, content) {
		const accordionContent = content;
		const contentLink = content.querySelector(`.${classes.viewLessLink}`);

		collapseAnimation(accordionContent, constants.transitionTime);

		removeClass(link, classes.active);
		link.setAttribute('aria-expanded', 'false');

		if (contentLink) {
			contentLink.setAttribute('aria-expanded', 'false');
		}

		accordionContent.setAttribute('aria-hidden', 'true');
	}

	setAriaAttributesOnDesktop() {
		this.$accordionTitles.forEach((item) => {
			const accordionContent = item.nextElementSibling;
			const accordionLink = item.querySelector('button');
			const contentLink = accordionContent.querySelector(`.${classes.viewLessLink}`);

			setTabInddex(accordionLink, '-1');
			accordionLink.setAttribute('aria-expanded', 'true');

			if (contentLink) {
				contentLink.setAttribute('aria-expanded', 'false');
			}

			accordionLink.setAttribute('aria-disabled', 'true');
			accordionContent.setAttribute('aria-hidden', 'false');
		});
	}

	setAnalyticsAttributes(link) {
		const eventName = normalizeText(link.innerText);

		link.setAttribute('data-event-type', 'accordion');
		link.setAttribute('data-event-name', eventName);
	}

	removeAllAnalyticsAttributes() {
		this.$accordionTitles.forEach((item) => {
			const accordionLink = item.querySelector('button');

			accordionLink.removeAttribute('data-event-type');
			accordionLink.removeAttribute('data-event-name');
		});
	}
}

UHCJS.registerComponent({
	name: 'accordionGroup',
	selector: '.accordionGroup',
	constr: Accordion,
});

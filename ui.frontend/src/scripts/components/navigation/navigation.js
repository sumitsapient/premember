import allyKey from 'ally.js/src/when/key';
import allyDisabled from 'ally.js/src/maintain/disabled';
import allyHidden from 'ally.js/src/maintain/hidden';
import allyFocusable from 'ally.js/src/query/focusable';

import UHCJS from '../../uhcjs';
import {
	throttle,
	isBreakpointMatched,
	isFederalSite,
} from '../../utils/helpers';

import {
	dispatchZipEvent,
	getRegionFromZip,
	getZipCookie,
	setRegionCookie,
	setDentalRegionCookie,
	setZipCookie,
	isZip,
	removeRegionCookie,
} from '../../utils/zip';

const throttleTime = 16;

const selectors = {
	errorField: '.js-zip-search__error',
	menu: '.c-navigation__menu',
	mobileBackBtn: '.c-button--back',
	mobileCloseBtn: '.c-button-close',
	mobileToggleBtn: '.c-navigation__toggle',
	nav: '.c-navigation',
	subMenu: '.c-navigation__sub-menu',
	subMenuBtn: '.c-button-sub-menu',
	zipForm: '#zip-menu-form',
	zipInputField: '#zip-menu-input',
	zipLabel: '.c-navigation__zip-label',
};

const classes = {
	mobileToggleBtn: 'c-navigation__toggle',
	navItem: 'cmp-navigation__item',
	navOverlay: 'c-navigation__overlay',
};

/**
 * Gets transition-duration css property as millisecond value
 *
 * @param el {Element} - DOM Element
 * @param [cssProp='transition-duration'] {string} - CSS property to get
 *
 * @return {number} Animation duration in milliseconds
 */
const getAnimDuration = (el, cssProp = 'transition-duration') => {
	let val = window.getComputedStyle(el).getPropertyValue(cssProp);
	val = val.replace(/[^0-9.,]/g, '');

	if (val) {
		val = parseFloat(val);
		val *= 1000;
	}

	return val;
};

class Navigation {
	constructor() {
		this.classes = {
			ACTIVE: 'is-active',
			DISPLAY_NONE: 'u-d-none',
			ERROR: 'has-error',
			FIXED: 'is-fixed',
			MODAL_OPEN: 'modal-open',
			NAV_OPEN: 'c-navigation--open',
			REVEALED: 'is-revealed',
			SUB_MENU_OPEN: 'c-navigation__menu--sub-menu-open',
		};

		this.str = {
			ERROR_TEXT: 'Please enter a valid ZIP code.',
		};

		this.offsetTop = window.pageYOffset;

		this.state = {
			height: null,
			isDesktop: isBreakpointMatched('large'),
			isFederal: isFederalSite(),
			isOpen: false,
			indexUpdated: false,
			subNav: {
				isOpen: false,
				el: null,
				btn: null,
			},
		};

		this.ally = {
			disabledHandle: null,
			focusableItems: null,
			hiddenHandle: null,
			keyHandle: {
				button: null,
				escape: null,
				first: null,
				last: null,
			},
			tabHandle: null,
		};

		this.init();
	}

	/**
 * Removes the error on the associated input.
 *
 * @param {Element} input The input field.
 * @param {Element} error The error output element.
 */
	removeError(input, error) {
		const errorField = error;
		input.classList.remove(this.classes.ERROR);
		input.setAttribute('aria-invalid', 'false');
		errorField.classList.remove(this.classes.ACTIVE);
		errorField.innerHTML = '';
	}

	/**
	 * Updates the error text and shows the element.
	 *
	 * @param {Element} input The input field.
	 * @param {Element} error The error output element.
	 * @param {String} text The text to display.
	 */
	showError(input, error, text) {
		const errorField = error;
		errorField.innerHTML = text;
		errorField.classList.add(this.classes.ACTIVE);
		input.classList.add(this.classes.ERROR);
		input.setAttribute('aria-invalid', 'true');
	}

	/**
	 * Replace zip code value in header if zip cookie exists
	 *
	 * @param {Number} [zip] - Zip code value
	 */
	replaceZipValue(zip = getZipCookie()) {
		if (zip && this.zipLabels.length > 0) {
			this.zipLabels.forEach((label) => {
				// eslint-disable-next-line no-param-reassign
				label.textContent = zip.toString();
			});

			// fill zip input
			document.querySelector(selectors.zipInputField).value = zip;
		}
	}

	checkZipCode(newUrlString) {
		const zipCheck = newUrlString.slice(newUrlString.length - 5, newUrlString.length);
		let modifiedStr;
		if (zipCheck.match(/^[0-9]*$/gm)) {
			modifiedStr = newUrlString.slice(0, newUrlString.length - 6);
		} else {
			modifiedStr = newUrlString;
		}
		return modifiedStr;
	}

	updateURL(zip) {
		const urlString = window.location.href;
		let updatedZipCodeUrl;

		if (urlString.endsWith('.html')) {
			const	newUrlString = urlString.slice(0, urlString.length - 5);
			// check for zip and return str
			const modifiedStr = this.checkZipCode(newUrlString);

			updatedZipCodeUrl = `${modifiedStr}.${zip}.html`;
		} else {
			const newUrlString = urlString;
			// check for zip and return str
			const modifiedStr = this.checkZipCode(newUrlString);
			updatedZipCodeUrl = `${modifiedStr}.${zip}`;
		}

		window.location.href = updatedZipCodeUrl;
	}

	/**
	 * Handles zip form submission
	 *
	 * @param {Event} event
	 */
	async handleZipFormSubmit(event) {
		event.preventDefault();

		const form = event.target;
		const input = form.querySelector(selectors.zipInputField);
		const error = form.querySelector(selectors.errorField);
		const zip = event.target.elements['zip-code'].value;
		let hasError = false;

		if (zip) {
			if (isZip(zip)) {
				if (zip.match(/^[0-9]*$/gm) && zip.length === 5) {
					const regionData = await getRegionFromZip(zip);

					this.removeError(input, error);

					if (regionData.regionName.length > 0 || zip === '99999') {
						// update zip value in header
						this.replaceZipValue(zip);
						// close menu item
						this.closeAllNavs();
						// set cookie
						setZipCookie(zip);
						// dispatch zip changed event
						dispatchZipEvent(form);

						// set region cookie
						if (regionData.regionName) {
							setRegionCookie(regionData.regionName);
						}

						if (zip === '99999') {
							removeRegionCookie();
						}

						if (regionData.dentalRegionName) {
							setDentalRegionCookie(regionData.dentalRegionName);
						}

						this.updateURL(zip);
					} else {
						hasError = true;
					}
				} else {
					hasError = true;
				}
			} else {
				hasError = true;
			}
		} else {
			hasError = true;
		}

		if (hasError) {
			this.showError(input, error, this.str.ERROR_TEXT);
		}
	}

	/**
	 * Handles nav stickiness on scroll
	 */
	handleScroll() {
		if (window.pageYOffset > 0) {
			this.nav.classList.add(this.classes.FIXED);

			if (window.pageYOffset < this.offsetTop) {
				this.nav.classList.add(this.classes.REVEALED);
			} else {
				this.nav.classList.remove(this.classes.REVEALED);
			}

			this.offsetTop = window.pageYOffset;
		} else {
			this.nav.classList.remove(this.classes.REVEALED);
			this.nav.classList.remove(this.classes.FIXED);
		}
	}

	/**
	 * Releases focus trap on mobile navigation items
	 */
	releaseNavFocusTrap() {
		if (this.ally.keyHandle.escape) {
			this.ally.keyHandle.escape.disengage();
		}

		if (this.ally.keyHandle.button) {
			this.ally.keyHandle.button.disengage();
		}

		if (this.ally.keyHandle.first) {
			this.ally.keyHandle.first.disengage();
		}

		if (this.ally.keyHandle.last) {
			this.ally.keyHandle.last.disengage();
		}

		if (this.ally.hiddenHandle) {
			this.ally.hiddenHandle.disengage();
		}

		if (this.ally.disabledHandle) {
			this.ally.disabledHandle.disengage();
		}

		if (this.ally.focusableItems) {
			this.ally.focusableItems = null;
		}
	}

	/**
	 * Traps focus when mobile navigation is open
	 *
	 * @param el {Element} - Element that should focus should be trapped to
	 * @param [clearExisting] {Boolean} - Clear existing ally services
	 */
	trapNavFocus(el, clearExisting = false) {
		if (clearExisting) {
			this.releaseNavFocusTrap();
		}

		this.ally.focusableItems = allyFocusable({
			context: el,
		});

		// Close nav on escape key press
		this.ally.keyHandle.escape = allyKey({
			escape: () => setTimeout(() => {
				if (this.state.isDesktop) {
					if (this.state.isOpen) {
						this.toggleMobileNav();
					} else {
						this.closeDesktopSubNav(this.state.subNav.btn);
					}
				} else {
					this.toggleMobileNav();
				}
			}),
		});

		this.ally.keyHandle.last = allyKey({
			context: this.ally.focusableItems.lastItem,
			tab: (evt) => {
				evt.preventDefault();

				if (this.state.isDesktop) {
					const btn = this.state.isOpen ? this.btn : this.state.subNav.btn;
					// close sub nav and focus next menu item
					const navFocusable = allyFocusable({
						context: this.nav,
					}).filter(item => !item.closest(selectors.subMenu));

					const navItemIndex = navFocusable.indexOf(btn);
					let nextFocus;

					if ((navItemIndex + 1) <= navFocusable.length) {
						nextFocus = navFocusable[navItemIndex + 1].focus();
					} else {
						nextFocus = btn.focus();
					}

					if (this.state.isOpen) {
						this.toggleMobileNav();
					} else {
						this.closeDesktopSubNav(nextFocus);
					}
				} else {
					// Jump to first focusable item when tabbing forward on last item
					this.ally.focusableItems[0].focus();
				}
			},
		});

		if (this.state.isDesktop) {
			const btn = this.state.isOpen ? this.btn : this.state.subNav.btn;

			// Jump to first focusable sub nav item when tabbing forward from open menu item
			this.ally.keyHandle.button = allyKey({
				context: btn,
				tab: (evt) => {
					evt.preventDefault();
					this.ally.focusableItems[0].focus();
				},
			});

			// Jump to menu item toggle when tabbing backwards from first sub nav item
			this.ally.keyHandle.first = allyKey({
				context: this.ally.focusableItems[0],
				'shift+tab': (evt) => {
					evt.preventDefault();
					btn.focus();
				},
			});
		} else {
			// Make sure that no element outside of the nav can be interacted with while the nav is visible
			this.ally.disabledHandle = allyDisabled({
				filter: el,
			});

			// Make sure that no element outside of the nav is exposed via the Accessibility Tree, to prevent screen readers
			// from navigating to content it shouldn't be seeing while the nav is open
			this.ally.hiddenHandle = allyHidden({
				filter: el,
			});

			// Jump to last focusable item when tabbing backwards from first item
			this.ally.keyHandle.first = allyKey({
				context: this.ally.focusableItems[0],
				'shift+tab': (evt) => {
					evt.preventDefault();
					this.ally.focusableItems.lastItem.focus();
				},
			});
		}
	}

	/**
	 * Toggles transparent content overlay
	 */
	toggleOverlay() {
		if (this.state.isOpen) {
			this.overlay = document.createElement('DIV');
			this.overlay.classList.add(classes.navOverlay);
			this.nav.prepend(this.overlay);
			// ensures animation runs
			setTimeout(() => this.overlay.classList.add(this.classes.ACTIVE), 0);
			// close mobile menu on click
			this.overlay.addEventListener('click', () => this.toggleMobileNav());
			this.overlayDuration = getAnimDuration(this.overlay);
		} else {
			this.overlay.classList.remove(this.classes.ACTIVE);
			setTimeout(() => this.overlay.remove(), this.overlayDuration);
		}
	}

	/**
	 * Toggles mobile navigation
	 *
	 * @param [evt] {Event} - Click event
	 */
	toggleMobileNav(evt) {
		// check if desktop sub nav is open
		if (this.state.isDesktop && this.state.subNav.isOpen) {
			this.closeDesktopSubNav();
		}

		this.state.isOpen = !this.state.isOpen;

		if (!this.state.isDesktop) {
			document.body.classList.toggle(this.classes.MODAL_OPEN);
			this.nav.classList.toggle(this.classes.NAV_OPEN);
			this.toggleOverlay();
		}

		if (this.state.isOpen) {
			this.btn = evt.target;
			this.menu = document.getElementById(this.btn.getAttribute('aria-controls'));
			this.menu.classList.remove(this.classes.DISPLAY_NONE);
			// ensures drawer animation runs
			window.requestAnimationFrame(() => this.menu.classList.add(this.classes.ACTIVE));
			this.trapNavFocus(this.menu);
		} else {
			// close sub nav if open
			if (this.state.subNav.isOpen) {
				this.closeSubNav(true);
			}

			this.menu.classList.remove(this.classes.ACTIVE);

			setTimeout(() => {
				this.menu.classList.add(this.classes.DISPLAY_NONE);
				this.releaseNavFocusTrap();
				this.btn.setAttribute('aria-expanded', 'false');

				if (!this.state.isDesktop) {
					this.btn.focus();
				}
			}, this.drawerDuration);
		}
	}

	/**
	 * Close open sub nav menu
	 *
	 * @param [hardClose] {Boolean} - Close sub nav without trapping focus and reopening main nav
	 */
	closeSubNav(hardClose = false) {
		const button = this.state.subNav.btn;
		const menu = this.state.subNav.el;
		const targetId = button.getAttribute('aria-controls');

		if (!button && !menu) {
			return;
		}

		// set own props
		this.state.subNav.isOpen = false;
		this.state.subNav.btn = null;
		this.state.subNav.el = null;

		if (menu) {
			if (!this.state.isDesktop && this.menu) {
				this.menu.classList.remove(this.classes.SUB_MENU_OPEN);
			}

			menu.classList.remove(this.classes.ACTIVE);

			setTimeout(() => menu.classList.add(this.classes.DISPLAY_NONE), this.drawerDuration);
		}

		if (button) {
			button.classList.remove(this.classes.ACTIVE);
			button.parentNode.classList.remove(this.classes.ACTIVE);

			// update aria expanded attribute for all buttons that control sub nav
			document.querySelectorAll(`[aria-controls="${targetId}"]`).forEach(
				btn => btn.setAttribute('aria-expanded', 'false'),
			);

			if (!hardClose) {
				button.focus();
			}
		}
	}

	/**
	 * Close sub nav and release focus traps
	 *
	 * @param [focus] {Element} - Element to focus on
	 */
	closeDesktopSubNav(focus) {
		if (focus) {
			focus.focus();
		}

		this.closeSubNav(true);
		this.releaseNavFocusTrap();
	}

	/**
	 * Open sub nav menu
	 *
	 * @param {Element|EventTarget} button - Button that should have it's menu opened
	 */
	openSubNav(button) {
		const targetId = button.getAttribute('aria-controls');
		const subMenu = document.getElementById(targetId);

		// close other open menus
		if (this.state.subNav.btn) {
			this.closeSubNav();
		}

		// set own props
		this.state.subNav.isOpen = true;
		this.state.subNav.btn = button;
		this.state.subNav.el = subMenu;

		// set button options
		button.classList.add(this.classes.ACTIVE);
		button.parentNode.classList.add(this.classes.ACTIVE);

		// update aria expanded attribute for all buttons that control sub nav
		document.querySelectorAll(`[aria-controls="${targetId}"]`).forEach(
			btn => btn.setAttribute('aria-expanded', 'true'),
		);

		// open menu
		subMenu.classList.remove(this.classes.DISPLAY_NONE);

		if (!this.state.isDesktop && this.menu) {
			this.menu.classList.add(this.classes.SUB_MENU_OPEN);
		}

		// timeout ensures animation runs
		setTimeout(() => subMenu.classList.add(this.classes.ACTIVE), 0);

		// trap focus after animation is complete
		setTimeout(() => {
			this.trapNavFocus(this.state.isDesktop ? subMenu : subMenu.closest(selectors.menu), true);

			// set focus on first menu item
			if (!this.state.isDesktop) {
				const focusEl = this.ally.focusableItems;

				if (focusEl && focusEl?.length > 0) {
					focusEl[0].focus();
				}
			}
		}, this.drawerDuration);
	}

	/**
	 * Toggles sub nav on click
	 *
	 * @param evt {Event} - Click event
	 */
	handleSubNavToggleClick(evt) {
		evt.preventDefault();

		// check if zip menu is open
		if (this.state.isFederal && this.state.isOpen && this.state.isDesktop) {
			this.toggleMobileNav();
		}

		this.state.subNav.isOpen = !this.state.subNav.isOpen;

		if (this.state.subNav.isOpen) {
			this.openSubNav(evt.target);
		} else if (this.state.isDesktop) {
			this.closeDesktopSubNav();
		} else {
			this.closeSubNav();
		}
	}

	/**
	 * Closes all navs
	 */
	closeAllNavs() {
		if (this.state.isOpen) {
			// Toggles nav drawer on click
			this.toggleMobileNav();
		} else if (this.state.subNav.isOpen) {
			this.closeDesktopSubNav();
		}
	}

	/**
	 * Close all navs and update isDesktop state prop
	 */
	handleResize() {
		this.state.isDesktop = isBreakpointMatched('large');
		this.closeAllNavs();
	}

	/**
	 * Determines if current url is root of site
	 *
	 * @return {boolean}
	 */
	isRoot() {
		const path = window.location.pathname;
		const pathArr = path.split('/');
		let root = false;

		if (path === '/') {
			root = true;
		}

		if (!root && pathArr.includes('home.html')) {
			root = true;
		}

		if (!root && pathArr[pathArr.length - 1] === 'home') {
			root = true;
		}

		return root;
	}


	bindElements() {
		// const navItemLinksList = document.querySelectorAll(selectors.navItemLink);

		this.nav = document.querySelector(selectors.nav);
		this.mobileBackBtn = this.nav.querySelector(selectors.mobileBackBtn);
		this.mobileCloseBtns = this.nav.querySelectorAll(selectors.mobileCloseBtn);
		this.mobileToggleBtns = this.nav.querySelectorAll(selectors.mobileToggleBtn);
		this.subMenuBtns = this.nav.querySelectorAll(selectors.subMenuBtn);
		this.zipForm = this.nav.querySelector(selectors.zipForm);
		this.zipLabels = this.nav.querySelectorAll(selectors.zipLabel);

		// Get animation durations
		this.drawerDuration = getAnimDuration(this.nav.querySelector(selectors.menu));
	}

	bindEvents() {
		// Toggle mobile navs
		if (this.mobileToggleBtns && this.mobileToggleBtns.length > 0) {
			this.mobileToggleBtns.forEach((toggle) => {
				toggle.addEventListener('click', this.toggleMobileNav.bind(this));
			});
		}
		// Close mobile menu on close button click
		if (this.mobileCloseBtns && this.mobileCloseBtns.length > 0) {
			this.mobileCloseBtns.forEach((toggle) => {
				toggle.addEventListener('click', this.toggleMobileNav.bind(this));
			});
		}
		// Check if any sub nav toggle buttons exist before binding
		if (this.subMenuBtns && this.subMenuBtns.length > 0) {
			this.subMenuBtns.forEach((toggle) => {
				// Toggles sub nav on click
				toggle.addEventListener('click', this.handleSubNavToggleClick.bind(this));
			});
		}
		// Sub menu back button
		this.mobileBackBtn.addEventListener('click', () => this.closeSubNav());
		// Handle zip form submit
		if (this.zipForm) {
			this.zipForm.addEventListener('submit', this.handleZipFormSubmit.bind(this));
		}
		// handles nav stickiness on scroll
		// window.addEventListener('scroll', throttle(this.handleScroll.bind(this), throttleTime));
		// Handles opening and closing nav on window resize
		window.addEventListener('resize', throttle(this.handleResize.bind(this), throttleTime));
		// Close nav when event is dispatched
		document.addEventListener('anchor:close:navigation', this.closeAllNavs.bind(this));
		// Displays hidden sticky nav when nav item is focused
		// this.nav.addEventListener('focusin', () => {
		// 	if (!this.nav.classList.contains(this.classes.REVEALED)) {
		// 		this.nav.classList.add(this.classes.REVEALED);
		// 	}
		// });
	}

	/**
	 * Initialize zip code functionality
	 */
	initZip() {
		const zipCookie = getZipCookie();

		// Only run the replaceZipValue function if the zip is displayed
		if (this.zipLabels) {
			this.replaceZipValue();
		}

		// Open zip code sub nav if no zip detected
		if (this.state.isFederal && this.isRoot() && !zipCookie) {
			const zipCodeBtn = document.querySelector('.c-navigation__toggle--zip');
			zipCodeBtn.click();
		}
	}

	init() {
		this.bindElements();
		this.bindEvents();
		this.handleResize();
		this.initZip();
	}
}

UHCJS.registerComponent({
	name: 'navigation',
	selector: selectors.nav,
	constr: Navigation,
});

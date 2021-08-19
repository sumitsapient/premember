import UHCJS from '../../uhcjs';

const selectors = {
	skipLink: '.skipContentLink',
	skipLinkAnchor: '.skipContentAnchor',
	homeHero: '.hero--home',
	container: '.o-aem-container',
	federalPage: '.federalpage',
	navigation: '.c-navigation',
	gridRoot: '.responsivegrid',
	anchorNav: '.anchorNavigation',
};

class SkipContentLink {
	constructor(elem) {
		this.isFederal = document.querySelectorAll(selectors.federalPage).length > 0;
		this.init(elem);
	}

	init(elem) {
		this.bindElements(elem);
		this.bindEventListeners(elem);

		if (!this.$firstContainer && !this.$homeHero) {
			return;
		}

		this.createAnchorToContainer();
		this.addRoleMainToContainer();
	}

	bindElements(elem) {
		this.$skipLink = elem.querySelector('a');
		if (this.isFederal) {
			let container = document.querySelector(selectors.navigation);

			// get first non-empty container after navigation
			do {
				container = container.nextElementSibling;
			} while (container.nextElementSibling && container.children.length < 1);

			this.$firstContainer = container;
		} else {
			this.$firstContainer = document.querySelector(selectors.container);
		}
		this.$homeHero = document.querySelector(selectors.homeHero);
	}

	bindEventListeners(elem) {
		this.$skipLink.addEventListener('focus', () => {
			elem.classList.remove('no-pointer');
		});
		this.$skipLink.addEventListener('blur', () => {
			elem.classList.add('no-pointer');
		});
	}

	createAnchorToContainer() {
		if (this.$homeHero) {
			this.$skipLink.href = `#${this.$homeHero.id}`;
			return;
		}

		const formattedHref = this.$skipLink.getAttribute('href').split('#')[1];
		this.$firstContainer.id = formattedHref;
	}

	addRoleMainToContainer() {
		let mainContainer;

		if (this.$homeHero) {
			mainContainer = this.$homeHero.closest(selectors.gridRoot);
		} else {
			mainContainer = this.$firstContainer;
		}

		if (mainContainer.classList.contains(selectors.anchorNav)) {
			mainContainer = mainContainer.nextElementSibling;
		}

		mainContainer.setAttribute('role', 'main');
	}
}

UHCJS.registerComponent({
	name: 'skipContentLink',
	selector: selectors.skipLink,
	constr: SkipContentLink,
});

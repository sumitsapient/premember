import UHCJS from '../../uhcjs';
import dispathEvent from '../../utils/event';

const selectors = {
	tabItem: '.cmp-tabs__tab',
	tabsPanel: '.cmp-tabs__tabpanel',
};

class Tabs {
	constructor(elem) {
		this.init(elem);
	}

	init(elem) {
		const windowHash = window.location.hash.substr(1);

		this.bindElements(elem);
		this.bindEvents();
		this.setAriaAttributes();

		if (windowHash) {
			this.toggleTabOnAnchor(windowHash);
		}
	}

	bindElements(elem) {
		this.$element = elem;
		this.$tabItems = Array.from(this.$element.querySelectorAll(selectors.tabItem));
		this.$tabsPanels = Array.from(this.$element.querySelectorAll(selectors.tabsPanel));
	}

	bindEvents() {
		window.addEventListener('hashchange', (evt) => {
			dispathEvent('anchor:close:navigation');
			const windowHash = evt.currentTarget.location.hash.substr(1);
			this.toggleTabOnAnchor(windowHash);
		});
	}

	setAriaAttributes() {
		this.$tabItems.forEach((item, index) => {
			const itemIDValue = item.id;
			const itemAriaControlValue = item.getAttribute('aria-controls');

			this.$tabsPanels[index].setAttribute('aria-labelledby', itemIDValue);
			this.$tabsPanels[index].id = itemAriaControlValue;
		});
	}

	toggleTabOnAnchor(hash) {
		const anchorItem = this.$element.querySelector(`#${hash}`);
		anchorItem.click();
	}
}

UHCJS.registerComponent({
	name: 'tabs',
	selector: '.tabs',
	constr: Tabs,
});

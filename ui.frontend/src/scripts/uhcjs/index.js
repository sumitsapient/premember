/**
 * UHCJS framework
 */
import Registry from './core/registry';

class UHCJS {
	constructor() {
		this.registry = Registry;
		this.initializedClass = 'initialized-uhc';
	}

	registerComponent(module) {
		this.registry.registerComponent(module);
	}

	init() {
		this.initComponents();
	}

	initComponents() {
		const reg = this.registry.getRegistry();

		Object.keys(reg).some((key) => {
			const component = reg[key];
			const elements = document.querySelectorAll(`${component.selector}:not(${this.initializedClass})`);

			if (elements.length === 0) {
				return;
			}

			Object.keys(elements).some((index) => {
				const el = elements[index];
				const componentClass = component.name.replace(/([A-Z])/g, $1 => `-${$1.toLowerCase()}`);

				if (el.classList.contains(`${this.initializedClass}-${componentClass}`)) {
					return;
				}

				el.classList.add(`${this.initializedClass}-${componentClass}`);
				new component.constr(el); // eslint-disable-line
			});
		});
	}
}

const instance = new UHCJS();

export default instance;

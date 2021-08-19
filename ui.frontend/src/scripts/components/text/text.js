import UHCJS from '../../uhcjs';
import { normalizeText, isEmpty } from '../../utils/helpers';
import { hasClass } from '../../utils/dom';

const selectors = {
	text: '.aem-component-rte',
	cmpText: '.cmp-text',
	textOneList: 'aem-component-rte--one-list',
	pdfLink: 'hyperlink--pdf',
};

class Text {
	constructor(elem) {
		this.init(elem);
	}

	init(elem) {
		this.bindElements(elem);

		if (!isEmpty(this.textLists)) {
			this.mergeListToOne(this.cmpText, this.textLists, this.textListsItems);
		}

		if (this.textLinks.length === 0) {
			return;
		}

		this.setAnalyticsAttributes(this.textLinks);
	}

	bindElements(elem) {
		this.textLinks = Array.from(elem.querySelectorAll('a'));
		this.hasOneListClass = elem.classList.contains(`${selectors.textOneList}`);

		if (this.hasOneListClass) {
			this.cmpText = elem.querySelector(`${selectors.cmpText}`);
			this.textLists = Array.from(elem.querySelectorAll('ul'));
			this.textListsItems = Array.from(elem.querySelectorAll('li'));
		}
	}

	setAnalyticsAttributes(list) {
		list.forEach((item) => {
			const linkText = normalizeText(item.innerText);
			const hasPdfClass = hasClass(item.parentNode, selectors.pdfLink);

			if (hasPdfClass) {
				item.setAttribute('aria-label', `${item.textContent} PDF`);
			}

			item.setAttribute('data-event-type', 'text');
			item.setAttribute('data-event-name', linkText);
		});
	}

	mergeListToOne(elem, list, listItems) {
		const newList = document.createElement('ul');
		const elemHeader = elem.querySelector('h4');

		listItems.forEach((item) => {
			newList.appendChild(item);
		});

		list.forEach((item) => {
			elem.removeChild(item);
		});

		if (elemHeader) {
			elem.insertBefore(newList, elemHeader.nextSibling);
		} else {
			elem.appendChild(newList);
		}
	}
}

UHCJS.registerComponent({
	name: 'Text',
	selector: selectors.text,
	constr: Text,
});

export function hasClass(element, selector) {
	return element.classList.contains(selector);
}

export function addClass(element, selector) {
	// check if element exists before adding class
	if (element) {
		element.classList.add(selector);
	}
}

export function toggleClass(element, selector) {
	// check if element exists before toggling class
	if (element) {
		element.classList.toggle(selector);
	}
}

export function removeClass(element, selector) {
	// check if element exists before removing class
	if (element) {
		element.classList.remove(selector);
	}
}

export function setTabInddex(elem, value) {
	const element = elem;
	element.tabIndex = value;
}

import { removeClass } from './dom';

export function getHideElementHeight(elem) {
	const element = elem;
	element.style.display = 'block';

	const newHeight = `${element.scrollHeight}px`;
	element.style.display = '';

	return newHeight;
}

export function expandAnimation(content, contentHeight, time) {
	const contentElem = content;
	/*
		* Set initial '0' height for element and then change it to proper value
		* requestAnimationFrame is necessary here because without timeout browser won’t detect the change at all
		* Needed for IE11
	*/
	contentElem.style.height = 0;
	requestAnimationFrame(() => {
		contentElem.style.height = contentHeight;
	});

	/*
		* Once the transition is complete, remove the inline height so the content can scale responsively
		* setTimeout time is the same as transition time in styles
	*/
	window.setTimeout(() => {
		contentElem.style.height = '';
	}, time);
}

export function collapseAnimation(content, time) {
	const contentElem = content;
	/*
		* Give element height to change from and then set the height back to 0
		* requestAnimationFrame is necessary here because without timeout browser won’t detect the change at all
		* Needed for IE11
	*/
	contentElem.style.height = `${contentElem.scrollHeight}px`;
	requestAnimationFrame(() => {
		contentElem.style.height = '0';
	});

	/*
		* Once the transition is complete, remove the inline height and show class
		* setTimeout time is the same as transition time in styles
	*/
	window.setTimeout(() => {
		contentElem.style.height = '';
		removeClass(contentElem, 'show');
	}, time);
}

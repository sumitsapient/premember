export default function dispatchEvent(customEvent) {
	const Event = document.createEvent('HTMLEvents');
	if (customEvent) {
		Event.initEvent(customEvent, true, true);
		document.dispatchEvent(Event);
	}
}

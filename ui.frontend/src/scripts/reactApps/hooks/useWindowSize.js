import { useState, useLayoutEffect } from 'react';
import constants from '../../utils/constants';

const getWidth = () => window.innerWidth
	|| document.documentElement.clientWidth
	|| document.body.clientWidth;

export default function useWindowSize() {
	const [width, setWidth] = useState(getWidth());

	useLayoutEffect(() => {
		let timeoutId = null;

		const resizeListener = () => {
			clearTimeout(timeoutId);
			timeoutId = setTimeout(() => setWidth(getWidth()), 150);
		};

		window.addEventListener('resize', resizeListener);

		return () => {
			window.removeEventListener('resize', resizeListener);
		};
	});

	return width < constants.breakpoints.medium;
}

import React, { useEffect, useState, useRef } from 'react';
import { normalizeText, isBreakpointMatched } from '../../utils/helpers';

/**
 * The largest level heading that should be included in component (1 being highest)
 * * Must be <= 1
 * * Must be >= 6
 * * Must be >= to HEADING_TARGET_MIN
 *
 * @example
 * // targets headings between h2 and h4
 * const HEADING_TARGET_MAX = 2
 * const HEADING_TARGET_MIN = 4
 *
 * @type {number}
 */
const HEADING_TARGET_MAX = 2;

/**
 * The smallest level heading that should be included in component (6 being lowest)
 * * Must be <= 1
 * * Must be >= 6
 * * Must be <= to HEADING_TARGET_MAX
 *
 * @see HEADING_TARGET_MAX - for usage example
 * @type {number}
 */
const HEADING_TARGET_MIN = 2;

/**
 * Text that should be displayed above anchor links, as a label
 *
 * @type {string}
 */
const LABEL_TEXT = 'On this Page';

/**
 * Returns all heading elements between given numbers
 *
 * @param min {number}
 * @param max {number}
 *
 * @return {Array<Element>}
 */
const getHeadingEls = (min, max) => {
	let selector = '';

	for (let i = max; i <= min; i += 1) {
		if (i > max) {
			selector += ', ';
		}

		selector += `h${i}.cmp-title__text`;
	}

	const headings = document.querySelectorAll(selector);

	return headings.length > 0 ? Array.prototype.slice.call(headings) : null;
};

/**
 * Returns an array of heading positions relative to the page
 *
 * @param items {Array<Element>}
 *
 * @return {Array<number>}
 */
const getHeadingPositions = items => items.map((item) => {
	const docScrollPosition = document.documentElement.scrollTop || document.body.scrollTop;

	return item.getBoundingClientRect().top + docScrollPosition - 16;
});

const createGrid = () => {
	const anchor = document.querySelector('.anchorNavigation');
	const content = anchor.nextElementSibling;

	if (content.classList.contains('responsivegrid')) {
		const grid = document.createElement('DIV');
		const columnRight = content.querySelector('.aem-Grid');

		anchor.classList.add('o-grid__item-12', 'o-grid__item-3@lg', 'u-p-t-xl');
		content.classList.add('o-container', 'o-container--reset-inner');
		columnRight.classList.add('o-grid__item');
		grid.classList.add('o-grid');

		grid.appendChild(anchor);
		grid.appendChild(columnRight);
		content.appendChild(grid);
	}
};

const AnchorNavigation = () => {
	const [items] = useState(getHeadingEls(HEADING_TARGET_MIN, HEADING_TARGET_MAX));

	if (!items) {
		return false;
	}

	const [positions] = useState(getHeadingPositions(items));
	const [selected, setSelected] = useState(0);
	const el = useRef(null);

	createGrid();

	const handleScrollSpy = () => {
		const docScrollPosition = document.documentElement.scrollTop || document.body.scrollTop;
		let newSelected = selected;

		for (let i = 0; i < positions.length; i += 1) {
			if (docScrollPosition >= positions[i]) {
				newSelected = i;
			}
		}

		if (newSelected !== selected) {
			setSelected(newSelected);
		} else if ((window.innerHeight + window.scrollY) >= document.body.offsetHeight) {
			// check if at bottom of page
			setSelected(positions.length - 1);
		}
	};

	useEffect(() => {
		if (isBreakpointMatched('large')) {
			window.addEventListener('scroll', handleScrollSpy, { passive: true });
		}

		return () => window.removeEventListener('scroll', handleScrollSpy);
	}, [selected]);

	return (
		<>
			<p
				className="u-text-bold u-color-blue"
				id="anchor-nav-label"
				ref={ el }
			>
				{ LABEL_TEXT }
			</p>
			<nav aria-labelledby="anchor-nav-label">
				<ul className="c-anchor-nav__list">
					{ items.map((item, index) => (
						<li
							className={ `c-anchor-nav__list-item${index === selected ? ' c-anchor-nav__list-item--active' : ''}` }
							key={ index }
						>
							<a
								className="c-anchor-nav__list-link"
								data-event-name={ normalizeText(item.innerText) }
								data-event-type="anchor"
								href={ `#${item.parentElement.id}` }
								onClick={ () => setSelected(index) }
							>
								{ item.innerText }
							</a>
						</li>
					)) }
				</ul>
			</nav>
		</>
	);
};

export default AnchorNavigation;

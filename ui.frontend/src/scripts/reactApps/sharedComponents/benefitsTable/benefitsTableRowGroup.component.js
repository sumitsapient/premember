import React, { useState } from 'react';
import PropTypes from 'prop-types';

/**
 * Generates random integer
 *
 * @param {number} min - Minimum number
 * @param {number} max - Maximum number
 * @returns {number} Random number
 */
const getRandomInt = (min, max) => {
	const cMin = Math.ceil(min);
	const fMax = Math.floor(max);
	return Math.floor(Math.random() * (fMax - cMin + 1) + cMin);
};

/**
 * Generates Id
 *
 * @param {string} [slugify] - String that should be slugified and used in Id
 */
const generateId = (slugify) => {
	let id = 'collapse';

	if (slugify) {
		id += `-${slugify.replace(/\s+/g, '-').toLowerCase()}`;
	}

	// add random number
	id += `-${getRandomInt(1000, 9999)}`;

	return id;
};

const BenefitsTableRowGroup = ({
	children,
	collapse,
	heading,
}) => {
	const [isCollapsed, setIsCollapsed] = useState(false);

	const bodyClasses = [
		'benefits-table__row-group',
		collapse ? 'benefits-table__row-group--collapse collapse' : null,
		collapse && isCollapsed ? null : 'u-d-block',
	];

	const id = generateId(heading);

	/**
	 * Toggles collapse
	 *
	 * @param {Event} evt - Click event
	 */
	const handleCollapseToggle = (evt) => {
		evt.preventDefault();

		setIsCollapsed(!isCollapsed);
	};

	/**
	 * Handles key press on collapse
	 *
	 * @param {Event} evt - Keydown event
	 */
	const handleKeyDown = (evt) => {
		if (evt.key === ' ') {
			evt.preventDefault();
			setIsCollapsed(!isCollapsed);
		}
	};

	/**
	 * Renders collapse heading
	 */
	const renderCollapseHeading = () => (
		<div className="benefits-table__collapse-header">
			<button
				className={ `c-collapse ${isCollapsed ? 'collapsed' : ''}` }
				data-toggle="collapse"
				data-target={ `#${id}` }
				aria-controls={ id }
				aria-expanded={ !isCollapsed }
				onClick={ handleCollapseToggle }
				onKeyDown={ handleKeyDown }
				type="button"
			>
				<span className="c-collapse__button-text">
					{ heading }
				</span>
				<span className="o-svg-icon">
					<svg xmlns="http://www.w3.org/2000/svg" focusable="false" role="presentation" viewBox="0 0 32 32">
						<path d="M16 32a16 16 0 1116-16 16 16 0 01-16 16zm0-31a15 15 0 1015 15A15 15 0 0016 1zm0 10.5l-6.35 6.35a.5.5 0 000 .71.48.48 0 00.7 0L16 12.91l5.65 5.65a.48.48 0 00.7 0 .5.5 0 000-.71z" fill="currentColor" />
					</svg>
				</span>
			</button>
		</div>
	);

	return (
		<>
			{ heading && renderCollapseHeading() }
			<div
				id={ collapse ? id : undefined }
				className={ bodyClasses.join(' ') }
				role="rowgroup"
			>
				{ children }
			</div>
		</>
	);
};

BenefitsTableRowGroup.propTypes = {
	children: PropTypes.oneOfType([
		PropTypes.any,
	]),
	collapse: PropTypes.bool,
	heading: PropTypes.string,
};

BenefitsTableRowGroup.defaultProps = {
	children: null,
	collapse: false,
	heading: null,
};

export default BenefitsTableRowGroup;

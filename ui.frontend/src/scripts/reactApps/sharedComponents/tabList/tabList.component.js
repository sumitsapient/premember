import React, { useEffect, useState } from 'react';
import PropTypes from 'prop-types';

// import TabListHeading from './tabListHeading.component';
// import TabListPanel from './tabListPanel.component';

import { keyCodes } from '../../../utils/constants';
import { slugifyString } from '../../../utils/helpers';

const Panel = () => null;
Panel.displayName = 'Panel';

/**
 * Find children that match given component type
 *
 * @param {object} children - React component children prop
 * @param {object} component - React component
 *
 * @returns {array} Array containing mathcing child components
 */
const findByType = (children, component) => {
	const results = [];
	const type = [component.displayName] || [component.name];

	React.Children.forEach(children, (child) => {
		const childType = child && child.type && (child.type.displayName || child.type.name);

		if (type.includes(childType)) {
			results.push(child);
		}
	});

	return results;
};

const TabList = ({
	children,
}) => {
	const [selected, setSelected] = useState(null);

	const panels = findByType(children, Panel);
	const labels = panels.map(panel => panel.props.tabLabel);
	const slugifiedIdRoots = labels.map(label => `tab_${slugifyString(label)}`);

	// set selected panel
	useEffect(() => {
		panels.forEach((panel, index) => {
			if (panel.props.selected) {
				setSelected(slugifiedIdRoots[index]);
			}
		});
	}, []);

	/**
	 * Handles switching tabs on arrow key press
	 *
	 * @param {Event} evt - Keydown event
	 */
	const handleKeyDown = (evt) => {
		const { keyCode } = evt;
		const currIndex = slugifiedIdRoots.indexOf(selected);
		let nextIndex = -1;

		// move to next tab
		if (keyCode === keyCodes.arrowRightKey || keyCode === keyCodes.arrowDownKey) {
			nextIndex = currIndex + 1;
		} else if (keyCode === keyCodes.arrowLeftKey || keyCode === keyCodes.arrowUpKey) {
			// move to previous tab
			nextIndex = currIndex - 1;
		}

		if (nextIndex >= 0 && slugifiedIdRoots[nextIndex] != null) {
			evt.preventDefault();
			setSelected(slugifiedIdRoots[nextIndex]);
			document.getElementById(`${slugifiedIdRoots[nextIndex]}-tab`).focus();
		}
	};

	/**
	 * Renders heading tabs
	 */
	const renderHeading = () => (
		<ul
			role="tablist"
			className="cmp-tabs__tablist"
		>
			{ labels.map((label, index) => {
				const idRoot = slugifiedIdRoots[index];
				const isSelected = selected === idRoot;

				return (
					<li
						className={ `cmp-tabs__tab u-p-0${isSelected ? ' cmp-tabs__tab--active' : ''}` }
						key={ `${idRoot}-tab` }
					>
						<a
							href={ `#${idRoot}-tabpanel` }
							id={ `${idRoot}-tab` }
							role="tab"
							aria-controls={ `${idRoot}-tabpanel` }
							aria-selected={ isSelected }
							onClick={ (evt) => {
								evt.preventDefault();
								setSelected(idRoot);
							} }
							onKeyDown={ handleKeyDown }
						>
							{ label }
						</a>
					</li>
				);
			}) }
		</ul>
	);

	/**
	 * Render panels
	 */
	const renderPanels = () => panels.map((panel, index) => {
		const idRoot = slugifiedIdRoots[index];
		const isSelected = selected === idRoot;

		return (
			<div
				id={ `${idRoot}-tabpanel` }
				className={ `cmp-tabs__tabpanel${isSelected ? ' cmp-tabs__tabpanel--active' : ''}` }
				role="tabpanel"
				aria-labelledby={ `${idRoot}-tab` }
				tabIndex="0"
				key={ `${idRoot}-panel` }
			>
				{	panel.props.children }
			</div>
		);
	});

	return (
		<div className="cmp-tabs">
			{ renderHeading() }
			{ renderPanels() }
		</div>
	);
};

TabList.Panel = Panel;

TabList.propTypes = {
	children: PropTypes.oneOfType([
		PropTypes.any,
	]).isRequired,
};

export default TabList;

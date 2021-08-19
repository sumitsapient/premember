import React from 'react';
import PropTypes from 'prop-types';

import { slugifyString } from '../../../utils/helpers';

const TabListPanel = ({
	children,
	tabLabel,
}) => {
	const slug = slugifyString(tabLabel);
	const idRoot = `tab_${slug.toLowerCase().trim()}`;

	return (
		<div id={ `${idRoot}-tabpanel` } role="tabpanel" aria-labelledby={ `${idRoot}-tab` } tabIndex="0" className="cmp-tabs__tabpanel cmp-tabs__tabpanel--active" data-cmp-hook-tabs="tabpanel">
			{	children }
		</div>
	);
};

TabListPanel.displayName = 'TabListPanel';

TabListPanel.propTypes = {
	children: PropTypes.oneOfType([
		PropTypes.any,
	]).isRequired,
	tabLabel: PropTypes.string.isRequired,
};

export default TabListPanel;

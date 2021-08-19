import React from 'react';
import PropTypes from 'prop-types';

import { slugifyString } from '../../../utils/helpers';

const TabListingItem = ({
	idRoot,
	label,
}) => (
	<li
		id={ `${idRoot}-tab` }
		className="cmp-tabs__tab cmp-tabs__tab--active"
		role="tab"
		aria-controls={ `${idRoot}-tabpanel` }
		aria-selected="true"
		tabIndex="0"
		data-cmp-hook-tabs="tab"
		data-event-type="tab"
		data-event-name={ idRoot }
	>
		{ label }
	</li>
);

TabListingItem.propTypes = {
	idRoot: PropTypes.string.isRequired,
	label: PropTypes.string.isRequired,
};

const TabListHeading = ({
	labels,
}) => (
	<ol role="tablist" className="cmp-tabs__tablist" aria-multiselectable="false">
		{ labels.map((label) => {
			const slug = slugifyString(label);
			const idRoot = `tab_${slug.toLowerCase().trim()}`;

			return (
				<TabListingItem idRoot={ idRoot } label={ label } key={ idRoot } />
			);
		}) }
	</ol>
);

TabListHeading.propTypes = {
	labels: PropTypes.arrayOf(PropTypes.string).isRequired,
};

export default TabListHeading;

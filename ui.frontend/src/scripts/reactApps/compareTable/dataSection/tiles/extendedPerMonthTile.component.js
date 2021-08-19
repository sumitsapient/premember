import React from 'react';
import PropTypes from 'prop-types';
import { ReactFromHtml } from '@bitjourney/react-from-html';
import { isEmpty } from '../../../../utils/helpers';
import { pluralizeText } from '../../../../utils/plan';

const reactFromHtml = new ReactFromHtml();

const getFilteredText = (data) => {
	const cleaningCount = data.value1 ? pluralizeText(data.value1, 'cleaning') : 'N/A cleanings';
	const cleaningTime = data.value2 ? pluralizeText(data.value2, 'month') : 'N/A months';
	const filteredText = data.value0.replace(/#\[([^#]+)\]/, cleaningCount).replace(/#\[([^#]+)\]/, cleaningTime);

	return reactFromHtml.parseToNodeList(filteredText);
};

const ExtendedPerMonthTile = ({ data }) => (
	!isEmpty(data) && (
		isEmpty(data.value0) ? (
			<p>
				<strong aria-hidden="true">N/A</strong>
				<span className="u-visually-hidden">Not Applicable</span>
			</p>
		) : getFilteredText(data)
	)
);

ExtendedPerMonthTile.defaultProps = {
	data: {},
};

ExtendedPerMonthTile.propTypes = {
	data: PropTypes.objectOf(PropTypes.string),
};

export default ExtendedPerMonthTile;

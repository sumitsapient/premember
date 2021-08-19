import React from 'react';
import PropTypes from 'prop-types';
import { ReactFromHtml } from '@bitjourney/react-from-html';
import { isEmpty } from '../../../../utils/helpers';
import { pluralizeText } from '../../../../utils/plan';

const reactFromHtml = new ReactFromHtml();

const ExtendedPerMonthTile = ({ data, rowID, className }) => {
	const renderContent = () => {
		if (isEmpty(data.value0)) {
			return (
				<p>
					<strong aria-hidden="true">N/A</strong>
					<span className="u-visually-hidden">Not Applicable</span>
				</p>
			);
		}

		const cleaningCount = data.value1 ? pluralizeText(data.value1, 'cleaning') : 'N/A cleanings';
		const cleaningTime = data.value2 ? pluralizeText(data.value2, 'month') : 'N/A months';
		const filteredText = data.value0.replace(/#\[([^#]+)\]/, cleaningCount).replace(/#\[([^#]+)\]/, cleaningTime);

		return reactFromHtml.parseToNodeList(filteredText);
	};


	return (
		<div role="cell" aria-labelledby={ `rowTitle-${rowID}` } className={ `compare-table__tile${className}` }>
			{
				!isEmpty(data) && renderContent()
			}
		</div>
	);
};

ExtendedPerMonthTile.defaultProps = {
	data: {},
	className: '',
};

ExtendedPerMonthTile.propTypes = {
	data: PropTypes.objectOf(PropTypes.string),
	rowID: PropTypes.number.isRequired,
	className: PropTypes.string,
};

export default ExtendedPerMonthTile;

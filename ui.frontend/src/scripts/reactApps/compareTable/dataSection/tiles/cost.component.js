import React from 'react';
import PropTypes from 'prop-types';

import PriceContent from '../../../sharedComponents/priceContent.component';
import PercentageContent from '../../../sharedComponents/percentageContent.component';

const Cost = ({
	disclaimer,
	label,
	percentage,
	range,
	title,
	value,
	value2,
}) => {
	const ValueType = percentage ? PercentageContent : PriceContent;
	let labelMatchesDisclaimer = false;

	if (disclaimer) {
		labelMatchesDisclaimer = label.toLowerCase() === disclaimer.toLowerCase();
	}

	return (
		<>
			{/* Title */}
			{ title && (
				<p className="c-card__table-cost-title u-text-bold">{ title }</p>
			) }

			{/* Value */}
			<div className="unity-type-h2">
				<ValueType value={ value } />

				{/*	Range */}
				{ (range && value2) && (
					<>
						<span> - </span>
						<ValueType value={ value2 } />
					</>
				) }
			</div>

			{/* Label */}
			{ label && (
				<p className="u-color-gray-dark u-m-vert-0">{ label }</p>
			) }

			{/* Small print */}
			{ (disclaimer && !labelMatchesDisclaimer) && (
				<small>{ disclaimer }</small>
			) }
		</>
	);
};

Cost.defaultProps = {
	disclaimer: null,
	label: null,
	percentage: false,
	range: false,
	title: null,
	value2: null,
};

Cost.propTypes = {
	disclaimer: PropTypes.string,
	label: PropTypes.string,
	percentage: PropTypes.bool,
	range: PropTypes.bool,
	title: PropTypes.string,
	value: PropTypes.oneOfType([
		PropTypes.number,
		PropTypes.string,
	]).isRequired,
	value2: PropTypes.string,
};

export default Cost;

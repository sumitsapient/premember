import React from 'react';
import PropTypes from 'prop-types';
import { getFormattedPrice } from '../../utils/plan';

const PriceContent = ({ value, percentageBool }) => {
	let isNumber = true;

	if (value !== undefined && typeof value === 'string') {
		const valueString = value.toString();

		if (/^[a-zA-Z\s]+$/.test(valueString)) {
			isNumber = false;
		}

		if (valueString.includes('$')) {
			isNumber = false;
		}
	}

	if (value !== undefined && isNumber) {
		return (
			<>
				{percentageBool !== 'true' && '$'}
				{ getFormattedPrice(value)}
				{percentageBool === 'true' && '%'}
			</>
		);
	}

	return (
		<>
			<strong
				className="unity-type-h6 u-text-serif"
				style={ { color: '#6F6F81' } }
				aria-hidden="true"
			>
				{value || 'Not Applicable'}
			</strong>
			<span className="u-visually-hidden">{value || 'Not Applicable'}</span>
		</>
	);
};

PriceContent.defaultProps = {
	percentageBool: false,
	value: undefined,
};

PriceContent.propTypes = {
	percentageBool: PropTypes.bool,
	value: PropTypes.oneOfType([
		PropTypes.number,
		PropTypes.string,
	]),
};

export default PriceContent;

import React from 'react';
import PropTypes from 'prop-types';

const PercentageContent = ({ value }) => {
	if (value === undefined) {
		return (
			<>
				<strong style={ { fontSize: '18px', color: '#6F6F81' } } aria-hidden="true">Not Applicable</strong>
				<span className="u-visually-hidden">Not Applicable</span>
			</>
		);
	}

	return (
		<>
			{ value }
			%
		</>
	);
};

PercentageContent.defaultProps = {
	value: undefined,
};

PercentageContent.propTypes = {
	value: PropTypes.oneOfType([
		PropTypes.number,
		PropTypes.string,
	]),
};

export default PercentageContent;

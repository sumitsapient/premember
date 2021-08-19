import React from 'react';
import PropTypes from 'prop-types';

const BenefitsTableRow = ({
	children,
	noBorder,
}) => {
	const classes = [
		'benefits-table__row',
		noBorder ? 'u-b-t-0' : null,
	];

	return (
		<div
			className={ classes.join(' ') }
			role="row"
		>
			{ children }
		</div>
	);
};

BenefitsTableRow.propTypes = {
	children: PropTypes.oneOfType([
		PropTypes.any,
	]),
	noBorder: PropTypes.bool,
};

BenefitsTableRow.defaultProps = {
	children: null,
	noBorder: false,
};

export default BenefitsTableRow;

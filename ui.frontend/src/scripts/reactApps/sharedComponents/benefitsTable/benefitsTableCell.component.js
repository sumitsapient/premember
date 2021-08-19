import React from 'react';
import PropTypes from 'prop-types';

const BenefitsTableCell = ({
	bold,
	children,
	columnHeader,
	visuallyHidden,
	noPaddingTop,
}) => {
	const classes = [
		'benefits-table__cell',
		bold ? 'benefits-table__cell--bold' : null,
		visuallyHidden ? 'benefits-table__cell--visually-hidden' : null,
		noPaddingTop ? 'u-p-t-0' : null,
	];

	/**
	 * Renders body content
	 */
	const renderBodyContent = () => {
		if (visuallyHidden) {
			return (<span>{ children }</span>);
		}

		return children;
	};


	return (
		<div
			className={ classes.join(' ') }
			role={ columnHeader ? 'columnheader' : 'cell' }
		>
			{ renderBodyContent() }
		</div>
	);
};

BenefitsTableCell.propTypes = {
	bold: PropTypes.bool,
	children: PropTypes.oneOfType([
		PropTypes.any,
	]),
	columnHeader: PropTypes.bool,
	noPaddingTop: PropTypes.bool,
	visuallyHidden: PropTypes.bool,
};

BenefitsTableCell.defaultProps = {
	bold: false,
	children: null,
	columnHeader: false,
	noPaddingTop: false,
	visuallyHidden: false,
};

export default BenefitsTableCell;

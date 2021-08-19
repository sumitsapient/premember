import React from 'react';
import PropTypes from 'prop-types';

const BenefitsTableNotes = ({
	children,
}) => (
	<div className="benefits-table__notes">
		<p>
			{ children }
		</p>
	</div>
);

BenefitsTableNotes.propTypes = {
	children: PropTypes.oneOfType([
		PropTypes.any,
	]).isRequired,
};

export default BenefitsTableNotes;

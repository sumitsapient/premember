import React from 'react';
import PropTypes from 'prop-types';
import { isEmpty } from '../../../../utils/helpers';

import Cost from './cost.component';

const PriceTile = ({
	title,
	data,
}) => (
	!isEmpty(data) && (
		<Cost
			label={ title }
			value={ data.value0 }
		/>
	)
);

PriceTile.defaultProps = {
	data: {},
	title: null,
};

PriceTile.propTypes = {
	title: PropTypes.string,
	data: PropTypes.objectOf(PropTypes.oneOfType([
		PropTypes.string,
		PropTypes.number,
	])),
};

export default PriceTile;

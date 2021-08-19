import React from 'react';
import PropTypes from 'prop-types';
import { isEmpty } from '../../../../utils/helpers';

import Cost from '../../../compareTable/dataSection/tiles/cost.component';

const PriceTileSimple = ({
	title,
	data,
}) => (
	!isEmpty(data) && (
		<Cost
			title={ title || null }
			value={ data.value0 }
		/>
	)
);

PriceTileSimple.defaultProps = {
	data: {},
	title: undefined,
};

PriceTileSimple.propTypes = {
	title: PropTypes.string,
	data: PropTypes.objectOf(PropTypes.oneOfType([
		PropTypes.string,
		PropTypes.number,
	])),
};

export default PriceTileSimple;

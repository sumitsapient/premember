import React from 'react';
import PropTypes from 'prop-types';
import { isEmpty } from '../../../../utils/helpers';

import Cost from '../../../compareTable/dataSection/tiles/cost.component';

const PriceTileSuffix = ({
	title,
	data,
}) => (
	!isEmpty(data) && (
		<Cost
			label={ data.value1 }
			percentage={ data.valuePercentage0 === 'true' }
			title={ title || null }
			value={ data.value0 }
		/>
	)
);

PriceTileSuffix.defaultProps = {
	data: {},
	title: undefined,
};

PriceTileSuffix.propTypes = {
	title: PropTypes.string,
	data: PropTypes.objectOf(PropTypes.oneOfType([
		PropTypes.string,
		PropTypes.number,
	])),
};

export default PriceTileSuffix;

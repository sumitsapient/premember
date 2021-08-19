import React from 'react';
import PropTypes from 'prop-types';
import { isEmpty } from '../../../../utils/helpers';

import Cost from '../../../compareTable/dataSection/tiles/cost.component';

const PriceTileVision = ({
	title,
	data,
}) => (
	!isEmpty(data) && (
		<>
			<Cost
				label="Biweekly"
				title={ title || null }
				value={ data.value0 }
			/>
			<Cost
				disclaimer={ typeof data.value2 !== 'undefined' ? data.value2 : null }
				label="Monthly"
				value={ data.value1 }
			/>
		</>
	)
);

PriceTileVision.defaultProps = {
	data: {},
	title: undefined,
};

PriceTileVision.propTypes = {
	title: PropTypes.string,
	data: PropTypes.objectOf(PropTypes.oneOfType([
		PropTypes.string,
		PropTypes.number,
	])),
};

export default PriceTileVision;

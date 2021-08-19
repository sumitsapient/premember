import React from 'react';
import PropTypes from 'prop-types';

import { isEmpty } from '../../../../utils/helpers';
import Cost from '../../../compareTable/dataSection/tiles/cost.component';

const BooleanTile = ({
	title,
	data,
}) => {
	let inNetworkVal = null;
	let outOfNetworkVal = null;

	if (typeof data.value0 !== 'undefined') {
		inNetworkVal = data.value0 === 'true' ? 'Yes' : 'No';
	}

	if (typeof data.value1 !== 'undefined') {
		outOfNetworkVal = data.value1 === 'true' ? 'Yes' : 'No';
	}

	return (
		!isEmpty(data) && (
			<>
				<Cost
					label="In Network"
					title={ title || null }
					value={ inNetworkVal }
				/>
				<Cost
					label="Out-of-Network"
					value={ outOfNetworkVal }
				/>
			</>
		)
	);
};

BooleanTile.defaultProps = {
	data: {},
	title: undefined,
};

BooleanTile.propTypes = {
	title: PropTypes.string,
	data: PropTypes.objectOf(PropTypes.oneOfType([
		PropTypes.string,
		PropTypes.number,
	])),
};

export default BooleanTile;

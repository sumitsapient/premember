import React from 'react';
import PropTypes from 'prop-types';
import { isEmpty } from '../../../../utils/helpers';

import Cost from './cost.component';

const PercentageTile = ({
	title,
	data,
}) => (
	!isEmpty(data) && (
		isEmpty(data.value1) ? (
			<Cost
				label={ title }
				percentage
				value={ data.value0 }
			/>
		) : (
			<Cost
				label={ title }
				percentage
				range
				value={ data.value1 }
				value2={ data.value2 }
			/>
		)
	)
);

PercentageTile.defaultProps = {
	title: null,
	data: {},
};

PercentageTile.propTypes = {
	title: PropTypes.string,
	data: PropTypes.objectOf(PropTypes.oneOfType([
		PropTypes.string,
		PropTypes.number,
	])),
};

export default PercentageTile;

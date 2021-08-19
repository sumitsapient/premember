import React from 'react';
import PropTypes from 'prop-types';
import { isEmpty } from '../../../../utils/helpers';

import PercentageContent from '../../../sharedComponents/percentageContent.component';
import PriceContent from '../../../sharedComponents/priceContent.component';

const ExtendedCostsTile = ({ data }) => (
	<>
		<strong>I am extended costs!</strong>
		{
			!isEmpty(data) && (
				<>
					<div className="compare-table__tile-copay">
						<p className="compare-table__copay-title">Copay per visit</p>
						<p className="compare-table__copay-value">
							<PriceContent value={ data.value0 } />
						</p>
					</div>
					<div className="compare-table__tile-coinsurance">
						<p className="compare-table__coinsurance-value">
							<PercentageContent value={ data.value1 } />
						</p>
						<p className="compare-table__coinsurance-title">Coinsurance for services</p>
					</div>
				</>
			)
		}
	</>
);

ExtendedCostsTile.defaultProps = {
	data: {},
};

ExtendedCostsTile.propTypes = {
	data: PropTypes.objectOf(PropTypes.oneOfType([
		PropTypes.number,
		PropTypes.string,
	])),
};

export default ExtendedCostsTile;

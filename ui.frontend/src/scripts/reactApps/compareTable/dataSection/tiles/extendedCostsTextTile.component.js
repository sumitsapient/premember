import React from 'react';
import PropTypes from 'prop-types';
import { ReactFromHtml } from '@bitjourney/react-from-html';
import { isEmpty } from '../../../../utils/helpers';

import Cost from './cost.component';

const reactFromHtml = new ReactFromHtml();

const ExtendedCostsTextTile = ({
	data,
	costType,
}) => {
	const renderPercentageOutOfNetworkContent = (dataObject) => {
		if (dataObject.value5) {
			return (
				<strong>
					{ reactFromHtml.parseToNodeList(dataObject.value5) }
				</strong>
			);
		}

		return (
			<Cost
				disclaimer={ dataObject.value3 ? 'paid by member' : null }
				label="Out-Of-Network"
				percentage={ !!dataObject.value2 }
				value={ dataObject.value2 || dataObject.value3 }
			/>
		);
	};

	const renderTileContent = (dataObject) => {
		if (dataObject.value4) {
			return reactFromHtml.parseToNodeList(dataObject.value4);
		}

		return (
			<>
				{/* In Network */}
				{
					costType === 'price' ? (
						<Cost
							label="Network"
							value={ dataObject.value1 }
							disclaimer={ dataObject.value1_disclaimer || null }
						/>
					) : (
						<Cost
							label="Network"
							percentage
							value={ dataObject.value0 }
							disclaimer={ dataObject.value0_disclaimer || null }
						/>
					)
				}
				{/* Out of Network */}
				{
					costType === 'price' ? (
						<Cost
							label="Out-Of-Network"
							value={ dataObject.value3 }
							disclaimer={ dataObject.value3_disclaimer || null }
						/>
					) : renderPercentageOutOfNetworkContent(dataObject)
				}
			</>
		);
	};

	return (
		!isEmpty(data) && renderTileContent(data)
	);
};

ExtendedCostsTextTile.defaultProps = {
	data: {},
	costType: '',
};

ExtendedCostsTextTile.propTypes = {
	data: PropTypes.objectOf(PropTypes.oneOfType([
		PropTypes.number,
		PropTypes.string,
		PropTypes.bool,
	])),
	costType: PropTypes.string,
};

export default ExtendedCostsTextTile;

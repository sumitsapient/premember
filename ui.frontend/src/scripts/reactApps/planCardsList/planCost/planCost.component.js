import React from 'react';
import PropTypes from 'prop-types';
import { getFormattedPrice } from '../../../utils/plan';

const PlanCosts = ({ costs }) => (
	<div className="c-card__plan-costs">
		<h4 className="unity-type-h6">{ costs.title }</h4>
		<div className="c-card__plan-cost-items o-grid">
			<div className="o-grid__item-auto o-grid__item-12@sm">
				<p className="u-color-gray-dark">{ costs.costText2 }</p>
				<p className="unity-type-h2">
					{ costs.costValue1 ? `$${getFormattedPrice(costs.costValue1)}` : 'N/A' }
				</p>
				<p className="u-color-gray-dark">{ costs.costName1 }</p>
			</div>
			<div className="o-grid__item-auto o-grid__item-12@sm">
				<p className="u-color-gray-dark">{ costs.costText2 }</p>
				<p className="unity-type-h2">
					{ costs.costValue2 ? `$${getFormattedPrice(costs.costValue2)}` : 'N/A' }
				</p>
				<p className="u-color-gray-dark">{ costs.costName2 }</p>
			</div>
		</div>
	</div>
);

PlanCosts.propTypes = {
	costs: PropTypes.objectOf(PropTypes.string).isRequired,
};

export default PlanCosts;

import React from 'react';
import PropTypes from 'prop-types';

import ZipSelector from './zipSelector.component';
import AdjustBySelectors from './adjustBySelectors.component';

const PlanFilters = ({
	checkConditionalStatement,
	filterFields,
	hasPlans,
	selected,
	updatePlans,
	updateSelected,
}) => (
	<>
		<ZipSelector
			hasPlans={ hasPlans }
			planType={ selected.planType }
			showZipOnLoad={ !selected.zipCode }
			updatePlans={ updatePlans }
			updateSelected={ updateSelected }
			zipCode={ selected.zipCode }
		/>

		{
			hasPlans && (
				<>
					<p>Make adjustments in the dropdowns to calculate each plan’s premium rate.</p>

					<AdjustBySelectors
						checkConditionalStatement={ checkConditionalStatement }
						filterFields={ filterFields }
						selected={ selected }
						updateSelected={ updateSelected }
					/>

					<hr className="u-m-vert" />
				</>
			)
		}
	</>
);

PlanFilters.protoTypes = {
	checkConditionalStatement: PropTypes.func.isRequired,
	filterFields: PropTypes.arrayOf(PropTypes.object).isRequired,
	hasPlans: PropTypes.bool.isRequired,
	selected: PropTypes.objectOf(PropTypes.string).isRequired,
	updatePlans: PropTypes.func.isRequired,
	updateSelected: PropTypes.func.isRequired,
};

export default React.memo(PlanFilters);

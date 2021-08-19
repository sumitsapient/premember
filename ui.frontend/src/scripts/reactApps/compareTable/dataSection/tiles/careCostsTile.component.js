import React from 'react';
import PropTypes from 'prop-types';
import { isEmpty } from '../../../../utils/helpers';

import Cost from './cost.component';

const CareCostsTile = ({
	data,
	rowType,
	plansData,
}) => {
	let tierOneTitle = null;
	let tierTwoTitle = null;

	// Set tier one and two titles
	if (!isEmpty(data) && plansData[`${rowType}CopayType`] === 'tiered') {
		if (plansData[`${rowType}CopayUseAlternateTierTitles`] === 'true') {
			switch (rowType) {
				case 'comparisonSpecialistVisits':
					tierOneTitle = 'With referral';
					tierTwoTitle = 'Without referral';
					break;
				case 'comparisonLabTesting':
					tierOneTitle = 'Freestanding/Office';
					tierTwoTitle = 'Hospital';
					break;
				case 'comparisonXRayTesting':
					tierOneTitle = 'Freestanding/Office';
					tierTwoTitle = 'Hospital';
					break;
				default:
					tierOneTitle = 'Tier 1';
					tierTwoTitle = 'Tier 2';
			}
		} else {
			tierOneTitle = rowType === 'comparisonEmergencyRoom' ? 'First and Second Visits'
				: 'Tier 1';
			tierTwoTitle = rowType === 'comparisonEmergencyRoom' ? 'Third/Additional Visits'
				: 'Tier 2';
		}
	}

	const renderSingle = () => (
		!isEmpty(data) && (
			plansData[`${rowType}CopayType`] === 'single' || (
				plansData[`${rowType}CopayType`] !== 'tiered'
				&& plansData[`${rowType}CopayType`] !== 'range'
			)
		) && (
			<>
				{/* Copay */}
				<Cost
					disclaimer={ plansData[`${rowType}CopayCopy`] !== undefined ? plansData[`${rowType}CopayCopy.copy`] : null }
					label="Copay"
					value={ data.value0 }
				/>
				{/* Coinsurance */}
				<Cost
					disclaimer={ plansData[`${rowType}CoinsuranceCopy`] !== undefined ? plansData[`${rowType}CoinsuranceCopy.copy`] : null }
					label="Coinsurance"
					percentage
					value={ data.value1 }
				/>
			</>
		)
	);

	const renderTiered = () => (
		!isEmpty(data) && plansData[`${rowType}CopayType`] === 'tiered' && (
			<>
				{/* Tier 1 - Copay */}
				<Cost
					disclaimer={ plansData[`${rowType}CopayTier1Copy.copy`] }
					label="Copay"
					title={ tierOneTitle }
					value={ plansData[`${rowType}CopayTier1`] }
				/>
				{/* Tier 1 - Coinsurance */}
				<Cost
					disclaimer={ plansData[`${rowType}CoinsuranceTier1Copy.copy`] }
					label="Coinsurance"
					percentage
					value={ plansData[`${rowType}CoinsuranceTier1`] }
				/>
				{/* Specialist visits w/ or w/out referral */}
				{
					rowType === 'comparisonSpecialistVisits' && (
						<>
							{ plansData[`${rowType}WithReferralTier1`] && (
								<Cost
									disclaimer={ plansData[`${rowType}WithReferralTier1Copy.copy`] }
									percentage
									title="With Referral"
									value={ plansData[`${rowType}WithReferralTier1`] }
								/>
							) }
							{ plansData[`${rowType}WithoutReferralTier1`] && (
								<Cost
									disclaimer={ plansData[`${rowType}WithoutReferralTier1Copy.copy`] }
									percentage
									title="Without Referral"
									value={ plansData[`${rowType}WithoutReferralTier1`] }
								/>
							) }
						</>
					)
				}
				{/* Lab testing & X ray testing */}
				{
					(rowType === 'comparisonLabTesting' || rowType === 'comparisonXRayTesting') && (
						<>
							{ plansData[`${rowType}FreestandingOfficeTier1`] && (
								<Cost
									disclaimer={ plansData[`${rowType}FreestandingOfficeTier1Copy.copy`] }
									title="Freestanding/Office"
									value={ plansData[`${rowType}FreestandingOfficeTier1`] }
								/>
							) }
							{ plansData[`${rowType}HospitalTier1`] && (
								<Cost
									disclaimer={ plansData[`${rowType}HospitalTier1Copy.copy`] }
									title="Hospital"
									value={ plansData[`${rowType}HospitalTier1`] }
								/>
							) }
						</>
					)
				}
				{/* Tier 2 - Copay */}
				<Cost
					disclaimer={ plansData[`${rowType}CopayTier2Copy.copy`] }
					title={ tierTwoTitle }
					label="Copay"
					value={ plansData[`${rowType}CopayTier2`] }
				/>
				{/* Tier 2 - Coinsurance */}
				<Cost
					disclaimer={ plansData[`${rowType}CoinsuranceTier2Copy.copy`] }
					label="Coinsurance"
					percentage
					value={ plansData[`${rowType}CoinsuranceTier2`] }
				/>
				{/* Specialist visits w/ or w/out referral */}
				{
					rowType === 'comparisonSpecialistVisits' && (
						<>
							{ plansData[`${rowType}WithReferralTier2`] && (
								<Cost
									disclaimer={ plansData[`${rowType}WithReferralTier2Copy.copy`] }
									percentage
									title="With Referral"
									value={ plansData[`${rowType}WithReferralTier2`] }
								/>
							) }
							{ plansData[`${rowType}WithoutReferralTier2`] && (
								<Cost
									disclaimer={ plansData[`${rowType}WithoutReferralTier2Copy.copy`] }
									percentage
									title="Without Referral"
									value={ plansData[`${rowType}WithoutReferralTier2`] }
								/>
							) }
						</>
					)
				}
				{/* Comparison lab testing & X ray testing */}
				{
					(rowType === 'comparisonLabTesting' || rowType === 'comparisonXRayTesting') && (
						<>
							{ plansData[`${rowType}FreestandingOfficeTier2`] && (
								<Cost
									disclaimer={ plansData[`${rowType}FreestandingOfficeTier2Copy.copy`] }
									title="Freestanding/Office"
									value={ plansData[`${rowType}FreestandingOfficeTier2`] }
								/>
							) }
							{ plansData[`${rowType}HospitalTier2`] && (
								<Cost
									disclaimer={ plansData[`${rowType}HospitalTier2Copy.copy`] }
									title="Hospital"
									value={ plansData[`${rowType}HospitalTier2`] }
								/>
							) }
						</>
					)
				}
			</>
		)
	);

	const renderRange = () => (
		!isEmpty(data) && plansData[`${rowType}CopayType`] === 'range' && (
			<>
				{/* Copay */}
				<Cost
					disclaimer={ plansData[`${rowType}CopayCopy`] !== undefined ? plansData[`${rowType}CopayCopy.copy`] : null }
					label="Copay"
					title={ plansData[`${rowType}CopayRangeValuesTitle`] }
					range
					value={ plansData[`${rowType}CopayRangeStart`] }
					value2={ plansData[`${rowType}CopayRangeEnd`] }
				/>
				{/* Coinsurance */}
				<Cost
					disclaimer={ plansData[`${rowType}CoinsuranceCopy`] !== undefined ? plansData[`${rowType}CoinsuranceCopy.copy`] : null }
					label="Coinsurance"
					percentage
					value={ plansData[`${rowType}Coinsurance`] }
				/>
			</>
		)
	);

	return (
		<>
			{ renderSingle() }
			{ renderTiered() }
			{ renderRange() }
		</>
	);
};

CareCostsTile.defaultProps = {
	data: {},
	plansData: {},
};

CareCostsTile.propTypes = {
	data: PropTypes.objectOf(PropTypes.oneOfType([
		PropTypes.number,
		PropTypes.string,
	])),
	rowType: PropTypes.string.isRequired,
	plansData: PropTypes.objectOf(PropTypes.oneOfType([
		PropTypes.number,
		PropTypes.string,
	])),
};

export default CareCostsTile;

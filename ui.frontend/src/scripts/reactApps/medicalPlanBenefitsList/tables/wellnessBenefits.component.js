import React from 'react';
import PropTypes from 'prop-types';

import BenefitsTable from '../../sharedComponents/benefitsTable/benefitsTable.component';

const WellnessBenefits = ({ data }) => {
	const { bwh } = data.benefits;
	const tabLabel = data.planName;

	const renderBenefit = (benefitType) => {
		const type = bwh[benefitType];

		return (
			`${type ?? 'Not Applicable'}${type ? '%' : ''}`
		);
	};

	return (
		<div className="wellnessBenefits u-color-blue">
			<BenefitsTable ariaLabel={ `${tabLabel} benefits table` }>
				{/* Table Headings */}
				<BenefitsTable.RowGroup>
					<BenefitsTable.Row>
						<BenefitsTable.Cell columnHeader visuallyHidden>Wellness Benefit</BenefitsTable.Cell>
					</BenefitsTable.Row>
				</BenefitsTable.RowGroup>
				{/* Table Rows */}
				<BenefitsTable.RowGroup>
					<BenefitsTable.Row>
						<BenefitsTable.Cell bold>Outpatient Services Office Visits </BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('OutpatientServicesOfficeVisits')}
						</BenefitsTable.Cell>

					</BenefitsTable.Row>
				</BenefitsTable.RowGroup>
				<BenefitsTable.RowGroup>
					<BenefitsTable.Row>
						<BenefitsTable.Cell bold>Inpatient Services</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('InpatientServices')}
						</BenefitsTable.Cell>
					</BenefitsTable.Row>
				</BenefitsTable.RowGroup>
			</BenefitsTable>
		</div>
	);
};

WellnessBenefits.propTypes = {
	data: PropTypes.shape().isRequired,
};

export default WellnessBenefits;

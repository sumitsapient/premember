import React from 'react';
import PropTypes from 'prop-types';

import BenefitsTable from '../../sharedComponents/benefitsTable/benefitsTable.component';

const PrescriptionBenefits = ({ data }) => {
	const { benefits } = data;
	const tabLabel = data.planName;

	const renderBenefit = (benefitType, benefitKey) => {
		const type = benefits[benefitType];
		const key = benefitKey;

		return (
			`${type[key] ? '$' : ''}${type[key] ? type[key] : 'Not Applicable'}`
		);
	};

	return (
		<div className="prescriptionBenefits">
			<BenefitsTable ariaLabel={ `${tabLabel} benefits table` }>
				{/* Table Headings */}
				<BenefitsTable.RowGroup>
					<BenefitsTable.Row>
						<BenefitsTable.Cell columnHeader visuallyHidden>Type of Care</BenefitsTable.Cell>
						<BenefitsTable.Cell columnHeader>Retail Pharmacy (30-day supply)</BenefitsTable.Cell>
						<BenefitsTable.Cell columnHeader>Mail Order (90-day supply)</BenefitsTable.Cell>
						<BenefitsTable.Cell columnHeader>Out of Network Order</BenefitsTable.Cell>
					</BenefitsTable.Row>
				</BenefitsTable.RowGroup>
				{/* Table Rows */}
				<BenefitsTable.RowGroup collapse heading="Prescription Benefits">
					<BenefitsTable.Row>
						<BenefitsTable.Cell>Tier 1</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('prescriptionDrugs', 'retailPharmacyTier1')}
						</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('prescriptionDrugs', 'mailOrderTier1')}
						</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('prescriptionDrugs', 'outOfNetworkTier1')}
						</BenefitsTable.Cell>
					</BenefitsTable.Row>
					<BenefitsTable.Row>
						<BenefitsTable.Cell>Tier 2</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('prescriptionDrugs', 'retailPharmacyTier2')}
						</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('prescriptionDrugs', 'mailOrderTier2')}
						</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('prescriptionDrugs', 'outOfNetworkTier2')}
						</BenefitsTable.Cell>
					</BenefitsTable.Row>
					<BenefitsTable.Row>
						<BenefitsTable.Cell>Tier 3</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('prescriptionDrugs', 'retailPharmacyTier3')}
						</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('prescriptionDrugs', 'mailOrderTier3')}
						</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('prescriptionDrugs', 'outOfNetworkTier3')}
						</BenefitsTable.Cell>
					</BenefitsTable.Row>
					<BenefitsTable.Row>
						<BenefitsTable.Cell>Tier 4</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('prescriptionDrugs', 'retailPharmacyTier4')}
						</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('prescriptionDrugs', 'mailOrderTier4')}
						</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('prescriptionDrugs', 'outOfNetworkTier4')}
						</BenefitsTable.Cell>
					</BenefitsTable.Row>
				</BenefitsTable.RowGroup>
				<BenefitsTable.RowGroup>
					<BenefitsTable.Row>
						<BenefitsTable.Cell>
							{(benefits.prescriptionDrugList !== undefined && benefits.prescriptionDrugList.items?.length > 0)
								&& (
									<div className="c-benefits-list__files">
										<h3 className="u-color-blue u-m-t-0">{benefits.prescriptionDrugList.sectionTitle}</h3>
										<ul className="o-list">
											{benefits.prescriptionDrugList.items.map((item, index) => (
												<li className="o-list__item" key={ index }>
													<a href={ item.path }>{item.title}</a>
												</li>
											))}
										</ul>
									</div>
								)}
						</BenefitsTable.Cell>
					</BenefitsTable.Row>
				</BenefitsTable.RowGroup>
			</BenefitsTable>

		</div>
	);
};

PrescriptionBenefits.propTypes = {
	data: PropTypes.shape().isRequired,
};

export default PrescriptionBenefits;

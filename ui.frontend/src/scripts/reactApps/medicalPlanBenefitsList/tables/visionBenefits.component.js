/* eslint-disable react/prefer-stateless-function */
import React, { Component } from 'react';
import PropTypes from 'prop-types';

import BenefitsTable from '../../sharedComponents/benefitsTable/benefitsTable.component';


class VisionBenefits extends Component {
	render() {
		// eslint-disable-next-line
		const { benefits } = this.props.data;
		// eslint-disable-next-line
		const tabLabel = this.props.data.planName;

		const renderBenefit = (benefitType, suffix) => {
			const benefit = benefits[benefitType];
			let suffixStr = '';
			if (typeof benefits[suffix] !== 'undefined') {
				suffixStr = benefits[suffix];
			}

			const percentageString = `${benefitType}IsPercentage`;
			let hasPercentage = true;
			if (benefits[percentageString] === 'true') {
				hasPercentage = true;
			} else if (benefits[percentageString] === 'false') {
				hasPercentage = false;
			}
			if (typeof benefit !== 'undefined') {
				return (
					`${benefit && !hasPercentage ? '$' : ''}${benefit}${benefit && hasPercentage ? '%' : ''} ${suffixStr}`
				);
			}
			return (
				'Not Applicable'
			);
		};

		return (
			<div className="wellnessBenefits">
				<BenefitsTable ariaLabel={ `${tabLabel} benefits table` }>
					{/* Table Rows */}
					<BenefitsTable.RowGroup collapse heading="Benefits">
						<BenefitsTable.Row>
							<BenefitsTable.Cell>Routine Refraction Exam</BenefitsTable.Cell>
							<BenefitsTable.Cell>
								{renderBenefit('visionRoutineRefractionExam', 'visionRoutineRefractionExamSuffix')}
							</BenefitsTable.Cell>
							<BenefitsTable.Cell>
							</BenefitsTable.Cell>

						</BenefitsTable.Row>
						<BenefitsTable.Row>
							<BenefitsTable.Cell>Routine Refraction Exam Plan Allowance</BenefitsTable.Cell>
							<BenefitsTable.Cell>
								{renderBenefit('visionRoutineRefractionExamPlanAllowance', 'visionRoutineRefractionExamPlanAllowanceSuffix')}
							</BenefitsTable.Cell>
							<BenefitsTable.Cell>
							</BenefitsTable.Cell>
						</BenefitsTable.Row>

					</BenefitsTable.RowGroup>
					<BenefitsTable.RowGroup collapse heading="Allowances">
						<BenefitsTable.Row>
							<BenefitsTable.Cell>Frames Allowance</BenefitsTable.Cell>
							<BenefitsTable.Cell>
								{renderBenefit('visionFramesAllowance', 'visionFramesAllowanceSuffix')}
							</BenefitsTable.Cell>
							<BenefitsTable.Cell>
							</BenefitsTable.Cell>
						</BenefitsTable.Row>
						<BenefitsTable.Row>
							<BenefitsTable.Cell>Frames Plan Allowance</BenefitsTable.Cell>
							<BenefitsTable.Cell>
								{renderBenefit('visionFramesAllowancePlanAllowance', 'visionFramesAllowancePlanAllowanceSuffix')}
							</BenefitsTable.Cell>
							<BenefitsTable.Cell>
							</BenefitsTable.Cell>
						</BenefitsTable.Row>
						<BenefitsTable.Row>
							<BenefitsTable.Cell>Eyeglass Lenses Allowance</BenefitsTable.Cell>
							<BenefitsTable.Cell>
								{renderBenefit('visionEyeglassLensAllowance', 'visionEyeglassLensAllowanceSuffix')}
							</BenefitsTable.Cell>
							<BenefitsTable.Cell>
							</BenefitsTable.Cell>
						</BenefitsTable.Row>
						<BenefitsTable.Row>
							<BenefitsTable.Cell>Eyeglass Lenses Plan Allowance</BenefitsTable.Cell>
							<BenefitsTable.Cell>
								{renderBenefit('visionEyeglassLensAllowancePlanAllowance', 'visionEyeglassLensAllowancePlanAllowanceSuffix')}
							</BenefitsTable.Cell>
							<BenefitsTable.Cell>
							</BenefitsTable.Cell>
						</BenefitsTable.Row>
						<BenefitsTable.Row>
							<BenefitsTable.Cell>Contact Lenses Allowance</BenefitsTable.Cell>
							<BenefitsTable.Cell>
								{renderBenefit('visionContactLensesAllowance', 'visionContactLensesAllowanceSuffix')}
							</BenefitsTable.Cell>
							<BenefitsTable.Cell>
							</BenefitsTable.Cell>
						</BenefitsTable.Row>
						<BenefitsTable.Row>
							<BenefitsTable.Cell>Contact Lenses Plan Allowance</BenefitsTable.Cell>
							<BenefitsTable.Cell>
								{renderBenefit('visionContactLensesAllowancePlanAllowance', 'visionContactLensesAllowancePlanAllowanceSuffix')}
							</BenefitsTable.Cell>
							<BenefitsTable.Cell>
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
	}
}

VisionBenefits.propTypes = {
	data: PropTypes.shape().isRequired,
};

export default VisionBenefits;

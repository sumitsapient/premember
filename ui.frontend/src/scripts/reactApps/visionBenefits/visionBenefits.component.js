import React from 'react';
import PropTypes from 'prop-types';

import BenefitsTable from '../sharedComponents/benefitsTable/benefitsTable.component';

const VisionBenefits = ({ data }) => {
	const benefits = data.data;
	const prefixes = [
		'up to',
	];

	const renderBenefit = (benefitType, benefitKey, benefitPrefix) => {
		const key = benefits[benefitType];
		const value = key[benefitKey];
		const prefix = key[benefitPrefix];
		let isPrefix;
		if (prefixes.includes(prefix)) {
			isPrefix = true;
		} else {
			isPrefix = false;
		}
		let fullCoverage = false;
		if (benefitType === 'allowances') {
			const coveredInFull = `${benefitKey}CoveredInFull`;
			const coveredVal = key[coveredInFull];
			if (coveredVal === 'true') {
				fullCoverage = true;
			}
		}
		if (fullCoverage) {
			return (
				'Covered in full'
			);
		}
		return (
			`${prefix && isPrefix ? prefix : ''} ${value ? `$${value}` : 'Not Applicable'} ${prefix && !isPrefix ? prefix : ''} `
		);
	};
	return (
		<div className="c-benefits-list u-bg-white">
			<BenefitsTable hasSibling>
				{/* Table Headings */}
				<BenefitsTable.RowGroup>
					<BenefitsTable.Row>
						<BenefitsTable.Cell columnHeader visuallyHidden>Type of Care</BenefitsTable.Cell>
						<BenefitsTable.Cell columnHeader visuallyHidden>Copay Amount</BenefitsTable.Cell>
					</BenefitsTable.Row>
				</BenefitsTable.RowGroup>
				{/* Table Body */}
				{/* Collapsing Body */}
				<BenefitsTable.RowGroup collapse heading="Copays">
					<BenefitsTable.Row>
						<BenefitsTable.Cell>Annual Eye Exam</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('copays', 'annualEyeExam', 'annualEyeExamPrefix')}
						</BenefitsTable.Cell>
					</BenefitsTable.Row>
					<BenefitsTable.Row>
						<BenefitsTable.Cell>Eye Glasses</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('copays', 'eyeGlasses', 'eyeGlassesPrefix')}
						</BenefitsTable.Cell>
					</BenefitsTable.Row>
					<BenefitsTable.Row>
						<BenefitsTable.Cell>Standard scartch-resistant coating</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('copays', 'standardScratchResistantCoating', 'standardScratchResistantCoatingPrefix')}
						</BenefitsTable.Cell>
					</BenefitsTable.Row>
					<BenefitsTable.Row>
						<BenefitsTable.Cell>Polycarbonate lenses</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('copays', 'polycarbonateLenses', 'polycarbonateLensesPrefix')}
						</BenefitsTable.Cell>
					</BenefitsTable.Row>
					<BenefitsTable.Row>
						<BenefitsTable.Cell>Transition lenses</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('copays', 'transitionLenses', 'transitionLensesPrefix')}
						</BenefitsTable.Cell>
					</BenefitsTable.Row>
					<BenefitsTable.Row>
						<BenefitsTable.Cell>Tinted Lenses</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('copays', 'tintedLenses', 'tintedLensesPrefix')}
						</BenefitsTable.Cell>
					</BenefitsTable.Row>
					<BenefitsTable.Row>
						<BenefitsTable.Cell>Tier 1 progressive</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('copays', 'tierOneProgressive', 'tierOneProgressivePrefix')}
						</BenefitsTable.Cell>
					</BenefitsTable.Row>
					<BenefitsTable.Row>
						<BenefitsTable.Cell>UV Coating</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('copays', 'uvCoating', 'uvCoatingPrefix')}
						</BenefitsTable.Cell>
					</BenefitsTable.Row>
					<BenefitsTable.Row>
						<BenefitsTable.Cell>High-end (Tier 2-4) progressive</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('copays', 'highEndTierTwoFourProgressive', 'highEndTierTwoFourProgressivePrefix')}
						</BenefitsTable.Cell>
					</BenefitsTable.Row>
					<BenefitsTable.Row>
						<BenefitsTable.Cell>High-end plastic up to 1.73</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('copays', 'highEndPlasticUpToOnePointSeventyThree', 'highEndPlasticUpToOnePointSeventyThreePrefix')}
						</BenefitsTable.Cell>
					</BenefitsTable.Row>
				</BenefitsTable.RowGroup>
			</BenefitsTable>
			<BenefitsTable>
				{/* Table Headings */}
				<BenefitsTable.RowGroup>
					<BenefitsTable.Row>
						<BenefitsTable.Cell columnHeader visuallyHidden>Type of Care</BenefitsTable.Cell>
						<BenefitsTable.Cell columnHeader visuallyHidden>Allowance Amount</BenefitsTable.Cell>
					</BenefitsTable.Row>
				</BenefitsTable.RowGroup>
				<BenefitsTable.RowGroup collapse heading="Allowances">
					<BenefitsTable.Row>
						<BenefitsTable.Cell>Frames</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('allowances', 'frames', 'framesPrefix')}
						</BenefitsTable.Cell>
					</BenefitsTable.Row>
					<BenefitsTable.Row>
						<BenefitsTable.Cell>Contact lenses</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('allowances', 'contactLenses', 'contactLensesPrefix')}
						</BenefitsTable.Cell>
					</BenefitsTable.Row>
					<BenefitsTable.Row>
						<BenefitsTable.Cell>Contact lens fitting/evaultion</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('allowances', 'contactLensFittingEvaluation', 'contactLensFittingEvaluationPrefix')}
						</BenefitsTable.Cell>
					</BenefitsTable.Row>
				</BenefitsTable.RowGroup>
				<BenefitsTable.RowGroup>
					<BenefitsTable.Row>
						<BenefitsTable.Cell>
							{data.data.brochures.items?.length > 0
								&& (
									<div className="c-benefits-list__files">
										<h3 className="u-color-blue u-m-t-0">{data.data.brochures.sectionTitle}</h3>
										<ul className="o-list-inline">
											{data.data.brochures.items.map((item, index) => (
												<li className="o-list-inline__item" key={ index }>
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

VisionBenefits.propTypes = {
	data: PropTypes.shape().isRequired,
};

export default VisionBenefits;

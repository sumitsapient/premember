import React from 'react';
import PropTypes from 'prop-types';

import BenefitsTable from '../sharedComponents/benefitsTable/benefitsTable.component';

const TABLE_HEADINGS = {
	medical: [
		'Diagnostic Services (e.g., checkups, X-rays, lab tests) Class A',
		'Preventive Services (e.g., cleanings, fluoride, sealants) Class A',
		'Intermediate Services (e.g., extractions, oral surgery) Class B',
		'Major Services (e.g., crowns, implants, dentures) Class C',
		'Annual Maximums for A, B, & C',
		'Limetime Maximum for D *',
		'Orthodontic Services Class D',
		'Waiting Period for Services Class A',
		'Waiting Period for Services Class B',
		'Waiting Period for Services Class C',
		'Waiting Period for Services Class D',


	],
};


const DentalBenefits = ({ data }) => {
	const benefits = {};
	benefits.medicalCare = data.data.medicalCare;
	benefits.preventiveCare = data.data.preventiveCare;

	const renderBenefit = (benefitKey, suppressPercentage = false, benefitType = 'medicalCare') => {
		const key = benefits[benefitType];
		const value = key[benefitKey];
		const isPercentageStr = `${benefitKey}IsPercentage`;
		let suppress = suppressPercentage;

		if (typeof key[isPercentageStr] !== 'undefined') {
			if (key[isPercentageStr] === 'true') {
				suppress = false;
			} else if (key[isPercentageStr] === 'false') {
				suppress = true;
			}
		}
		return (
			`${suppress && value ? '$' : ''}${value ?? 'Not Applicable'}${value && !suppress ? '%' : ''}`
		);
	};

	const renderYesNo = (val) => {
		const benKey = benefits.preventiveCare;
		const boolean = benKey[val];
		let text;

		if (boolean === 'true') {
			text = 'Yes';
		} else if (boolean === 'false') {
			text = 'No';
		} else {
			text = 'Not Applicable';
		}

		return (
			`${text}`
		);
	};

	return (
		<div className="c-benefits-list u-bg-white">
			{/* { data } */}
			<div className="u-b-b-black">
				<BenefitsTable>
					{/* Table Headings */}
					<BenefitsTable.RowGroup>
						<BenefitsTable.Row>
							<BenefitsTable.Cell columnHeader visuallyHidden>Type of Care</BenefitsTable.Cell>
							<BenefitsTable.Cell columnHeader>In Network</BenefitsTable.Cell>
							<BenefitsTable.Cell columnHeader>Out-of-Network</BenefitsTable.Cell>
						</BenefitsTable.Row>
					</BenefitsTable.RowGroup>
					{/* Table Rows */}
					<BenefitsTable.RowGroup>
						<BenefitsTable.Row>
							<BenefitsTable.Cell bold>Preventive Care</BenefitsTable.Cell>
							<BenefitsTable.Cell>{ renderBenefit('inPreventiveCare', true, 'preventiveCare') }</BenefitsTable.Cell>
							<BenefitsTable.Cell>{renderBenefit('oonDiagnosticServices', true, 'preventiveCare') }</BenefitsTable.Cell>
						</BenefitsTable.Row>
					</BenefitsTable.RowGroup>
					<BenefitsTable.RowGroup collapse heading="Dental Benefits">
						<BenefitsTable.Row>
							<BenefitsTable.Cell>{ TABLE_HEADINGS.medical[0] }</BenefitsTable.Cell>
							<BenefitsTable.Cell>{ renderBenefit('inDiagnosticServices') }</BenefitsTable.Cell>
							<BenefitsTable.Cell>{ renderBenefit('oonDiagnosticServices') }</BenefitsTable.Cell>

						</BenefitsTable.Row>
						<BenefitsTable.Row>
							<BenefitsTable.Cell>{ TABLE_HEADINGS.medical[1] }</BenefitsTable.Cell>
							<BenefitsTable.Cell>{ renderBenefit('inPreventiveServices') }</BenefitsTable.Cell>
							<BenefitsTable.Cell>{ renderBenefit('oonPreventiveServices') }</BenefitsTable.Cell>
						</BenefitsTable.Row>
						<BenefitsTable.Row>
							<BenefitsTable.Cell>{ TABLE_HEADINGS.medical[2] }</BenefitsTable.Cell>
							<BenefitsTable.Cell>{ renderBenefit('inIntermediateServices') }</BenefitsTable.Cell>
							<BenefitsTable.Cell>{ renderBenefit('oonIntermediateServices') }</BenefitsTable.Cell>
						</BenefitsTable.Row>
						<BenefitsTable.Row>
							<BenefitsTable.Cell>{ TABLE_HEADINGS.medical[3] }</BenefitsTable.Cell>
							<BenefitsTable.Cell>{ renderBenefit('inMayorServices') }</BenefitsTable.Cell>
							<BenefitsTable.Cell>{ renderBenefit('oonMayorServices') }</BenefitsTable.Cell>
						</BenefitsTable.Row>
						<BenefitsTable.Row>
							<BenefitsTable.Cell>{ TABLE_HEADINGS.medical[4] }</BenefitsTable.Cell>
							<BenefitsTable.Cell>{ renderBenefit('inAnnualMaximumsForA', true) }</BenefitsTable.Cell>
							<BenefitsTable.Cell>{ renderBenefit('oonAnnualMaximumsForA', true) }</BenefitsTable.Cell>
						</BenefitsTable.Row>
						<BenefitsTable.Row>
							<BenefitsTable.Cell>{ TABLE_HEADINGS.medical[5] }</BenefitsTable.Cell>
							<BenefitsTable.Cell>{ renderBenefit('inLifetimeMaximumsForD', true) }</BenefitsTable.Cell>
							<BenefitsTable.Cell>{ renderBenefit('oonLifetimeMaximumsForD', true) }</BenefitsTable.Cell>
						</BenefitsTable.Row>
						<BenefitsTable.Row>
							<BenefitsTable.Cell>{TABLE_HEADINGS.medical[6]}</BenefitsTable.Cell>
							<BenefitsTable.Cell>
								{ `${renderBenefit('inOrthodonticServices')}` }
							</BenefitsTable.Cell>
							<BenefitsTable.Cell>
								{ `${renderBenefit('oonOrthodonticServices')}`}
							</BenefitsTable.Cell>
						</BenefitsTable.Row>
						<BenefitsTable.Row>
							<BenefitsTable.Cell>{TABLE_HEADINGS.medical[7]}</BenefitsTable.Cell>
							<BenefitsTable.Cell>
								{`${renderYesNo('inWaitingPeriodClassA')}`}
							</BenefitsTable.Cell>
							<BenefitsTable.Cell>
								{`${renderYesNo('oonWaitingPeriodClassA')}`}
							</BenefitsTable.Cell>
						</BenefitsTable.Row>
						<BenefitsTable.Row>
							<BenefitsTable.Cell>{TABLE_HEADINGS.medical[8]}</BenefitsTable.Cell>
							<BenefitsTable.Cell>
								{`${renderYesNo('inWaitingPeriodClassB')}`}
							</BenefitsTable.Cell>
							<BenefitsTable.Cell>
								{`${renderYesNo('oonWaitingPeriodClassB')}`}
							</BenefitsTable.Cell>
						</BenefitsTable.Row>
						<BenefitsTable.Row>
							<BenefitsTable.Cell>{TABLE_HEADINGS.medical[9]}</BenefitsTable.Cell>
							<BenefitsTable.Cell>
								{`${renderYesNo('inWaitingPeriodClassC')}`}
							</BenefitsTable.Cell>
							<BenefitsTable.Cell>
								{`${renderYesNo('oonWaitingPeriodClassC')}`}
							</BenefitsTable.Cell>
						</BenefitsTable.Row>
						<BenefitsTable.Row>
							<BenefitsTable.Cell>{TABLE_HEADINGS.medical[10]}</BenefitsTable.Cell>
							<BenefitsTable.Cell>
								{`${renderYesNo('inWaitingPeriodClassD')}`}
							</BenefitsTable.Cell>
							<BenefitsTable.Cell>
								{`${renderYesNo('oonWaitingPeriodClassD')}`}
							</BenefitsTable.Cell>
						</BenefitsTable.Row>
					</BenefitsTable.RowGroup>
					<BenefitsTable.RowGroup>
						<BenefitsTable.Row>
							<BenefitsTable.Cell>
								{
									(benefits.medicalCare.inOrthodonticServices?.length > 0
								|| benefits.medicalCare.oonOrthodonticServices?.length > 0)
								&& (
									benefits.medicalCare.orthodonticsServicesFootnote
								)
								}
							</BenefitsTable.Cell>
							<BenefitsTable.Cell>
							</BenefitsTable.Cell>
						</BenefitsTable.Row>
					</BenefitsTable.RowGroup>
					<BenefitsTable.RowGroup>
						<BenefitsTable.Row noBorder>
							<BenefitsTable.Cell noPaddingTop>
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

		</div>
	);
};

DentalBenefits.propTypes = {
	data: PropTypes.shape().isRequired,
};

export default DentalBenefits;

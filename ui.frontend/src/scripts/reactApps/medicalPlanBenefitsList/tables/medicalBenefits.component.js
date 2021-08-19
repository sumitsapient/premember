import React from 'react';
import PropTypes from 'prop-types';
import reactComp from '../../componentRegistry';

import BenefitsTable from '../../sharedComponents/benefitsTable/benefitsTable.component';

const MedicalBenefits = ({ data }) => {
	const prefixes = [
		'up to',
	];
	const { benefits } = data;
	const tabLabel = data.planName;

	const renderBenefit = (benefitType, benefitKey, benefitSuffix, percentageBool = false) => {
		const type = benefits[benefitType];
		const key = benefitKey;
		const suf = benefitSuffix;
		let prefix;
		const suffixValue = type[suf];

		let hasPercentage = false;
		if (percentageBool) {
			const percentageString = `${benefitKey}IsPercentage`;
			hasPercentage = true;
			if (type[percentageString] === 'true') {
				hasPercentage = true;
			} else if (type[percentageString] === 'false') {
				hasPercentage = false;
			}
		}

		if (prefixes.includes(suffixValue)) {
			prefix = true;
		} else {
			prefix = false;
		}

		if (reactComp.ratesTable) {
			const ratesData = JSON.parse(reactComp.ratesTable.dataset.plan);
			const { enrollmentCode } = ratesData.premiumRates.selfOnly;

			// hospitalization && LV1 && InNetwork
			if (benefitType === 'hospitalization' && enrollmentCode === 'LV1' && benefitKey === 'inInPatient') {
				return (
					<div style={ { marginRight: '5%' } }>
						{'Premium - designated(Tier 1) network hospital 20% after $500 per occurrence deductible and annual deductible are met. Non-designated in-network hospital 20% after $1,000 per occurrence deductible and annual deductible are met'}
					</div>
				);
			}

			// hospitalization && LV1 && out of Network
			if (benefitType === 'hospitalization' && enrollmentCode === 'LV1' && benefitKey === 'oonInPatient') {
				return (
					<div style={ { marginRight: '5%' } }>
						{'50% after $1,000 per occurrence deductible and annual deductible are met'}
					</div>
				);
			}

			// Outpatient surgical  care – Hospital based facility  && LV1 && InNetwork
			if (benefitType === 'outPatientSurgery' && enrollmentCode === 'LV1' && benefitKey === 'inHospitalBasedFacilities') {
				return (
					<div style={ { marginRight: '5%' } }>
						{'20% after per occurrence deductible of $500 and annual deductible are met'}
					</div>
				);
			}

			// Outpatient surgical  care – Hospital based facility  && LV1 && out of Network
			if (benefitType === 'outPatientSurgery' && enrollmentCode === 'LV1' && benefitKey === 'oonHospitalBasedFacilities') {
				return (
					<div style={ { marginRight: '5%' } }>
						{'50% after per occurrence deductible of $500 and annual deductible are met'}
					</div>
				);
			}
		}

		return (
			`${type[suf] && prefix ? type[suf] : ''}${type[suf] && prefix ? ' ' : ''}${type[key] && !hasPercentage ? '$' : ''}${type[key] ? `${type[key]}` : 'Not Applicable'}${type[key] && hasPercentage ? '%' : ''} ${type[suf] && prefix === false ? type[suf] : ''}`
		);
	};

	return (
		<div className="medicalBenefits">
			<BenefitsTable ariaLabel={ `${tabLabel} benefits table` }>
				{/* Table Headings */}
				<BenefitsTable.RowGroup>
					<BenefitsTable.Row>
						<BenefitsTable.Cell columnHeader visuallyHidden>Type of Care</BenefitsTable.Cell>
						<BenefitsTable.Cell columnHeader>Network</BenefitsTable.Cell>
						<BenefitsTable.Cell columnHeader>Out-of-network</BenefitsTable.Cell>
					</BenefitsTable.Row>
				</BenefitsTable.RowGroup>
				{/* Table Body */}
				<BenefitsTable.RowGroup>
					<BenefitsTable.Row>
						<BenefitsTable.Cell bold>Preventive Care</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('preventiveCare', 'inVisit', 'inVisitSuffix')}
						</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('preventiveCare', 'oonVisit', 'oonVisitSuffix')}
						</BenefitsTable.Cell>
					</BenefitsTable.Row>
				</BenefitsTable.RowGroup>
				{/* Collapsing Body */}
				<BenefitsTable.RowGroup collapse heading="Medical Care">
					<BenefitsTable.Row>
						<BenefitsTable.Cell>Primary care physician</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('medicalCare', 'inPCP', 'inPCPSuffix', true)}
						</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('medicalCare', 'oonPCP', 'oonPCPSuffix', true)}
						</BenefitsTable.Cell>
					</BenefitsTable.Row>
					<BenefitsTable.Row>
						<BenefitsTable.Cell>Primary care physician for children under 18</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('medicalCare', 'inPCPForChildren', 'inPCPForChildrenSuffix', true)}
						</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('medicalCare', 'oonPCPForChildren', 'oonPCPForChildrenSuffix', true)}
						</BenefitsTable.Cell>
					</BenefitsTable.Row>
					<BenefitsTable.Row>
						<BenefitsTable.Cell>Virtual Visit</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('medicalCare', 'inVirtualVisit', 'inVirtualVisitSuffix', true)}
						</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('medicalCare', 'oonVirtualVisit', 'oonVirtualVisitSuffix', true)}
						</BenefitsTable.Cell>
					</BenefitsTable.Row>
					<BenefitsTable.Row>
						<BenefitsTable.Cell>Specialist</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('medicalCare', 'inSpecialist', 'inSpecialistSuffix', true)}
						</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('medicalCare', 'oonSpecialist', 'oonSpecialistSuffix', true)}
						</BenefitsTable.Cell>
					</BenefitsTable.Row>
					<BenefitsTable.Row>
						<BenefitsTable.Cell>Specialist Premium Tier 1</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('medicalCare', 'inSpecialistTier1', 'inSpecialistTier1Suffix', true)}
						</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('medicalCare', 'oonSpecialistTier1', 'oonSpecialistTier1Suffix', true)}
						</BenefitsTable.Cell>
					</BenefitsTable.Row>
				</BenefitsTable.RowGroup>
				<BenefitsTable.RowGroup collapse heading="Hospitalization">
					<BenefitsTable.Row>
						<BenefitsTable.Cell>Inpatient</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('hospitalization', 'inInPatient', 'inInPatientSuffix', true)}
						</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('hospitalization', 'oonInPatient', 'oonInPatientSuffix', true)}
						</BenefitsTable.Cell>
					</BenefitsTable.Row>
				</BenefitsTable.RowGroup>
				<BenefitsTable.RowGroup collapse heading="Outpatient Surgery, Urgent & Emergency Care">
					<BenefitsTable.Row>
						<BenefitsTable.Cell>Outpatient surgical care - Freestanding Facility</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('outPatientSurgery', 'inFreestandingFacilities', 'inFreestandingFacilitiesSuffix', true)}
						</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('outPatientSurgery', 'oonFreestandingFacilities', 'oonFreestandingFacilitiesSuffix', true)}
						</BenefitsTable.Cell>
					</BenefitsTable.Row>
					<BenefitsTable.Row>
						<BenefitsTable.Cell>Outpatient surgical care - Hospital-based-facility</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('outPatientSurgery', 'inHospitalBasedFacilities', 'inHospitalBasedFacilitiesSuffix', true)}
						</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('outPatientSurgery', 'oonHospitalBasedFacilities', 'oonHospitalBasedFacilitiesSuffix', true)}
						</BenefitsTable.Cell>
					</BenefitsTable.Row>
					<BenefitsTable.Row>
						<BenefitsTable.Cell>Urgent Care</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('outPatientSurgery', 'inUrgentCare', 'inUrgentCareSuffix', true)}
						</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('outPatientSurgery', 'oonUrgentCare', 'oonUrgentCareSuffix', true)}
						</BenefitsTable.Cell>
					</BenefitsTable.Row>
					<BenefitsTable.Row>
						<BenefitsTable.Cell>Emergency Room Service</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('outPatientSurgery', 'inEmergencyRoomServices', 'inEmergencyRoomServicesSuffix', true)}
						</BenefitsTable.Cell>
						<BenefitsTable.Cell>
							{renderBenefit('outPatientSurgery', 'oonEmergencyRoomServices', 'oonEmergencyRoomServicesSuffix', true)}
						</BenefitsTable.Cell>
					</BenefitsTable.Row>
				</BenefitsTable.RowGroup>
			</BenefitsTable>
		</div>
	);
};

MedicalBenefits.propTypes = {
	data: PropTypes.shape().isRequired,
};

export default MedicalBenefits;

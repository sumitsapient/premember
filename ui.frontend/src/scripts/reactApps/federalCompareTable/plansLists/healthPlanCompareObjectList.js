import { generateRowData, generateRowDataLink } from '../helpers';

export default function federalHealthPlanCompareTableObject(labels, plansList) {
	return [
		{
			sectionTitle: labels.medicalBenefitsTitle,
			sectionSubTitle: labels.medicalBenefitsSubtitle,
			rows: [
				generateRowData(labels.medicalBenefitsDeductible, null, plansList, 'price', 'medicalBenefits', 'deductible', ['inSelfOnly', 'oonSelfOnly'], 'Individual'),
				generateRowData(labels.medicalBenefitsDeductible, null, plansList, 'price', 'medicalBenefits', 'deductible', ['inSelfPlusOne', 'oonSelfPlusOne'], 'Individual + One'),
				generateRowData(labels.medicalBenefitsDeductible, null, plansList, 'price', 'medicalBenefits', 'deductible', ['inSelfPlusFamily', 'oonSelfPlusFamily'], 'Individual + Family'),
				generateRowData(labels.medicalBenefitsOutOfPocketMaximum, null, plansList, 'price', 'medicalBenefits', 'outOfPocketMaximum', ['inSelfOnly', 'oonSelfOnly'], 'Individual'),
				generateRowData(labels.medicalBenefitsOutOfPocketMaximum, null, plansList, 'price', 'medicalBenefits', 'outOfPocketMaximum', ['inSelfPlusOne', 'oonSelfPlusOne'], 'Individual + One'),
				generateRowData(labels.medicalBenefitsOutOfPocketMaximum, null, plansList, 'price', 'medicalBenefits', 'outOfPocketMaximum', ['inSelfPlusFamily', 'oonSelfPlusFamily'], 'Individual + Family'),
			],
		},
		{
			sectionTitle: 'Medical Care',
			sectionSubTitle: 'Amount you pay',
			rows: [
				generateRowData('Primary Care Visit', null, plansList, 'price_suffix', 'medicalBenefits', 'medicalCare', ['inPCP', 'inPCPSuffix'], 'Adult'),
				generateRowData('Primary Care Visit', null, plansList, 'price_suffix', 'medicalBenefits', 'medicalCare', ['inPCPForChildren', 'inPCPForChildrenSuffix'], 'Children'),
				generateRowData('Specialist Visit', null, plansList, 'price_suffix', 'medicalBenefits', 'medicalCare', ['inSpecialist', 'inSpecialistSuffix']),
				generateRowData('Specialist Visit', null, plansList, 'price_suffix', 'medicalBenefits', 'medicalCare', ['inSpecialistTier1', 'inSpecialistTier1Suffix'], 'Tier 1'),
				generateRowData('Virtual Visit', null, plansList, 'price_suffix', 'medicalBenefits', 'medicalCare', ['inVirtualVisit', 'inVirtualVisitSuffix']),
				generateRowData('Inpatient Services', null, plansList, 'price_suffix', 'medicalBenefits', 'hospitalization', ['inInPatient', 'inInPatientSuffix']),
				generateRowData('Outpatient - Emergency Room Services', null, plansList, 'price_suffix', 'medicalBenefits', 'outPatientSurgery', ['inEmergencyRoomServices', 'inEmergencyRoomServicesSuffix']),
				generateRowData('Outpatient - Freestanding Facilities', null, plansList, 'price_suffix', 'medicalBenefits', 'outPatientSurgery', ['inFreestandingFacilities', 'inFreestandingFacilitiesSuffix']),
				generateRowData('Outpatient - Urgent Care', null, plansList, 'price_suffix', 'medicalBenefits', 'outPatientSurgery', ['inUrgentCare', 'inUrgentCareSuffix']),
				generateRowData('Preventive Care', null, plansList, 'price_suffix', 'medicalBenefits', 'preventiveCare', ['inVisit', 'inVisitSuffix']),
			],
		},
		{
			sectionTitle: labels.morePlanInfoTitle,
			sectionSubTitle: null,
			rows: [
				generateRowDataLink(labels.morePlanInfoBenefits, null, plansList, 'link',
					['planBenefitsSummaryPdf']),
			],
		},
	];
}

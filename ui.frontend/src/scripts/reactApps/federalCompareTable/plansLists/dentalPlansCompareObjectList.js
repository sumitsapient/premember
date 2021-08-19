import { generateRowData, generateRowDataLink } from '../helpers';

export default function federalDentalPlanCompareTableObject(labels, plansList) {
	return [
		{
			sectionTitle: labels.planCostsTitle,
			sectionSubTitle: labels.planCostsSubtitle,
			rows: [generateRowData(labels.planCostsDeductibleTitle, labels.planCostsDeductibleDescription, plansList, 'price', 'planCosts', 'deductible', ['individual', 'family'], 'Individual'),
				generateRowData(labels.planCostsAnnualMaximumTitle, labels.planCostsAnnualMaximumDescription, plansList, 'price', 'planCosts', 'annualMaximum', ['individual', 'family'], 'Individual')],
		},
		{
			sectionTitle: labels.preventiveCareTitle,
			sectionSubTitle: labels.preventiveCareSubtitle,
			rows: [
				generateRowData('Preventive Care', null, plansList, 'price', null, 'preventiveCare', ['inPreventiveCare', 'oonDiagnosticServices'], null),
				generateRowData('Waiting Period for Class A', null, plansList, 'boolean', null, 'preventiveCare', ['inWaitingPeriodClassA', 'oonWaitingPeriodClassA'], null),
				generateRowData('Waiting Period for Class B', null, plansList, 'boolean', null, 'preventiveCare', ['inWaitingPeriodClassB', 'oonWaitingPeriodClassB'], null),
				generateRowData('Waiting Period for Class C', null, plansList, 'boolean', null, 'preventiveCare', ['inWaitingPeriodClassC', 'oonWaitingPeriodClassC'], null),
				generateRowData('Waiting Period for Class D', null, plansList, 'boolean', null, 'preventiveCare', ['inWaitingPeriodClassD', 'oonWaitingPeriodClassD'], null),
			],
		},
		{
			sectionTitle: labels.medicalCareTitle,
			sectionSubTitle: 'Amount you pay',
			rows: [
				generateRowData(labels.medicalCarePreventiveServices, null, plansList, 'price', null, 'medicalCare', ['inPreventiveServices', 'oonPreventiveServices'], null),
				generateRowData(labels.medicalCareDiagnosticServices, null, plansList, 'price', null, 'medicalCare', ['inDiagnosticServices', 'oonDiagnosticServices'], null),
				generateRowData(labels.medicalCareIntermediateServices, null, plansList, 'price', null, 'medicalCare', ['inIntermediateServices', 'oonIntermediateServices'], null),
				generateRowData(labels.medicalCareMayorServices, null, plansList, 'price', null, 'medicalCare', ['inMayorServices', 'oonMayorServices'], null),
				generateRowData(labels.medicalCareOrthodonticServices, null, plansList, 'price', null, 'medicalCare', ['inOrthodonticServices', 'oonOrthodonticServices'], null),
				generateRowData(labels.medicalCareAnnualMaximumsForA, null, plansList, 'price', null, 'medicalCare', ['inAnnualMaximumsForA', 'oonAnnualMaximumsForA'], null),
				generateRowData(labels.medicalCareAnnualMaximumsForB, null, plansList, 'price', null, 'medicalCare', ['inAnnualMaximumsForB', 'oonAnnualMaximumsForB'], null),
				generateRowData(labels.medicalCareAnnualMaximumsForC, null, plansList, 'price', null, 'medicalCare', ['inAnnualMaximumsForC', 'oonMedicalCareAnnualMaximumsForC'], null),
				generateRowData(labels.medicalCareAnnualMaximumsForD, null, plansList, 'price', null, 'medicalCare', ['inAnnualMaximumsForD', 'oonAnnualMaximumsForD'], null),
				generateRowData(labels.medicalCareLifetimeMaximumsForD, null, plansList, 'price', null, 'medicalCare', ['inLifetimeMaximumsForD', 'oonLifetimeMaximumsForD'], null),
			],
		},
		{
			sectionTitle: labels.morePlanInfoTitle,
			sectionSubTitle: null,
			rows: [
				generateRowDataLink(labels.morePlanInfoBenefits, null, plansList, 'link', ['planBenefitsSummaryPdf']),
			],
		},
	];
}

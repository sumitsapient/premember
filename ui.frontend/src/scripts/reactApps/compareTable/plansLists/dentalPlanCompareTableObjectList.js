import { generateRowData } from '../../../utils/plan';

export default function dentalPlanCompareTableObjectsList(plansCopy, planList) {
	return [
		{
			sectionTitle: plansCopy.comparisonChapter1Headline,
			sectionSubTitle: plansCopy.comparisonChapter1SubHeadline,
			rows: [
				generateRowData(plansCopy.comparisonChapter1Topic1, plansCopy.comparisonChapter1Topic1Description, planList, 'simple_text',
					['comparisonNetworkBenefits.copy']),
				generateRowData(plansCopy.comparisonChapter1Topic2, plansCopy.comparisonChapter1Topic2Description, planList, 'extended_per_month',
					['comparisonPreventiveCare.copy', 'preventiveCareCleaning', 'preventiveCareCleaningMonths']),
				generateRowData(plansCopy.comparisonChapter1Topic3, plansCopy.comparisonChapter1Topic3Description, planList, 'simple_text',
					['comparisonOrthodontia.copy']),
			],
		},
		{
			sectionTitle: plansCopy.comparisonChapter2Headline,
			sectionSubTitle: plansCopy.comparisonChapter2SubHeadline,
			rows: [
				generateRowData(plansCopy.comparisonChapter2Topic1, plansCopy.comparisonChapter2Topic1Description, planList, 'link',
					['comparisonProviderSearchLink', 'comparisonSearchCoverageLinkText.copy', 'planName']),
			],
		},
		{
			sectionTitle: plansCopy.comparisonChapter3Headline,
			sectionSubTitle: plansCopy.comparisonChapter3SubHeadline,
			subSectionTitle: plansCopy.comparisonChapter3SubSection1Headline,
			rows: [
				generateRowData(plansCopy.comparisonChapter3SubSection1Topic1, plansCopy.comparisonChapter3SubSection1Topic1Description, planList, 'extended_costs_text',
					[undefined, 'deductibleIndividualInNetwork', undefined, 'deductibleIndividualOutOfNetwork', 'comparisonNonOrthodonticsIndividualNoDeductible.copy'], 'price'),
				generateRowData(plansCopy.comparisonChapter3SubSection1Topic1Title2, plansCopy.comparisonChapter3SubSection1Topic1Description2, planList, 'extended_costs_text',
					[undefined, 'deductibleFamilyInNetwork', undefined, 'deductibleFamilyOutOfNetwork', 'comparisonNonOrthodonticsFamilyNoDeductible.copy'], 'price'),
				generateRowData(plansCopy.comparisonChapter3SubSection1Topic2, plansCopy.comparisonChapter3SubSection1Topic2Description, planList, 'extended_costs_text',
					[undefined, 'annualMaximumIndividualInNetwork', undefined, 'annualMaximumIndividualOutOfNetwork', 'comparisonNonOrthodonticsIndividualNoAnnualMaximum.copy'], 'price'),
				generateRowData(plansCopy.comparisonChapter3SubSection1Topic2Title2, plansCopy.comparisonChapter3SubSection1Topic2Description2, planList, 'extended_costs_text',
					[undefined, 'annualMaximumFamilyInNetwork', undefined, 'annualMaximumFamilyOutOfNetwork', 'comparisonNonOrthodonticsFamilyNoAnnualMaximum.copy'], 'price'),
			],
		},
		{
			subSectionTitle: plansCopy.comparisonChapter3SubSection2Headline,
			rows: [
				generateRowData(plansCopy.comparisonChapter3SubSection2Topic1, plansCopy.comparisonChapter3SubSection2Topic1Description, planList, 'extended_costs_text',
					[undefined, 'deductibleIndividualInNetworkOrtho', undefined, 'deductibleIndividualOutOfNetworkOrtho', 'comparisonOrthodonticsIndividualNoDeductible.copy'], 'price'),
				generateRowData(plansCopy.comparisonChapter3SubSection2Topic1Title2, plansCopy.comparisonChapter3SubSection2Topic1Description2, planList, 'extended_costs_text',
					[undefined, 'deductibleFamilyInNetworkOrtho', undefined, 'deductibleFamilyOutOfNetworkOrtho', 'comparisonOrthodonticsFamilyNoDeductible.copy'], 'price'),
				generateRowData(plansCopy.comparisonChapter3SubSection2Topic2, plansCopy.comparisonChapter3SubSection2Topic2Description, planList, 'extended_costs_text',
					[undefined, 'annualMaximumIndividualInNetworkOrtho', undefined, 'annualMaximumIndividualOutOfNetworkOrtho', 'comparisonOrthodonticsIndividualNoAnnualMaximum.copy'], 'price'),
				generateRowData(plansCopy.comparisonChapter3SubSection2Topic2Title2, plansCopy.comparisonChapter3SubSection2Topic2Description2, planList, 'extended_costs_text',
					[undefined, 'annualMaximumFamilyInNetworkOrtho', undefined, 'annualMaximumFamilyOutOfNetworkOrtho', 'comparisonOrthodonticsFamilyNoAnnualMaximum.copy'], 'price'),
			],
		},
		{
			sectionTitle: plansCopy.comparisonChapter4Headline,
			sectionSubTitle: plansCopy.comparisonChapter4SubHeadline,
			subSectionTitle: plansCopy.comparisonChapter4SubSection1Headline,
			rows: [
				generateRowData(plansCopy.comparisonChapter4SubSection1Topic1, plansCopy.comparisonChapter4SubSection1Topic1Description, planList, 'extended_costs_text',
					['routineExamsAndEvaluations', undefined, 'routineExamsAndEvaluationsOutOfNetwork', 'routineExamsAndEvaluationsPaidByMember', undefined, 'comparisonRoutineExamsOutOfNetwork.copy'], 'percentage'),
				generateRowData(plansCopy.comparisonChapter4SubSection1Topic2, plansCopy.comparisonChapter4SubSection1Topic2Description, planList, 'extended_costs_text',
					['radiograph', undefined, 'radiographOutOfNetwork', undefined, undefined, 'comparisonRadiographsOutOfNetwork.copy'], 'percentage'),
				generateRowData(plansCopy.comparisonChapter4SubSection1Topic3, plansCopy.comparisonChapter4SubSection1Topic3Description, planList, 'extended_costs_text',
					['labAndOtherDiagnosticTests', undefined, 'labAndOtherDiagnosticTestsOutOfNetwork', undefined, undefined, 'comparisonLabDiagnosticTestsOutOfNetwork.copy'], 'percentage'),
			],
		},
		{
			subSectionTitle: plansCopy.comparisonChapter4SubSection2Headline,
			rows: [
				generateRowData(plansCopy.comparisonChapter4SubSection2Topic1, plansCopy.comparisonChapter4SubSection2Topic1Description, planList, 'extended_costs_text',
					['cleaning', undefined, 'cleaningOutOfNetwork', 'cleaningOutOfNetworkPaidByMember', undefined, 'comparisonCleaningOutOfNetwork.copy'], 'percentage'),
				generateRowData(plansCopy.comparisonChapter4SubSection2Topic2, plansCopy.comparisonChapter4SubSection2Topic2Description, planList, 'extended_costs_text',
					['flourideTreatment', undefined, 'flourideTreatmentOutOfNetwork', undefined, undefined, 'comparisonFlourideTreatmentOutOfNetwork.copy'], 'percentage'),
				generateRowData(plansCopy.comparisonChapter4SubSection2Topic3, plansCopy.comparisonChapter4SubSection2Topic3Description, planList, 'extended_costs_text',
					['sealants', undefined, 'sealantsOutOfNetwork', undefined, undefined, 'comparisonSealantsOutOfNetwork.copy'], 'percentage'),
				generateRowData(plansCopy.comparisonChapter4SubSection2Topic4, plansCopy.comparisonChapter4SubSection2Topic4Description, planList, 'extended_costs_text',
					['spaceMaintainers', undefined, 'spaceMaintainersOutOfNetwork', undefined, undefined, 'comparisonSpaceMaintainersOutOfNetwork.copy'], 'percentage'),
			],
		},
		{
			subSectionTitle: plansCopy.comparisonChapter4SubSection3Headline,
			rows: [
				generateRowData(plansCopy.comparisonChapter4SubSection3Topic1, plansCopy.comparisonChapter4SubSection3Topic1Description, planList, 'extended_costs_text',
					['restorations', undefined, 'restorationsOutOfNetwork', undefined, 'comparisonRestorationsBenefit.copy', 'comparisonRestorationsOutOfNetwork.copy'], 'percentage'),
				generateRowData(plansCopy.comparisonChapter4SubSection3Topic2, plansCopy.comparisonChapter4SubSection3Topic2Description, planList, 'extended_costs_text',
					['emergencyTreatmentGeneralServices', undefined, 'emergencyTreatmentGeneralServicesOutOfNetwork', undefined, 'comparisonEmergencyTreatmentBenefit.copy', 'comparisonEmergencyTreatmentOutOfNetwork.copy'], 'percentage'),
				generateRowData(plansCopy.comparisonChapter4SubSection3Topic3, plansCopy.comparisonChapter4SubSection3Topic3Description, planList, 'extended_costs_text',
					['simpleExtractions', undefined, 'simpleExtractionsOutOfNetwork', undefined, 'comparisonSimpleExtractionsBenefit.copy', 'comparisonSimpleExtractionsOutOfNetwork.copy'], 'percentage'),
				generateRowData(plansCopy.comparisonChapter4SubSection3Topic4, plansCopy.comparisonChapter4SubSection3Topic4Description, planList, 'extended_costs_text',
					['oralSurgery', undefined, 'oralSurgeryOutOfNetwork', undefined, 'comparisonOralSurgeryBenefit.copy', 'comparisonOralSurgeryOutOfNetwork.copy'], 'percentage'),
				generateRowData(plansCopy.comparisonChapter4SubSection3Topic5, plansCopy.comparisonChapter4SubSection3Topic5Description, planList, 'extended_costs_text',
					['periodontics', undefined, 'periodonticsOutOfNetwork', undefined, 'comparisonPeriodonticsBenefit.copy', 'comparisonPeriodonticsOutOfNetwork.copy'], 'percentage'),
				generateRowData(plansCopy.comparisonChapter4SubSection3Topic6, plansCopy.comparisonChapter4SubSection3Topic6Description, planList, 'extended_costs_text',
					['endodontics', undefined, 'endodonticsOutOfNetwork', undefined, 'comparisonEndodonticsBenefit.copy', 'comparisonEndodonticsOutOfNetwork.copy'], 'percentage'),
			],
		},
		{
			subSectionTitle: plansCopy.comparisonChapter4SubSection4Headline,
			rows: [
				generateRowData(plansCopy.comparisonChapter4SubSection4Topic1, plansCopy.comparisonChapter4SubSection4Topic1Description, planList, 'extended_costs_text',
					['inlaysOnlaysCrowns', undefined, 'inlaysOnlaysCrownsOutOfNetwork', undefined, 'comparisonInlaysOnlaysCrownsBenefit.copy', 'comparisonInlaysOnlaysCrownsOutOfNetwork.copy'], 'percentage'),
				generateRowData(plansCopy.comparisonChapter4SubSection4Topic2, plansCopy.comparisonChapter4SubSection4Topic2Description, planList, 'extended_costs_text',
					['denturesAndRemovableProsthetics', undefined, 'denturesAndRemovableProstheticsOutOfNetwork', undefined, 'comparisonDenturesAndRemovableProstheticsBenefit.copy', 'comparisonDenturesAndRemovableProstheticsOutOfNetwork.copy'], 'percentage'),
				generateRowData(plansCopy.comparisonChapter4SubSection4Topic3, plansCopy.comparisonChapter4SubSection4Topic3Description, planList, 'extended_costs_text',
					['fixedPartialDentures', undefined, 'fixedPartialDenturesOutOfNetwork', undefined, 'comparisonFixedPartialDenturesBenefit.copy', 'comparisonFixedPartialDenturesOutOfNetwork.copy'], 'percentage'),
			],
		},
		{
			subSectionTitle: plansCopy.comparisonChapter4SubSection5Headline,
			rows: [
				generateRowData(plansCopy.comparisonChapter4SubSection5Topic1, plansCopy.comparisonChapter4SubSection5Topic1Description, planList, 'extended_costs_text',
					['diagnoseCorrectMisalignment', undefined, 'diagnoseCorrectMisalignmentOutOfNetwork', undefined, 'comparisonDiagnoseMisalignmentBenefit.copy', 'comparisonDiagnoseMisalignmentOutOfNetwork.copy'], 'percentage'),
			],
		},
		{
			sectionTitle: plansCopy.comparisonChapter5Headline,
			sectionSubTitle: plansCopy.comparisonChapter5SubHeadline,
			rows: [
				generateRowData(plansCopy.comparisonChapter5Topic1, plansCopy.comparisonChapter5Topic1Description, planList, 'link',
					['fullBenefitsLink', 'comparisonFullBenefitsLinkText.copy', 'planName']),
			],
		},
	];
}

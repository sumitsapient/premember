import { generateRowData } from '../../../utils/plan';

export default function healthPlanCompareTableObjectsList(plansCopy, planList) {
	return [
		{
			sectionTitle: plansCopy.comparisonChapter1Headline,
			sectionSubTitle: plansCopy.comparisonChapter1SubHeadline,
			rows: [
				generateRowData(plansCopy.comparisonChapter1Topic1, plansCopy.comparisonChapter1Topic1Description, planList, 'simple_text',
					['comparisonNetworkCoverage.copy']),
				generateRowData(plansCopy.comparisonChapter1Topic2, plansCopy.comparisonChapter1Topic2Description, planList, 'simple_text',
					['comparisonPcpRequirement.copy']),
				generateRowData(plansCopy.comparisonChapter1Topic3, plansCopy.comparisonChapter1Topic3Description, planList, 'simple_text',
					['comparisonChapter1Topic3Option1.copy', 'comparisonChapter1Topic3Option2.copy', 'comparisonChapter1Topic3Option3.copy', 'comparisonChapter1Topic3Option4.copy']),
			],
		},
		{
			sectionTitle: plansCopy.comparisonChapter2Headline,
			sectionSubTitle: plansCopy.comparisonChapter2SubHeadline,
			rows: [
				generateRowData(plansCopy.comparisonChapter2Topic1, plansCopy.comparisonChapter2Topic1Description, planList, 'link',
					['comparisonProviderLink', 'providersComparisonSearchCoverageLinkText.copy', 'planName'], 'Doctors, Clinics or Facilities'),
				generateRowData(plansCopy.comparisonChapter2Topic2, plansCopy.comparisonChapter2Topic2Description, planList, 'link',
					['prescriptionSearchLink.copy', 'prescriptionsComparisonSearchCoverageLinkText.copy', 'planName', 'suppressPrescriptionCoverage'], 'Prescriptions'),
			],
		},
		{
			sectionTitle: plansCopy.comparisonChapter3Headline,
			sectionSubTitle: plansCopy.comparisonChapter3SubHeadline,
			rows: [
				generateRowData(plansCopy.comparisonChapter3Topic2, plansCopy.comparisonChapter3Topic2Description, planList, 'price',
					['deductibleIndividual'], 'Individual'),
				generateRowData(plansCopy.comparisonChapter3Topic2, plansCopy.comparisonChapter3Topic2Description, planList, 'price',
					['deductibleFamily'], 'Family'),
				generateRowData(plansCopy.comparisonChapter3Topic2, plansCopy.comparisonChapter3Topic2Description, planList, 'price',
					['deductibleIndividualPlusOne'], 'Individual + One'),
				generateRowData(plansCopy.comparisonChapter3Topic2, plansCopy.comparisonChapter3Topic2Description, planList, 'price',
					['deductibleIndividualPlusChildren'], 'Individual + Children'),
				generateRowData(plansCopy.comparisonChapter3Topic2, plansCopy.comparisonChapter3Topic2Description, planList, 'price',
					['deductibleIndividualPlusFamily'], 'Individual + Family'),
				generateRowData(plansCopy.comparisonChapter3Topic3, plansCopy.comparisonChapter3Topic3Description, planList, 'percentage',
					['comparisonCoinsurance', 'comparisonCoinsuranceRangeStart', 'comparisonCoinsuranceRangeEnd'], 'Cost varies by service'),
				generateRowData(plansCopy.comparisonChapter3Topic4, plansCopy.comparisonChapter3Topic4Description, planList, 'price',
					['outOfPocketLimitIndividual'], 'Individual'),
				generateRowData(plansCopy.comparisonChapter3Topic4, plansCopy.comparisonChapter3Topic4Description, planList, 'price',
					['outOfPocketLimitFamily'], 'Family'),
				generateRowData(plansCopy.comparisonChapter3Topic4, '', planList, 'price',
					['outOfPocketLimitIndividualPlusOne'], 'Individual + One'),
				generateRowData(plansCopy.comparisonChapter3Topic4, '', planList, 'price',
					['outOfPocketLimitIndividualPlusChildren'], 'Individual + Children'),
				generateRowData(plansCopy.comparisonChapter3Topic4, '', planList, 'price',
					['outOfPocketLimitIndividualPlusFamily'], 'Individual + Family'),
			],
		},
		{
			sectionTitle: plansCopy.comparisonChapter4Headline,
			sectionSubTitle: plansCopy.comparisonChapter4SubHeadline,
			rows: [
				generateRowData(plansCopy.comparisonChapter4Topic1, plansCopy.comparisonChapter4Topic1Description, planList, 'care_service_costs',
					['comparisonPrimaryCareVisitCopay', 'comparisonPrimaryCareVisitCoinsurance'], null, 'comparisonPrimaryCareVisit'),
				generateRowData(plansCopy.comparisonChapter4Topic2, plansCopy.comparisonChapter4Topic2Description, planList, 'care_service_costs',
					['comparisonSpecialistVisitsCopay', 'comparisonSpecialistVisitsCoinsurance'], null, 'comparisonSpecialistVisits'),
				generateRowData(plansCopy.comparisonChapter4Topic3, plansCopy.comparisonChapter4Topic3Description, planList, 'care_service_costs',
					['comparisonLabTestingCopay', 'comparisonLabTestingCoinsurance'], null, 'comparisonLabTesting'),
				generateRowData(plansCopy.comparisonChapter4Topic4, plansCopy.comparisonChapter4Topic4Description, planList, 'care_service_costs',
					['comparisonXRayTestingCopay', 'comparisonXRayTestingCoinsurance'], null, 'comparisonXRayTesting'),
				generateRowData(plansCopy.comparisonChapter4Topic5, plansCopy.comparisonChapter4Topic5Description, planList, 'care_service_costs',
					['comparisonVirtualVisitsCopay', 'comparisonVirtualVisitsCoinsurance'], null, 'comparisonVirtualVisits'),
				generateRowData(plansCopy.comparisonChapter4Topic6, plansCopy.comparisonChapter4Topic6Description, planList, 'care_service_costs',
					['comparisonEmergencyRoomCopay', 'comparisonEmergencyRoomCoinsurance'], null, 'comparisonEmergencyRoom'),
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

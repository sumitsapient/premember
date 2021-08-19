import { generateRowData } from '../../../utils/plan';

export default function visionPlanCompareTableObjectsList(
	plansCopy,
	planList,
	hideAllowance,
) {
	let materialsNetworkTilesTitle = 'Copay';

	// Check if custom materials label exists
	for (let i = 0; i < planList.length; i += 1) {
		const list = planList[i];

		if (list && Object.prototype.hasOwnProperty.call(list, 'materialsLabel')) {
			materialsNetworkTilesTitle = list.materialsLabel;
			break;
		}
	}

	return [
		{
			sectionTitle: plansCopy.comparisonChapter1Headline,
			sectionSubTitle: plansCopy.comparisonChapter1SubHeadline,
			rows: [
				generateRowData(
					plansCopy.comparisonChapter1Topic1,
					plansCopy.comparisonChapter1Topic1Description,
					planList,
					'per_month',
					[
						'eyeExamsFrequency',
						'eyeExamsOption.copy',
					],
					'Once every ',
				),
				generateRowData(
					plansCopy.comparisonChapter1Topic2,
					plansCopy.comparisonChapter1Topic2Description,
					planList,
					'per_month',
					[
						'spectaclesLensesFrequency',
						'framesFrequencyOption.copy',
					],
					'Once every ',
				),
				generateRowData(
					plansCopy.comparisonChapter1Topic3,
					plansCopy.comparisonChapter1Topic3Description,
					planList,
					'per_month',
					[
						'framesFrequency',
						'eyeGlassLensesOption.copy',
					],
					'Once every ',
				),
				generateRowData(
					plansCopy.comparisonChapter1Topic4,
					plansCopy.comparisonChapter1Topic4Description,
					planList,
					'per_month',
					[
						'contactLensesFrequency',
						'contactLensesOption.copy',
					],
					'Once every ',
				),
			],
		},
		{
			sectionTitle: plansCopy.comparisonChapter2Headline,
			sectionSubTitle: plansCopy.comparisonChapter2SubHeadline,
			rows: [
				generateRowData(
					plansCopy.comparisonChapter2Topic1,
					plansCopy.comparisonChapter2Topic1Description,
					planList,
					'link',
					[
						'comparisonProviderLink',
						'comparisonSearchCoverageLinkText.copy',
						'planName',
					],
					'Doctors, Clinics or Facilities',
				),
			],
		},
		{
			sectionTitle: plansCopy.comparisonChapter3Headline,
			sectionSubTitle: plansCopy.comparisonChapter3SubHeadline,
			rows: [
				generateRowData(
					plansCopy.comparisonChapter3Topic1,
					plansCopy.comparisonChapter3Topic1Description,
					planList,
					'price',
					['eyeExamsNetwork'],
					'Copay',
				),
				generateRowData(
					plansCopy.comparisonChapter3Topic2,
					plansCopy.comparisonChapter3Topic2Description,
					planList,
					'price',
					['materialsNetwork'],
					materialsNetworkTilesTitle,
				),
			],
		},
		hideAllowance === 'true'
			? {

				rows: [
				],
			} : {
				sectionTitle: plansCopy.comparisonChapter4Headline,
				sectionSubTitle: plansCopy.comparisonChapter4SubHeadline,
				subSectionTitle: plansCopy.comparisonChapter4SubSection1Headline,
				rows: [
					generateRowData(
						plansCopy.comparisonChapter4SubSection1Topic1,
						plansCopy.comparisonChapter4SubSection1Topic1Description,
						planList,
						'price',
						['framesPrivatePracticeNetwork'],
						'Allowance',
					),
					generateRowData(
						plansCopy.comparisonChapter4SubSection1Topic2,
						plansCopy.comparisonChapter4SubSection1Topic2Description,
						planList,
						'price',
						['framesRetailChainNetwork'],
						'Allowance',
					),
				],
			},

		{
			subSectionTitle: plansCopy.comparisonChapter4SubSection2Headline,
			rows: [
				generateRowData(
					plansCopy.comparisonChapter4SubSection2Topic1,
					plansCopy.comparisonChapter4SubSection2Topic1Description,
					planList,
					'price',
					['contactLensesElectiveNetwork'],
				),
				generateRowData(
					plansCopy.comparisonChapter4SubSection2Topic2,
					plansCopy.comparisonChapter4SubSection2Topic2Description,
					planList,
					'simple_text',
					['contactLensesNecessaryNetwork.copy'],
				),
			],
		},
		{
			sectionTitle: plansCopy.comparisonChapter5Headline,
			sectionSubTitle: plansCopy.comparisonChapter5SubHeadline,
			subSectionTitle: plansCopy.comparisonChapter5SubSection1Headline,
			rows: [
				generateRowData(
					plansCopy.comparisonChapter5SubSection1Topic1,
					plansCopy.comparisonChapter5SubSection1Topic1Description,
					planList,
					'price',
					['eyeExamsOutOfNetwork'],
					'Up to',
				),
			],
		},
		{
			subSectionTitle: plansCopy.comparisonChapter5SubSection2Headline,
			rows: [
				generateRowData(
					plansCopy.comparisonChapter5SubSection2Topic1,
					plansCopy.comparisonChapter5SubSection2Topic1Description,
					planList,
					'price',
					['framesReimbursementsOutOfNetwork'],
					'Up to',
				),
				generateRowData(
					plansCopy.comparisonChapter5SubSection2Topic2,
					plansCopy.comparisonChapter5SubSection2Topic2Description,
					planList,
					'extended_text',
					[
						'comparisonChapter5SubSection2Topic2Option1.copy',
						'spectaclesLensesSingleVision',
						'comparisonChapter5SubSection2Topic2Option2.copy',
						'spectaclesLensesLinedBifocal',
						'comparisonChapter5SubSection2Topic2Option3.copy',
						'spectaclesLensesLinedTrifocal',
						'comparisonChapter5SubSection2Topic2Option4.copy',
						'spectaclesLensesLenticular',
					],
				),
				generateRowData(
					plansCopy.comparisonChapter5SubSection2Topic3,
					plansCopy.comparisonChapter5SubSection2Topic3Description,
					planList,
					'extended_text',
					[
						'comparisonChapter5SubSection2Topic3Option1.copy',
						'contactLensesNecessaryOutOfNetwork',
						'comparisonChapter5SubSection2Topic3Option2.copy',
						'contactLensesElectiveOutOfNetwork',
					],
				),
			],
		},
		{
			sectionTitle: plansCopy.comparisonChapter6Headline,
			sectionSubTitle: plansCopy.comparisonChapter6SubHeadline,
			rows: [
				generateRowData(
					plansCopy.comparisonChapter6Topic1,
					plansCopy.comparisonChapter6Topic1Description,
					planList,
					'link',
					[
						'fullBenefitsLink',
						'comparisonFullBenefitsLinkText.copy',
						'planName',
					],
				),
			],
		},
	];
}

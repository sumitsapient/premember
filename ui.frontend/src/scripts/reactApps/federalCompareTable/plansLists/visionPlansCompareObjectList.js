import { generateRowData, generateRowDataLink } from '../helpers';

export default function federalVisionPlanCompareTableObject(labels, plansList) {
	return [
		{
			sectionTitle: labels.premiumRatesTitle,
			sectionSubTitle: labels.premiumRatesSubtitle,
			rows: [
				generateRowData(labels.premiumRatesSelfOnly, null, plansList, 'price_vision', 'premiumRates', 'selfOnly', ['biWeekly', 'monthly']),
				generateRowData(labels.premiumRatesSelfPlusOneTitle, null, plansList, 'price_vision', 'premiumRates', 'selfPlusOne', ['biWeekly', 'monthly']),
				generateRowData(labels.premiumRatesSelfPlusFamilyTitle, null, plansList, 'price_vision', 'premiumRates', 'selfAndFamily', ['biWeekly', 'monthly']),
			],
		},
		{
			sectionTitle: labels.copaysTitle,
			sectionSubTitle: labels.copaysSubtitle,
			rows: [
				generateRowData(labels.copaysAnnualEyeExam, null, plansList, 'price_suffix', null, 'copays', ['annualEyeExam', 'annualEyeExamPrefix']),
				generateRowData(labels.copaysEyeGlasses, null, plansList, 'price_suffix', null, 'copays', ['eyeGlasses', 'eyeGlassesPrefix']),
				generateRowData(labels.copaysStandardScratchResistantCoating, null, plansList, 'price_suffix', null, 'copays', ['standardScratchResistantCoating', 'standardScratchResistantCoatingPrefix']),
				generateRowData(labels.copaysPolycarbonateLenses, null, plansList, 'price_suffix', null, 'copays', ['polycarbonateLenses', 'polycarbonateLensesPrefix']),
				generateRowData(labels.copaysTransitionLenses, null, plansList, 'price_suffix', null, 'copays', ['transitionLenses', 'transitionLensesPrefix']),
				generateRowData(labels.copaysTier1AntiReflectiveCoating, null, plansList, 'price_suffix', null, 'copays', ['tierOneAntiReflectiveCoating', 'tierOneAntiReflectiveCoatingPrefix']),
				generateRowData(labels.copaysTintedLenses, null, plansList, 'price_suffix', null, 'copays', ['tintedLenses', 'tintedLensesPrefix']),
				generateRowData(labels.copaysUVCoating, null, plansList, 'price_suffix', null, 'copays', ['uvCoating', 'uvCoatingPrefix']),
				generateRowData(labels.copaysTier1Progressive, null, plansList, 'price_suffix', null, 'copays', ['tierOneProgressive', 'tierOneProgressivePrefix']),
				generateRowData(labels.copaysHighEndTier2To4Progressive, null, plansList, 'price_suffix', null, 'copays', ['highEndTierTwoFourProgressive', 'highEndTierTwoFourProgressivePrefix']),
				generateRowData(labels.copaysHighEndPlasticUpTo173, null, plansList, 'price_suffix', null, 'copays', ['highEndPlasticUpToOnePointSeventyThree', 'highEndPlasticUpToOnePointSeventyThreePrefix']),
			],
		},
		{
			sectionTitle: labels.allowancesTitle,
			sectionSubTitle: labels.allowancesSubtitle,
			rows: [
				generateRowData(labels.allowancesAnnualEyeExam, null, plansList, 'price_suffix', null, 'allowances', ['frames', 'framesPrefix']),
				generateRowData(labels.allowancesContactLenses, null, plansList, 'price_suffix', null, 'allowances', ['contactLenses', 'contactLensesPrefix']),
				generateRowData(labels.allowancesContactLensFittingEvaluation, null, plansList, 'price_suffix', null, 'allowances', ['contactLensFittingEvaluation', 'contactLensFittingEvaluationPrefix']),
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

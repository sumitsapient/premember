const planSortOptions = {
	HEALTH: [
		{
			label: 'Deductible: Low to High',
			value: 'deductible-ASC',
			property: 'deductibleIndividual',
			direction: 1,
		},
		{
			label: 'Deductible: High to Low',
			value: 'deductible-DESC',
			property: 'deductibleIndividual',
			direction: -1,
		},
		{
			label: 'Out-of-Pocket Limit: Low to High',
			value: 'outOfPocketLimit-ASC',
			property: 'outOfPocketLimitIndividual',
			direction: 1,
		},
		{
			label: 'Out-of-Pocket Limit: High to Low',
			value: 'outOfPocketLimit-DESC',
			property: 'outOfPocketLimitIndividual',
			direction: -1,
		},
	],
	DENTAL: [
		{
			label: 'Deductible: Low to High',
			value: 'deductible-ASC',
			property: 'deductibleIndividualInNetwork',
			direction: 1,
		},
		{
			label: 'Deductible: High to Low',
			value: 'deductible-DESC',
			property: 'deductibleIndividualInNetwork',
			direction: -1,
		},
		{
			label: 'Annual Max: Low to High',
			value: 'annualMax-ASC',
			property: 'annualMaximumIndividualInNetwork',
			direction: 1,
		},
		{
			label: 'Annual Max: High to Low',
			value: 'annualMax-DESC',
			property: 'annualMaximumIndividualInNetwork',
			direction: -1,
		},
	],
	VISION: [
		{
			label: 'Copay: Low to High',
			value: 'copay-ASC',
			property: 'eyeExamsNetwork',
			direction: 1,
		},
		{
			label: 'Copay: High to Low',
			value: 'copay-DESC',
			property: 'eyeExamsNetwork',
			direction: -1,
		},
		{
			label: 'Allowance: Low to High',
			value: 'allowance-ASC',
			property: 'framesPrivatePracticeNetwork',
			direction: 1,
		},
		{
			label: 'Allowance: High to Low',
			value: 'allowance-DESC',
			property: 'framesPrivatePracticeNetwork',
			direction: -1,
		},
	],
};

export default planSortOptions;

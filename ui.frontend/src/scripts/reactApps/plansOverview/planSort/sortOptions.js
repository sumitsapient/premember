const planSortOptions = {
	DENTAL: [
		{
			label: 'Deductible: Low to High',
			value: 'deductible-ASC',
			costType: 'deductible',
			property: 'individual',
			direction: 1,
		},
		{
			label: 'Deductible: High to Low',
			value: 'deductible-DESC',
			costType: 'deductible',
			property: 'individual',
			direction: -1,
		},
		{
			label: 'Annual Maximum: Low to High',
			value: 'annualMaximum-ASC',
			costType: 'annualMaximum',
			property: 'individual',
			direction: 1,
		},
		{
			label: 'Annual Maximum: High to Low',
			value: 'annualMaximum-DESC',
			costType: 'annualMaximum',
			property: 'individual',
			direction: -1,
		},
	],
	HEALTH: [
		{
			label: 'Deductible: Low to High',
			value: 'deductible-ASC',
			costType: 'deductible',
			property: 'individual',
			direction: 1,
		},
		{
			label: 'Deductible: High to Low',
			value: 'deductible-DESC',
			costType: 'deductible',
			property: 'individual',
			direction: -1,
		},
		{
			label: 'Out-of-Pocket Limit: Low to High',
			value: 'outOfPocketLimit-ASC',
			costType: 'outOfPocketLimit',
			property: 'individual',
			direction: 1,
		},
		{
			label: 'Out-of-Pocket Limit: High to Low',
			value: 'outOfPocketLimit-DESC',
			costType: 'outOfPocketLimit',
			property: 'individual',
			direction: -1,
		},
	],
	VISION: [
		{
			label: 'Copays: Low to High',
			value: 'copays-ASC',
			costType: 'copays',
			property: 'eyeGlasses',
			direction: 1,
		},
		{
			label: 'Copays: High to Low',
			value: 'copays-DESC',
			costType: 'copays',
			property: 'eyeGlasses',
			direction: -1,
		},
	],
};

export default planSortOptions;

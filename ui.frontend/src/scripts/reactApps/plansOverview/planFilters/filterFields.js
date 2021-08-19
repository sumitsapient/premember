const filterOptions = [
	{
		name: 'employeeType',
		label: 'Are you a Postal Employee?',
		planType: ['health'],
		options: [
			{
				text: 'Non-Postal Employee',
				value: 'nonPostalEmployee',
			},
			{
				text: 'Postal Employee',
				value: 'postalEmployee',
			},
		],
	},
	{
		name: 'familySize',
		label: 'Family Size',
		options: [
			{
				text: 'Self',
				value: 'selfOnly',
			},
			{
				text: 'Self Plus One',
				value: 'selfPlusOne',
			},
			{
				text: 'Self And Family',
				value: 'selfAndFamily',
			},
		],
	},
	{
		name: 'payFrequency',
		label: 'Pay Frequency',
		displayCondition: 'employeeType = nonPostalEmployee',
		options: [
			{
				text: 'Bi-weekly',
				value: 'biweekly',
			},
			{
				text: 'Monthly',
				value: 'monthly',
			},
		],
	},
	{
		name: 'payFrequency',
		label: 'Pay Frequency',
		displayCondition: 'employeeType = postalEmployee',
		planType: ['health'],
		options: [
			{
				text: 'Biweekly Category 1',
				value: 'biweeklyCategory1',
			},
			{
				text: 'Biweekly Category 2',
				value: 'biweeklyCategory2',
			},
		],
	},
];

export default filterOptions;

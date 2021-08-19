import {
	isEmpty, getObjectValuesList, normalizeText, getCopayTypeValuesList,
} from './helpers';

export const createCostObject = (title, costName1, costValue1, costName2, costValue2, costText2) => (
	{
		title,
		costValue1,
		costName1,
		costValue2,
		costName2,
		costText2,
	}
);

export const createIconBlockObject = (title, text, svgIcon) => (
	{
		title,
		text,
		svgIcon,
	}
);

export const generateTilesList = (plansList, keyValues) => {
	const tileList = [];
	plansList.forEach((plan) => {
		if (isEmpty(plan)) {
			tileList.push({});
		} else {
			const valuesList = getObjectValuesList(plan, keyValues);
			tileList.push(valuesList);
		}
	});

	return tileList;
};
export const generateTileTypes = (plansList, copayType) => {
	const tileTypes = [];
	if (copayType !== undefined) {
		plansList.forEach((plan) => {
			if (isEmpty(plan)) {
				tileTypes.push({});
			} else {
				const valuesList = getCopayTypeValuesList(plan, copayType);
				tileTypes.push(valuesList);
			}
		});
	}


	return tileTypes;
};

export const generateRowData = (rowTitle, rowDescription, planList, type, keyValues, tilesTitle, copayType) => (
	{
		rowTitle,
		rowDescription,
		type,
		tilesTitle,
		planList,
		rowType: copayType,
		tiles: generateTilesList(planList, keyValues),
		tileCopayTypes: generateTileTypes(planList, `${copayType}CopayType`),
	}
);

export const getPlanSelectLabel = (index) => {
	const ordinalNumberLabels = ['first', 'second', 'third'];

	return `Select ${ordinalNumberLabels[index]} plan`;
};

export const getSelectedOptions = (options, counter) => {
	const newSelectedOptionsList = [];

	for (let i = 0; i < counter; i += 1) {
		newSelectedOptionsList.push({});
	}

	return [...options, ...newSelectedOptionsList];
};

export const getSelectedPlans = (options, plans) => {
	const plansArray = [];

	options.forEach((element) => {
		if (element.value === undefined) {
			plansArray.push({});
		} else {
			const planObject = plans.find(plan => plan.id === element.value);
			plansArray.push(planObject.details.data);
		}
	});

	return plansArray;
};
export const getSelectedPlansFederal = (options, plans) => {
	const plansArray = [];

	options.forEach((element) => {
		if (element.value === undefined) {
			plansArray.push({});
		} else {
			const planObject = plans.find(plan => plan.type === element.value);
			plansArray.push(planObject);
		}
	});

	return plansArray;
};

export const getPlanCardEventName = (cardTitle, type, text) => {
	const normalizedTitle = normalizeText(cardTitle);
	const normalizedText = normalizeText(text);

	return `${normalizedTitle}|${type}|${normalizedText}`;
};

export const getCompareTableEventName = (planTitle, type, text, rowTitle) => {
	const normalizeTitle = normalizeText(planTitle);
	const normalizeRowTitle = !rowTitle ? '' : `|${normalizeText(rowTitle)}`;
	const normalizedText = !text ? '' : `|${normalizeText(text)}`;
	const elemType = !type ? '' : `|${type}`;

	return `${normalizeTitle}${elemType}${normalizeRowTitle}${normalizedText}`;
};

export const calculateNumberOfPlans = (options, list) => (list.length > 2 ? 3 - options.length : 2 - options.length);

export const getFormattedPrice = (value) => {
	const parsed = typeof value === 'number' ? value : parseFloat(value);

	if (value % 1 !== 0) {
		return parsed.toFixed(2).toLocaleString('en');
	}

	return parsed.toLocaleString('en');
};

export const replaceTokenWithValue = (string, value) => string.replace(/#\[[^{}]+\]/g, value);

export const pluralizeText = (count, noun, suffix = 's') => `${count} ${noun}${(count === 1 || count === '1') ? '' : suffix}`;

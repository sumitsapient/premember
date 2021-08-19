import {
	isEmpty,
} from '../../utils/helpers';


export const getObjectValuesList = (object, keys) => {
	const valuesObject = {};
	keys.forEach((key, id) => {
		valuesObject[`value${id}`] = object[key];
		valuesObject[`valuePercentage${id}`] = object[`${key}IsPercentage`];
	});

	return valuesObject;
};

export const generateTilesList = (plansList, type, key, keyValues) => {
	const tileList = [];
	plansList.forEach((plan) => {
		if (isEmpty(plan)) {
			tileList.push({});
		} else {
			let planVals;
			if (type !== null) {
				planVals = plan[type];
			} else {
				planVals = plan;
			}

			const valuesList = getObjectValuesList(planVals[key], keyValues);
			tileList.push(valuesList);
		}
	});

	return tileList;
};

export const generateTilesListLink = (plansList, keyValues) => {
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

export const generateRowDataLink = (rowTitle, rowDescription, planList, type, keyValues, tilesTitle, copayType) => (
	{
		rowTitle,
		rowDescription,
		type,
		tilesTitle,
		planList,
		rowType: copayType,
		tiles: generateTilesListLink(planList, keyValues),
		tileCopayTypes: null,
	}
);

export const generateRowData = (rowTitle, rowDescription, planList, type, benefitType, key, keyValues, tilesTitle, copayType) => (
	{
		rowTitle,
		rowDescription,
		planList,
		type,
		tilesTitle,
		rowType: copayType,
		tiles: generateTilesList(planList, benefitType, key, keyValues),
		tileCopayTypes: null,
	}
);

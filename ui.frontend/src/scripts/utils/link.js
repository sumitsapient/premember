export function createURL(id, type) {
	if (!type) {
		return `plan-details.${id}`;
	}

	return `${type.toLowerCase()}-plans/plan-details.${id}`;
}

export function createComparisonURL(type) {
	return `${type.toLowerCase()}-plans/comparison-table`;
}

import React from 'react';
import PropTypes from 'prop-types';

import { camelCase, isEmpty, sentenceCase } from '../../utils/helpers';
import { getFormattedPrice } from '../../utils/plan';
import Table from '../sharedComponents/benefitsTable/benefitsTable.component';

/**
 * Table Headings
 */
const TABLE_HEADINGS = {
	medical: [
		'Type of Care',
		'Network',
		'Out-of-network',
	],
	pharmacy: [
		'Tiers',
		'Retail up to 30-day supply',
		'Home Delivery up to 90-day supply',
	],
};

/**
 * Maps given data values to format readable by table
 *
 * @param {Object.<string, string>} benefits - Original benefits object
 *
 * @returns {object} - Correctly mapped benefits object
 */
const getTableMap = (benefits) => {
	const mapVal = {};
	const stripValues = [
		'Network',
		'Notes',
		'OutOfNetwork',
		'Suffix',
		'DeliveryUp',
		'RetailUp',
	];
	const rgx = new RegExp(stripValues.join('|'), 'gi');
	let type;
	let name;

	Object.keys(benefits).forEach((key) => {
		type = key.match(rgx);
		name = key.replace(rgx, '');

		// TODO: Report issue
		// TEMPORARY: Typo fix
		if (name === 'doctorsAnsSpecialist') {
			name = 'doctorsAndSpecialist';
		}

		if (!Object.prototype.hasOwnProperty.call(mapVal, name)) {
			if (name === 'items') {
				mapVal.icons = [];
			} else {
				mapVal[name] = {};
			}
		}

		// add non-suffix values
		if (type && type.length < 2) {
			mapVal[name][type[0]] = benefits[key];
		}

		// add suffix values
		if (type && type.length > 1 && type.includes('Suffix')) {
			// create suffix object, if it doesn't exist
			if (!Object.prototype.hasOwnProperty.call(mapVal[name], 'Suffix')) {
				mapVal[name].Suffix = {};
			}

			mapVal[name].Suffix[type.filter(typ => typ !== 'Suffix')[0]] = benefits[key];
		}

		// add icon values
		if (!type && name === 'items') {
			Object.keys(benefits[name]).forEach(icon => mapVal.icons.push(benefits[name][icon]));
		}
	});

	return mapVal;
};

const MedicareBenefits = ({
	data,
}) => {
	/**
	 * Renders table column headers
	 *
	 * @param {String} benefitKey - Benefit key
	 */
	const renderTableHeaders = (benefitKey) => {
		if (Object.prototype.hasOwnProperty.call(TABLE_HEADINGS, benefitKey)) {
			return (
				<Table.RowGroup>
					<Table.Row>
						{TABLE_HEADINGS[benefitKey].map(key => (
							<Table.Cell columnHeader key={ key }>{key}</Table.Cell>
						))}
					</Table.Row>
				</Table.RowGroup>
			);
		}

		return null;
	};

	const getRowIndex = (col) => {
		let index;
		switch (col) {
			case 'tierLevelOne':
				index = 1;
				break;
			case 'tierLevelTwo':
				index = 2;
				break;
			case 'tierLevelThree':
				index = 3;
				break;
			case 'tierLevelFour':
				index = 4;
				break;
			case 'documentsAndResources':
				index = 5;
				break;
			default:
				index = 0;
		}

		return index;
	};

	/**
	 * Renders table rows
	 *
	 * @param {String} benefitKey - Benefit key
	 * @param {object} benefits - Benefit values
	 */
	const renderTableRows = (benefitKey, benefits, fullBenefits) => {
		const rows = [];
		if (Object.prototype.hasOwnProperty.call(TABLE_HEADINGS, benefitKey)) {
			const firstCols = Object.keys(benefits);
			const headings = TABLE_HEADINGS[benefitKey];
			// remove first element
			headings.shift();
			firstCols.forEach((col) => {
				const vals = benefits[col];
				const orderIndex = getRowIndex(col);

				if (col !== 'documentsAndResources') {
					rows.push((
						<Table.Row key={ col } index={ orderIndex }>
							<Table.Cell bold>{sentenceCase(col, 'camel')}</Table.Cell>

							{ headings.map((heading) => {
								const prop = camelCase(heading);
								let cellVal = null;

								// check for prop
								if (Object.prototype.hasOwnProperty.call(vals, prop)) {
									cellVal = getFormattedPrice(vals[prop]);
								} else {
									// check for matching characters
									Object.keys(vals).forEach((val) => {
										if (prop.includes(val)) {
											cellVal = getFormattedPrice(vals[val]);
										}
									});
								}

								if (benefitKey === 'medical') {
									cellVal = `${cellVal}%`;
								}

								if (benefitKey === 'pharmacy') {
									cellVal = `$${cellVal}`;
								}

								// check for suffix
								if (Object.prototype.hasOwnProperty.call(vals, 'Suffix')) {
									if (Object.prototype.hasOwnProperty.call(vals.Suffix, prop)) {
										cellVal += `, ${vals.Suffix[prop]}`;
									}
								}

								if (cellVal) {
									return (
										<Table.Cell key={ `${col}-${prop}` }>{cellVal}</Table.Cell>
									);
								}

								return null;
							})}

							{	Object.prototype.hasOwnProperty.call(vals, 'Notes') && (
								<Table.Notes>{vals.Notes}</Table.Notes>
							)}
						</Table.Row>
					));
				} else if (col === 'documentsAndResources') {
					const documents = fullBenefits[col];

					if (!isEmpty(documents) && !isEmpty(documents.items)) {
						const arr = [];
						const { items } = documents;

						Object.keys(items).forEach((key) => {
							arr.push(items[key]);
						});
						rows.push(
							(
								<Table.RowGroup key={ col } index={ orderIndex }>
									<Table.Row noBorder>
										<Table.Cell noPaddingTop>
											<div className="c-benefits-list__files">
												<h3 className="u-color-blue u-m-t-0">{documents.sectionTitle}</h3>
												<ul className="o-list-inline">
													{arr.map((item, index) => (
														<li className="o-list-inline__item" key={ index }>
															<a href={ item.path }>{item.title}</a>
														</li>
													))}
												</ul>
											</div>
										</Table.Cell>
									</Table.Row>
								</Table.RowGroup>
							),
						);
					}
				}
			});
		}
		rows.sort((a, b) => a.props.index - b.props.index);
		return rows;
	};

	/**
	 * Renders icon elements
	 *
	 * @param {object} benefits - Mapped benefits object
	 */
	const renderIcons = benefits => (
		<div className="u-bg-white u-p-lg">
			<div className="o-grid">
				{benefits.icons.map((icon, index) => (
					<div
						className={ `o-grid__item-12 o-grid__item-6@md ${(index + 1) !== benefits.icons.length ? 'u-m-b' : ''}` }
						key={ icon.title }
					>
						<div className="c-icon-block o-media c-icon-block--sidebyside u-f-align-center" data-container="Icon Block">
							<div className="o-media__img">
								<div className="c-icon-box c-icon-box--circle u-bg-blue-medium u-color-blue">
									<div className="cmp-icon">
										<img src={ icon.icon } alt="" className="o-media__img u-m-r" />
									</div>
								</div>
							</div>
							<div className="o-media__body u-color-black">
								<p className="u-text-bold u-m-b-0">{icon.title}</p>
								<p>{icon.description}</p>
							</div>
						</div>
					</div>
				))}
			</div>
		</div>
	);

	const { benefits, benefitKey } = data;
	const tabLabel = sentenceCase(benefitKey);
	const parsedBenefits = getTableMap(benefits);

	if (Object.prototype.hasOwnProperty.call(parsedBenefits, 'icons')) {
		return renderIcons(parsedBenefits);
	}

	return (
		<Table ariaLabel={ `${tabLabel} benefits table` }>
			{ renderTableHeaders(benefitKey)}

			<Table.RowGroup>
				{renderTableRows(benefitKey, parsedBenefits, benefits)}
			</Table.RowGroup>
		</Table>
	);
};

MedicareBenefits.propTypes = {
	data: PropTypes.objectOf(PropTypes.any).isRequired,
};

export default MedicareBenefits;

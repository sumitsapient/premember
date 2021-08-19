import React from 'react';
import PropTypes from 'prop-types';
import DataRow from './dataRow.component';
import { isEmpty } from '../../../utils/helpers';

const DataSection = ({ id, section }) => {
	const data = section;
	const isCoverageSection = section.sectionTitle === 'Search Coverage';
	const isPlanCostSection = section.sectionTitle === 'Health Plan Costs';
	const deductible = {
		hasPlusOne: 'false',
		hasPlusChildren: 'false',
		hasPlusFamily: 'false',
	};
	const copay = {
		hasPlusOne: 'false',
		hasPlusChildren: 'false',
		hasPlusFamily: 'false',
	};
	const {
		nextIsSubSection,
		gridClasses,
	} = data;
	let hasPharmacy;
	let cardClasses = [
		'c-card',
		'c-card--compare-table',
		nextIsSubSection ? 'c-card--section-heading' : null,
		!data.sectionTitle && data.subSectionTitle ? 'c-card--section-sub' : null,
	];

	function rowCheck(planName, index, type, varName) {
		const plans = data.rows[index].planList;
		const planBooleanArray = [];
		const objectData = type;

		plans.forEach((plan) => {
			if (!isEmpty(plan) && typeof plan[planName] !== 'undefined') {
				planBooleanArray.push(plan[planName].toString());
			}
		});

		if (planBooleanArray.length) {
			data.rows[index].hide = false;
			objectData[varName] = true;
		} else {
			data.rows[index].hide = true;
			objectData[varName] = false;
		}
	}

	// Determines if prescription coverage should be suppressed in coverage sections
	if (isCoverageSection) {
		const plans = data.rows[1].planList;
		const coverageBooleanArray = [];

		data.rows[0].hide = false;

		plans.forEach((plan) => {
			if (!isEmpty(plan) && typeof plan.suppressPrescriptionCoverage !== 'undefined') {
				coverageBooleanArray.push(plan.suppressPrescriptionCoverage.toString());
			}
		});

		if (coverageBooleanArray.includes('false')) {
			hasPharmacy = true;
			data.sectionSubTitle = 'Providers and Prescriptions';
			data.rows[1].hide = false;
		} else {
			hasPharmacy = false;
			data.sectionSubTitle = 'Providers';
			data.rows[1].hide = true;
		}
	}

	if (isPlanCostSection) {
		data.rows[0].hide = false;
		data.rows[1].hide = false;
		data.rows[6].hide = false;
		data.rows[7].hide = false;

		data.rows.forEach((row, index) => {
			if (row.rowTitle === 'Deductible' || row.rowTitle === 'Out-of-pocket Limit') {
				switch (row.tilesTitle) {
					case 'Individual + One':
						if (row.rowTitle === 'Deductible') {
							rowCheck('deductibleIndividualPlusOne', index, deductible, 'hasPlusOne');
						}
						if (row.rowTitle === 'Out-of-pocket Limit') {
							rowCheck('outOfPocketLimitIndividualPlusOne', index, copay, 'hasPlusOne');
						}
						break;
					case 'Individual + Children':
						if (row.rowTitle === 'Deductible') {
							rowCheck('deductibleIndividualPlusChildren', index, deductible, 'hasPlusChildren');
						}
						if (row.rowTitle === 'Out-of-pocket Limit') {
							rowCheck('outOfPocketLimitIndividualPlusChildren', index, copay, 'hasPlusChildren');
						}
						break;
					case 'Individual + Family':
						if (row.rowTitle === 'Deductible') {
							rowCheck('deductibleIndividualPlusFamily', index, deductible, 'hasPlusFamily');
						}
						if (row.rowTitle === 'Out-of-pocket Limit') {
							rowCheck('outOfPocketLimitIndividualPlusFamily', index, copay, 'hasPlusFamily');
						}
						break;
					default:
						break;
				}
			}
		});
	}

	cardClasses = cardClasses.filter(item => item).join(' ');

	return (
		<div className={ cardClasses }>
			{/* Title */}
			{
				data.sectionTitle && (
					<div
						className="c-card__header"
						id={ `title-${id}` }
					>
						<h2 className="c-card__title">{ data.sectionTitle }</h2>
						{
							data.sectionSubTitle && (
								<span className="c-card__title">{ data.sectionSubTitle }</span>
							)
						}
					</div>
				)
			}
			{/* Subtitle */}
			{
				data.subSectionTitle && (
					<div
						className="c-card__header c-card__header--subtitle"
						id={ `subTitle-${id}` }
					>
						<h3 className="c-card__title">{ data.subSectionTitle }</h3>
					</div>
				)
			}
			{/* Data */}
			<div role="rowgroup" className="c-card__body">
				{ (isCoverageSection && hasPharmacy === false)
					|| (isPlanCostSection
						&& (deductible.hasPlusOne === false
						|| deductible.hasPlusChildren === false
						|| deductible.hasPlusFamily === false
						|| copay.hasPlusOne === false
						|| copay.hasPlusChildren === false
						|| copay.hasPlusFamily === false)) ? (
						data.rows.filter(row => !row.hide).map((row, i) => (
							<DataRow
								id={ i }
								sectionID={ id }
								key={ i }
								rowTitle={ row.rowTitle }
								rowDescription={ row.rowDescription }
								type={ row.type }
								tilesTitle={ row.tilesTitle }
								tiles={ row.tiles }
								plansList={ row.planList }
								rowType={ row.rowType }
								gridClasses={ gridClasses }
							/>
						))
					) : (
						data.rows.map((row, i) => (
							<DataRow
								id={ i }
								sectionID={ id }
								key={ i }
								rowTitle={ row.rowTitle }
								rowDescription={ row.rowDescription }
								type={ row.type }
								tilesTitle={ row.tilesTitle }
								tiles={ row.tiles }
								plansList={ row.planList }
								rowType={ row.rowType }
								gridClasses={ gridClasses }
							/>
						))
					)
				}
			</div>
		</div>
	);
};

DataSection.propTypes = {
	id: PropTypes.number.isRequired,
	section: PropTypes.shape().isRequired,
};

export default DataSection;

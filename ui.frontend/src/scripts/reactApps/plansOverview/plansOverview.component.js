import React, { useEffect, useRef, useState } from 'react';
import PropTypes from 'prop-types';
import axios from 'axios';

import PlanFilters from './planFilters/planFilters.component';
import PlanSort from './planSort/planSort.component';
import PlanCard from './planCard/planCard.component';

import filterFields from './planFilters/filterFields';
import sortOptions from './planSort/sortOptions';

const PlansOverview = ({
	data,
	plansUrl,
	planType,
	zipCode,
}) => {
	const [plans, setPlans] = useState(data);
	const	[selected, setSelected] = useState({
		employeeType: '',
		familySize: '',
		payFrequency: '',
		planType,
		sort: '',
		zipCode,
	});
	const selectedRef = useRef(selected);
	let sort;

	// set sort based on plan type
	switch (planType) {
		case 'dental':
			sort = sortOptions.DENTAL;
			break;
		case 'health':
			sort = sortOptions.HEALTH;
			break;
		case 'vision':
			sort = sortOptions.VISION;
			break;
		default:
			sort = sortOptions.HEALTH;
	}

	/**
	 * Gets updated plans based on zip code
	 */
	const updatePlans = async () => {
		try {
			const response = await axios.get(plansUrl);

			if (response.data) {
				// check if returned data contains plans
				if (Object.prototype.hasOwnProperty.call(response.data, 'plans')) {
					setPlans(response.data.plans);
				}
			}
		} catch (err) {
			console.error(err);
		}
	};

	/**
	 * Update selected items
	 *
	 * @param {String|object} value - Updated value
	 * @param {String} [prop] - Name of value to update
	 */
	const updateSelected = (value, prop) => {
		let newVal;

		if (value.constructor.name === 'Object') {
			newVal = {
				...selectedRef.current,
				...value,
			};
		// if string value is passed, it must contain a prop as well
		} else if (prop) {
			newVal = {
				...selectedRef.current,
				[prop]: value,
			};
		}

		selectedRef.current = newVal;
		setSelected(newVal);
	};

	/**
	 * Sort plan options by given value
	 *
	 * @param {number} sortValue - Sort value number
	 */
	const sortPlanOptions = (sortValue) => {
		const list = [...data];
		const {
			costType,
			property,
			direction,
		} = sort.find(item => item.value === sortValue);

		const sortedPlans = list.sort((option1, option2) => {
			let value1 = option1;
			let value2 = option2;

			if (planType !== 'vision') {
				value1 = value1.planCosts;
				value2 = value2.planCosts;
			}

			value1 = value1[costType][property];
			value2 = value2[costType][property];

			return direction * (value1 - value2);
		});

		setPlans(sortedPlans);
	};

	/**
	 * Checks conditional statement
	 *
	 * @param {String} condition - Conditional statement using "=" sign
	 * @returns {Boolean}
	 */
	const checkConditionalStatement = (condition) => {
		const conditionArr = condition.split('=');
		const prop = conditionArr[0].trim();
		const field = filterFields.find(el => el.name === prop);

		if (field && Object.prototype.hasOwnProperty.call(selected, prop)) {
			// if conditional field is not included in plan type, show dependent by default
			if (Object.prototype.hasOwnProperty.call(field, 'planType') && !field.planType.includes(planType)) {
				return true;
			}

			// if prop has already been set
			if (selected[prop]) {
				return selected[prop] === conditionArr[1].trim();
			}

			// if selected value has not been set, use local data
			return field.options[0].value === conditionArr[1].trim();
		}

		return false;
	};

	// Set default values for selected options (if they don't exist)
	useEffect(() => {
		const newVals = {};

		// update filter props
		filterFields.forEach((field) => {
			const { name, displayCondition } = field;
			let updateVal = true;

			if (!selected[name]) {
				if (displayCondition && !checkConditionalStatement(displayCondition)) {
					updateVal = false;
				}

				// check if plan type matches
				if (field.planType && !field.planType.includes(planType)) {
					updateVal = false;
				}

				if (updateVal) {
					newVals[name] = field.options[0].value;
				}
			}
		});

		// update sort prop
		if (!selected.sort) {
			newVals.sort = sort[0].value;
		}

		if (Object.keys(newVals).length > 0) {
			updateSelected(newVals);
		}
	}, []);

	return (
		<>
			{
				planType !== 'medicare' && (
					<>
						<PlanFilters
							checkConditionalStatement={ checkConditionalStatement }
							filterFields={ filterFields }
							hasPlans={ plans.length > 0 }
							selected={ selected }
							updatePlans={ updatePlans }
							updateSelected={ updateSelected }
						/>
						{
							plans.length > 0 && (
								<PlanSort
									onSortChange={ sortPlanOptions }
									updateSelected={ updateSelected }
									sortOptions={ sort }
									sortValue={ selected.sort }
								/>
							)
						}
					</>
				)
			}
			{ plans.map((plan, index) => (
				<PlanCard planData={ plan } key={ `plan-${index}` } selected={ selected } />
			)) }
		</>
	);
};

PlansOverview.propTypes = {
	data: PropTypes.arrayOf(PropTypes.object).isRequired,
	plansUrl: PropTypes.string.isRequired,
	planType: PropTypes.oneOf([
		'dental',
		'health',
		'medicare',
		'vision',
	]).isRequired,
	zipCode: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
};

export default PlansOverview;

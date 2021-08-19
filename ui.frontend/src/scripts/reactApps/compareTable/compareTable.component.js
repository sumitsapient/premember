import React, {
	useState,
	useCallback,
	useLayoutEffect,
	useEffect,
} from 'react';
import PropTypes from 'prop-types';
import useWindowSize from '../hooks/useWindowSize';
import { isEmpty, isAuthor, normalizeText } from '../../utils/helpers';
import {
	getSelectedOptions,
	getSelectedPlans,
	calculateNumberOfPlans,
} from '../../utils/plan';
import HeaderSection from './headerSection/headerSection.component';
import DataSection from './dataSection/dataSection.component';
import healthPlanCompareTableObject from './plansLists/healthPlanCompareTableObjectsList';
import visionPlanCompareTableObject from './plansLists/visionPlanCompareTableObjectsList';
import dentalPlanCompareTableObject from './plansLists/dentalPlanCompareTableObjectList';

const CompareTable = ({ data }) => {
	const [plansList] = useState(data.options);
	const [plansCopy] = useState(data.copy.data);
	const [sectionsData, setSectionsData] = useState([]);
	const [selectedOptions, setSelectedOptions] = useState([]);
	const [plansType] = useState(data.type);
	const isMobileBreakpoint = useWindowSize();

	let gridClasses = ['o-grid__item-6'];

	if (
		(plansType === 'DENTAL' || plansType === 'VISION')
		&& plansList.length < 3
	) {
		gridClasses.push('o-grid__item-4@md');
	} else {
		gridClasses.push('o-grid__item-3@md');
	}

	gridClasses = gridClasses.filter(item => item).join(' ');

	// sets plans by default, if number of plans are less than or equal to number of select inputs available
	useEffect(() => {
		const numberOfPlans = calculateNumberOfPlans(selectedOptions, plansList);

		if (isEmpty(plansList) || plansList.length === 1) {
			return;
		}

		if (!isMobileBreakpoint && plansList.length <= numberOfPlans) {
			const newSelectedOptions = [];
			plansList.forEach((plan) => {
				let planName;

				if (Object.prototype.hasOwnProperty.call(plan, 'details')) {
					// eslint-disable-next-line prefer-destructuring
					planName = plan.details.data.planName;
				} else if (Object.prototype.hasOwnProperty.call(plan, 'title')) {
					planName = plan.title;
				}

				if (planName) {
					newSelectedOptions.push({
						value: plan.id,
						planName,
					});
				}
			});

			setSelectedOptions(newSelectedOptions);
		}
	}, []);

	useLayoutEffect(() => {
		if (plansList.length > 2 && selectedOptions.length === 3) {
			setSelectedOptions((oldValue) => {
				const newSelectedOptions = oldValue;
				oldValue.pop();
				return [...newSelectedOptions];
			});
		}

		if (selectedOptions.length === 0) {
			setSelectedOptions([]);
		}
	}, [isMobileBreakpoint]);

	useEffect(() => {
		const numberOfPlans = calculateNumberOfPlans(selectedOptions, plansList);
		const options = getSelectedOptions(selectedOptions, numberOfPlans);
		const plans = getSelectedPlans(options, plansList);
		let sectionsList;

		if (plansType === 'HEALTH') {
			sectionsList = healthPlanCompareTableObject(plansCopy, plans);
		}

		if (plansType === 'DENTAL') {
			sectionsList = dentalPlanCompareTableObject(plansCopy, plans);
		}

		if (plansType === 'VISION') {
			const { hideAllowance } = plansList[0].details.data;
			sectionsList = visionPlanCompareTableObject(
				plansCopy,
				plans,
				hideAllowance,
			);
		}

		setSectionsData(sectionsList);
	}, [selectedOptions]);

	const formatSelectedOptions = () => selectedOptions
		.map(option => option.planName)
		.filter(name => name)
		.map(normalizeText)
		.join('|');

	const comparisonTakingPlace = () => selectedOptions.length > 1;

	const updateAnalytics = () => {
		// Show compared options (plural) - if at least two options
		// are not selected, then we are not doing comparison
		if (comparisonTakingPlace()) {
			window.digitalData.plan.compareNames = formatSelectedOptions();
		} else {
			window.digitalData.plan.compareNames = '';
		}
	};

	useEffect(() => {
		if (isAuthor() || !window.digitalData) {
			return;
		}
		updateAnalytics();
	}, [selectedOptions]);

	const onChange = useCallback((evt) => {
		const {
			id, value, selectedIndex, options,
		} = evt.target;
		const planName = options[selectedIndex].text;
		evt.target.blur();

		setSelectedOptions((oldValue) => {
			const newSelectedOptions = oldValue;

			if (value === 'default') {
				newSelectedOptions.splice(id, 1);
				return [...newSelectedOptions];
			}

			if (isEmpty(newSelectedOptions[id])) {
				return [...oldValue, { value, planName }];
			}

			newSelectedOptions[id].value = value;
			newSelectedOptions[id].planName = planName;
			return [...newSelectedOptions];
		});
	}, []);

	if (isEmpty(plansList)) {
		return 'No options defined for the plan.';
	}

	if (plansList.length === 1) {
		return 'Only one Plan Available';
	}

	return (
		<>
			<HeaderSection
				plansList={ plansList }
				planType={ plansType }
				onChange={ onChange }
				headersNumber={ calculateNumberOfPlans(selectedOptions, plansList) }
				selectedOptions={ selectedOptions }
				gridClasses={ gridClasses }
			/>
			<div
				id="compare-table-body"
				className="compare-table__body"
				aria-live="polite"
				aria-atomic="true"
			>
				{sectionsData.map((section, i) => {
					// check if next section has subtitle
					const nextSection = sectionsData[i + 1];
					let nextIsSubSection = false;

					if (
						nextSection
						&& !Object.keys(nextSection).includes('sectionTitle')
						&& Object.keys(nextSection).includes('subSectionTitle')
						&& nextSection.subSectionTitle
					) {
						nextIsSubSection = true;
					}

					return (
						<DataSection
							key={ i }
							id={ i }
							section={ {
								...section,
								nextIsSubSection,
								gridClasses,
							} }
						/>
					);
				})}
			</div>
		</>
	);
};

CompareTable.propTypes = {
	data: PropTypes.shape().isRequired,
};

export default CompareTable;

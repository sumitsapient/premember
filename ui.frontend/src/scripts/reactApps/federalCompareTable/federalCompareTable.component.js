import React, {
	useState,
	useCallback,
	useLayoutEffect,
	useEffect,
} from 'react';
import PropTypes from 'prop-types';
import useWindowSize from '../hooks/useWindowSize';
import { isEmpty, isAuthor, normalizeText } from '../../utils/helpers';
import HeaderSection from './headerSection/headerSection.component';
import DataSection from './dataSection/dataSection.component';
import federalHealthPlanCompareTableObject from './plansLists/healthPlanCompareObjectList';
import federalDentalPlanCompareTableObject from './plansLists/dentalPlansCompareObjectList';
import federalVisionPlanCompareTableObject from './plansLists/visionPlansCompareObjectList';
import { getSelectedOptions, getSelectedPlansFederal, calculateNumberOfPlans } from '../../utils/plan';

const FederalCompareTable = ({ planInfoData, labelsData, planType }) => {
	const [labels] = useState(labelsData);
	const [plansList] = useState(planInfoData);
	const [initLoad, setInitLoad] = useState(true);
	const [sectionsData, setSectionsData] = useState([]);
	const [selectedOptions, setSelectedOptions] = useState([]);
	const [plansType] = useState(planType);
	const isMobileBreakpoint = useWindowSize();

	let gridClasses = [
		'o-grid__item-6',
		plansType === 'DENTAL' || plansType === 'VISION' ? 'o-grid__item-4@md' : 'o-grid__item-3@md',
	];

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

	useEffect(() => {
		if (initLoad === true && plansList.length === 2) {
			const plan1 = plansList[0];
			const plan2 = plansList[1];
			const plan1Opts = {
				value: plan1.type,
				planName: plan1.title,
			};
			const plan2Opts = {
				value: plan2.type,
				planName: plan2.title,
			};
			const selectedPlans = [];
			selectedPlans.push(plan1Opts);
			selectedPlans.push(plan2Opts);
			setSelectedOptions(selectedPlans);
			setInitLoad(false);
		}
	});

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
		const plans = getSelectedPlansFederal(options, plansList);
		let sectionsList;
		if (plansType === 'HEALTH') {
			sectionsList = federalHealthPlanCompareTableObject(labels, plans);
			setSectionsData(sectionsList);
		}

		if (plansType === 'DENTAL') {
			sectionsList = federalDentalPlanCompareTableObject(labels, plans);
			setSectionsData(sectionsList);
		}

		if (plansType === 'VISION') {
			sectionsList = federalVisionPlanCompareTableObject(labels, plans);
			setSectionsData(sectionsList);
		}
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
			id,
			value,
			selectedIndex,
			options,
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
		<div role="table">
			<HeaderSection
				plansList={ plansList }
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
				{
					sectionsData.map((section, i) => {
						// check if next section has subtitle
						const nextSection = sectionsData[i + 1];
						let nextIsSubSection = false;

						if (
							nextSection
							&& (
								(
									!Object.keys(nextSection).includes('sectionTitle')
									&& Object.keys(nextSection).includes('subSectionTitle')
									&& nextSection.subSectionTitle
								)
								|| (
									!nextSection.sectionTitle
									&& !nextSection.sectionSubTitle
									&& !nextSection.subSectionTitle
								)
							)
						) {
							nextIsSubSection = true;
						}

						return (
							<DataSection
								key={ i }
								id={ i }
								section={ {
									...section,
									gridClasses,
									nextIsSubSection,
								} }
							/>
						);
					})
				}
			</div>
		</div>
	);
};

FederalCompareTable.propTypes = {
	planInfoData: PropTypes.shape().isRequired,
	labelsData: PropTypes.shape().isRequired,
	planType: PropTypes.shape().isRequired,
};

export default FederalCompareTable;

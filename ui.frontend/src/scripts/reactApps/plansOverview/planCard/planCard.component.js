import React from 'react';
import PropTypes from 'prop-types';

import DOMPurify from 'dompurify';
import PlanCardColumn from './planCardColumn.component';
import { renderChevronIcon } from '../../planCardsList/planCardsList.component';

const propertyToText = {
	annualMaximum: 'Annual Maximum',
	outOfPocketLimit: 'Out-of-pocket Limit',
	deductible: 'Deductible',
	premiumRates: 'Rate',
};

const PlanCard = ({
	planData,
	selected,
}) => {
	const {
		benefits,
		copays,
		description,
		detailsLink,
		planCosts,
		premiumRates,
		title,
		// type,
	} = planData;

	const { planType } = selected;

	/**
	 * Checks plan type against given array of plan types
	 *
	 * @param {Array.<string>|String} plans - Array of plan types or plan type string
	 * @return {Boolean} If given plan types contains current plan type
	 */
	const isPlanType = (plans) => {
		if (Array.isArray(plans)) {
			return plans.includes(planType);
		}

		return plans === planType;
	};

	/**
	 * Convert pay frequency to readable string
	 *
	 * @param {string} payFrequency - pay frequency string
	 */
	const getPayFrequencyString = (payFrequency) => {
		let newString = `${payFrequency[0].toUpperCase()}${payFrequency.substring(1)}`;

		// remove numbers
		newString = newString.replace(/\d+/g, '');

		// remove "category" from string
		newString = newString.replace(/category/gi, '');

		return newString;
	};

	/**
	 * Searches for property in object using case insensitive search
	 *
	 * @param {Object} obj - Object that should be searched
	 * @param {String} prop - Search property
	 *
	 * @return Matching property or null if not found
	 */
	const getCaseInsensitiveProp = (obj, prop) => {
		const propLower = prop.toLowerCase();
		let match = null;

		Object.keys(obj).forEach((key) => {
			if (key.toLowerCase() === propLower) {
				match = key;
			}
		});

		if (match) {
			return obj[match];
		}

		return null;
	};

	/**
	 * Get rates data based on selections
	 */
	const getRatesData = () => {
		const {
			employeeType,
			familySize,
			payFrequency,
		} = selected;

		let prop = 'make selections above';
		let val = '--';
		let rates;

		// family size
		rates = getCaseInsensitiveProp(premiumRates, familySize);

		// employee type (health plans only)
		if (rates && isPlanType('health')) {
			rates = getCaseInsensitiveProp(rates, employeeType);
		}

		// pay frequency
		if (rates) {
			rates = getCaseInsensitiveProp(rates, payFrequency);
		}

		// replace default values, if exists
		if (rates) {
			prop = getPayFrequencyString(payFrequency);
			val = rates;
		}

		return {
			[prop]: val,
		};
	};

	return (
		<div className="c-card c-card--plan c-card--plan-federal">
			<div className="c-card__title">
				{/* Title */}
				<h3 className="unity-type-h3 u-text-serif u-m-b-0">
					{ title }
				</h3>

				{/* Plan Details Button */}
				<a href={ detailsLink } className="c-button c-button--small">
					{ 'Plan Details' }
					{ renderChevronIcon() }
				</a>
			</div>
			<div className="c-card__body">
				<div className="o-grid c-card__plan-main">
					{/* Description */}
					<div
						className={ `o-grid__item-12 ${isPlanType('vision') ? 'o-grid__item-6@lg' : 'o-grid__item-4@lg'}` }
						// eslint-disable-next-line react/no-danger
						dangerouslySetInnerHTML={ {
							__html: DOMPurify.sanitize(description),
						} }
					/>

					{/* Plan Costs (health and dental plans only) */}
					{isPlanType(['health', 'dental'])
						&& Object.keys(planCosts).map((PCTitle, index) => (
							<PlanCardColumn
								title={ propertyToText[PCTitle] }
								items={ planCosts[PCTitle] }
								key={ `plan-costs-${index}` }
							/>
						))}

					{/* Copays (vision plans only) */}
					{isPlanType('vision')
						&& (
							<PlanCardColumn
								title="Copays"
								items={ copays }
							/>
						)
					}

					{/* Premium Rates */}
					{ premiumRates
						&& (
							<PlanCardColumn
								title={ propertyToText.premiumRates }
								items={ getRatesData() }
							/>
						)
					}

					{/* Benefit Icons */}
					{
						isPlanType('medicare')
						&& (
							<PlanCardColumn
								title={ false }
								icons={ benefits.items }
							/>
						)
					}
				</div>
			</div>
		</div>
	);
};

PlanCard.propTypes = {
	planData: PropTypes.shape({
		benefits: PropTypes.objectOf(PropTypes.array),
		copays: PropTypes.object,
		description: PropTypes.string,
		detailsLink: PropTypes.string,
		planCosts: PropTypes.object,
		premiumRates: PropTypes.object,
		title: PropTypes.string,
		type: PropTypes.string,
	}).isRequired,
	selected: PropTypes.objectOf(PropTypes.string).isRequired,
};

export default PlanCard;

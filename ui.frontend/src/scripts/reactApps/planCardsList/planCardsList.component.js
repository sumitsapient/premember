import React, { Component } from 'react';
import PlanSort from './planSort/planSort.component';
import PlanCard from './planCard/planCard.component';
import PlanCompareCard from './planCompareCard/planCompareCard.component';
import sortOptions from './planSort/sortOptions';
import { isEmpty, normalizeText } from '../../utils/helpers';
import { createComparisonURL } from '../../utils/link';

export const renderChevronIcon = () => (
	<span className="o-svg-icon o-svg-icon--lg u-m-l-xxs">
		<svg
			xmlns="http://www.w3.org/2000/svg"
			viewBox="0 0 24 24"
			fill="currentColor"
			focusable="false"
		>
			<path d="M0 0h24v24H0z" fill="none" />
			<path d="M10 6L8.59 7.41 13.17 12l-4.58 4.59L10 18l6-6z" />
		</svg>
	</span>
);

class PlanCardsList extends Component {
	constructor(props) {
		super(props);

		this.plans = JSON.parse(document.querySelector('#plan-cards-list').dataset.plan);
		this.generic = document.querySelector('#plan-cards-list').dataset.generic;
		this.comparelinkText = 'Plan Comparison';

		const defaultSortedBy = this.plans.type ? sortOptions[this.plans.type][0].value : null;

		this.state = {
			options: this.getSortedPlanOptions(this.plans.options, defaultSortedBy),
			copy: this.plans.copy,
			type: this.plans.type,
		};
	}

	getSortedPlanOptions = (options, sortValue) => {
		const list = [...options];
		const { property, direction } = sortOptions[this.plans.type].find(item => item.value === sortValue);

		return list.sort((option1, option2) => {
			const value1 = option1.details.data[property] || 0;
			const value2 = option2.details.data[property] || 0;
			const result = direction * (value1 - value2);

			return result;
		});
	}

	sortPlanOptions = (sortValue) => {
		const { options } = this.state;
		const sortedOptions = this.getSortedPlanOptions(options, sortValue);

		this.setState({
			options: sortedOptions,
		});
	}

	getCompareCardContent = (type) => {
		const linkText = 'Plan Comparison';
		const linkUrl = createComparisonURL(type);
		let title;

		switch (type) {
			case 'HEALTH':
				title = 'Our comparison chart is designed to help you find the health plan that best fits your needs by listing important network plan benefits side-by-side.';
				break;
			case 'VISION':
				title = 'Our comparison chart is designed to help you find the vision plan that best fits your needs by listing important plan benefits side-by-side.';
				break;
			case 'DENTAL':
				title = 'Our comparison chart is designed to help you find the dental plan that best fits your needs by listing important plan benefits side-by-side.';
				break;
			default:
				title = '';
				break;
		}

		return {
			title,
			linkText,
			linkUrl,
		};
	}

	render() {
		const { options, copy, type } = this.state;
		const { title, linkText, linkUrl } = this.getCompareCardContent(type);

		if (isEmpty(options)) {
			return 'No options defined for the plan.';
		}

		if (this.generic === 'true') {
			return (
				<>
					<div id="plan-cards-list">
						{options.map(plan => (
							<PlanCard
								key={ plan.id }
								id={ plan.id }
								planData={ plan.details.data }
								planType={ plan.type }
								copy={ copy.data }
								renderChevronIcon={ renderChevronIcon }
								generic={ this.generic }
							/>
						))}
					</div>
					<PlanCompareCard
						title={ title }
						linkText={ linkText }
						linkUrl={ linkUrl }
						blueBg
					/>
				</>
			);
		}

		return (
			<>
				<div className="plan-cards-header u-d-flex@md u-f-align-center u-f-justify-between">
					<PlanSort
						sortOptions={ sortOptions[type] }
						onSortChange={ this.sortPlanOptions }
					/>
					<a
						href={ createComparisonURL(type) }
						className="c-button c-button--secondary js-track-event"
						data-event-name={ normalizeText(this.comparelinkText) }
						data-event-type="button"
					>
						{ this.comparelinkText }
						{ renderChevronIcon() }
					</a>
				</div>
				<div id="plan-cards-list">
					{options.map(plan => (
						<PlanCard
							key={ plan.id }
							id={ plan.id }
							planData={ plan.details.data }
							planType={ plan.type }
							copy={ copy.data }
							renderChevronIcon={ renderChevronIcon }
						/>
					))}
				</div>
				<PlanCompareCard
					title={ title }
					linkText={ linkText }
					linkUrl={ linkUrl }
					blueBg
				/>
			</>
		);
	}
}

export default PlanCardsList;

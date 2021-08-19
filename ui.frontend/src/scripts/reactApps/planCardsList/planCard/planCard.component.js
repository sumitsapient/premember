import React, { Component } from 'react';
import PropTypes from 'prop-types';

import TempoIconBlock from '../../sharedComponents/tempoIconBlock.component';
import PlanAccordion from '../planAccordion/planAccordion.component';
import PlanCosts from '../planCost/planCost.component';
import { createCostObject, getPlanCardEventName } from '../../../utils/plan';
import { createURL } from '../../../utils/link';

class PlanCard extends Component {
	renderCardTitle() {
		const {
			planData,
			planType,
			id,
			renderChevronIcon,
		} = this.props;

		return (
			<div className="c-card__title">
				<h2 className="unity-type-h3 u-text-serif u-m-b-0">
					{ planData.planName }
				</h2>
				<a
					className="c-button c-button--small"
					href={ createURL(id, planType) }
					data-event-type="plan_card"
					data-event-name={ getPlanCardEventName(planData.planName, 'link', planData.planName) }
				>
					{ 'Plan Details' }
					{ renderChevronIcon() }
				</a>
			</div>
		);
	}

	renderCardBody() {
		const {
			planType,
			planData,
			copy,
			generic,
		} = this.props;
		let costsList;
		let summaryTopic1Copy = '';
		const framesPrivatePracticeCost = planData.summaryTopic5Option1Offered ? planData.framesPrivatePracticeNetwork
			: null;
		const framesRetailChainCost = planData.summaryTopic5Option2Offered ? planData.framesRetailChainNetwork : null;

		switch (planType) {
			case 'HEALTH':
				summaryTopic1Copy = planData['summaryNetworkCoverage.copy'];

				costsList = [
					createCostObject(copy.summaryTopic5, 'Individual', planData.deductibleIndividual, 'Family', planData.deductibleFamily, null),
					createCostObject(copy.summaryTopic6, 'Individual', planData.outOfPocketLimitIndividual, 'Family', planData.outOfPocketLimitFamily, null),
				];
				break;
			case 'VISION':
				summaryTopic1Copy = planData['summaryTopic1Option.copy'];

				costsList = [
					createCostObject(copy.summaryTopic4, 'Exams', planData.eyeExamsNetwork, 'Materials', planData.materialsNetwork, null),
					createCostObject(copy.summaryTopic5, 'Frames - Private Practice', framesPrivatePracticeCost, 'Frames - Retail', framesRetailChainCost, planData.summaryTopic5ExtraText),
				];
				break;
			case 'DENTAL':
				summaryTopic1Copy = planData['summaryNetworkCoverage.copy'];

				costsList = [
					createCostObject('Deductible', 'Individual', planData.deductibleIndividualInNetwork, 'Family', planData.deductibleFamilyInNetwork, null),
					createCostObject(copy.summaryTopic3, 'Individual', planData.annualMaximumIndividualInNetwork, 'Family', planData.annualMaximumFamilyInNetwork, null),
				];
				break;
			default:
				costsList = [];
				break;
		}

		if (generic === 'true') {
			return (
				<div className="c-card__body">
					<div className="c-card__plan-main c-card__plan-main--generic o-grid">
						<div className="o-grid__item-5 u-d-none@sm-only">
							<TempoIconBlock
								title={ copy.summaryTopic1 }
								text={ summaryTopic1Copy }
								svgIcon={ copy.summaryTopic1IconContent }
								twoColor
							/>
						</div>
					</div>
				</div>
			);
		}

		return (
			<div className="c-card__body">
				<div className="c-card__plan-main o-grid">
					<div className="o-grid__item-5 o-grid__item@sm u-d-none@sm-only">
						<TempoIconBlock
							title={ copy.summaryTopic1 }
							text={ summaryTopic1Copy }
							svgIcon={ copy.summaryTopic1IconContent }
							twoColor
						/>
					</div>
					{ costsList.map((costs, index) => (
						<div className="o-grid__item-12 o-grid__item@sm" key={ index }>
							<PlanCosts costs={ costs } />
						</div>
					)) }
				</div>
			</div>
		);
	}

	renderCardAccordion() {
		const { planData, copy, planType } = this.props;
		return <PlanAccordion planData={ planData } planType={ planType } copy={ copy } />;
	}

	render() {
		const { planType } = this.props;

		return (
			<div className={ `c-card c-card--plan c-card--${planType.toLowerCase()}` }>
				{ this.renderCardTitle() }
				{ this.renderCardBody() }
				{ this.renderCardAccordion() }
			</div>
		);
	}
}

PlanCard.propTypes = {
	planData: PropTypes.shape({
		planId: PropTypes.string,
		planName: PropTypes.string,
	}).isRequired,
	id: PropTypes.string.isRequired,
	planType: PropTypes.string.isRequired,
	copy: PropTypes.shape().isRequired,
	generic: PropTypes.string,
	renderChevronIcon: PropTypes.func.isRequired,
};

PlanCard.defaultProps = {
	generic: null,
};

export default PlanCard;

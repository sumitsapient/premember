import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { ReactFromHtml } from '@bitjourney/react-from-html';

import TempoIconBlock from '../../sharedComponents/tempoIconBlock.component';
import Button from '../../sharedComponents/button.component';
import { addClass, removeClass } from '../../../utils/dom';
import constants from '../../../utils/constants';
import { getHideElementHeight, expandAnimation, collapseAnimation } from '../../../utils/animations';
import { createIconBlockObject, getPlanCardEventName, pluralizeText } from '../../../utils/plan';

export default class PlanAccordion extends Component {
	constructor(props) {
		super(props);

		this.state = {
			isToggleOn: false,
			collapseText: 'Close Quick Look',
			expandText: 'Open Quick Look',
		};

		this.reactFromHtml = new ReactFromHtml();
		this.accordionLink = React.createRef();
		this.accordionContent = React.createRef();
		this.toggleAccordionOnClick = this.toggleAccordionOnClick.bind(this);
		this.toggleAccordionOnKeyDown = this.toggleAccordionOnKeyDown.bind(this);
	}

	getMonthsFrequencyText(months) {
		return `Once every ${pluralizeText(months, 'month')}`;
	}

	expandAccordion() {
		const contentHeight = getHideElementHeight(this.accordionContent.current);

		addClass(this.accordionLink.current, 'active');
		addClass(this.accordionContent.current, 'show');

		expandAnimation(this.accordionContent.current, contentHeight, constants.transitionTime);

		this.accordionLink.current.setAttribute('aria-expanded', 'true');
		this.accordionContent.current.setAttribute('aria-hidden', 'false');
		this.accordionContent.current.focus();
	}

	collapseAccordion() {
		collapseAnimation(this.accordionContent.current, constants.transitionTime);

		removeClass(this.accordionLink.current, 'active');
		this.accordionLink.current.setAttribute('aria-expanded', 'false');
		this.accordionContent.current.setAttribute('aria-hidden', 'true');
	}

	toggleAccordionOnClick(evt) {
		evt.preventDefault();

		this.setState(state => ({
			isToggleOn: !state.isToggleOn,
		}), () => {
			const { isToggleOn } = this.state;
			if (isToggleOn) {
				this.expandAccordion();
			} else {
				this.collapseAccordion();
			}
		});
	}

	toggleAccordionOnKeyDown(evt) {
		if (evt.keyCode !== constants.keyCodes.enterKey) {
			return;
		}

		evt.preventDefault();

		this.setState(state => ({
			isToggleOn: !state.isToggleOn,
		}), () => {
			const { isToggleOn } = this.state;
			if (isToggleOn) {
				this.expandAccordion();
			} else {
				this.collapseAccordion();
			}
		});
	}

	renderExpandIcon() {
		return (
			<span className="o-svg-icon o-svg-icon--expand o-svg-icon--xl u-m-l-xxs">
				<svg
					xmlns="http://www.w3.org/2000/svg"
					viewBox="0 0 24 24"
					fill="currentColor"
					focusable="false"
				>
					<path d="M0 0h24v24H0z" fill="none" />
					<path d="M16.59 8.59L12 13.17 7.41 8.59 6 10l6 6 6-6z" />
				</svg>
			</span>
		);
	}

	renderPlanSummary() {
		const { planData, copy, planType } = this.props;
		let iconBlockList;
		const savingOptionsList = [
			planData['summaryTopic4Option1.copy'],
			planData['summaryTopic4Option2.copy'],
			planData['summaryTopic4Option3.copy'],
			planData['summaryTopic4Option4.copy'],
		];

		switch (planType) {
			case 'HEALTH':
				iconBlockList = [
					createIconBlockObject(copy.summaryTopic1, planData['summaryNetworkCoverage.copy'], copy.summaryTopic1IconContent),
					createIconBlockObject(copy.summaryTopic2, planData['primaryCarePhysician.copy'], copy.summaryTopic2IconContent),
					createIconBlockObject(copy.summaryTopic3, planData['summaryCopays.copy'], copy.summaryTopic3IconContent),
					createIconBlockObject(copy.summaryTopic4, savingOptionsList, copy.summaryTopic4IconContent),
				];
				break;
			case 'VISION':
				iconBlockList = [
					createIconBlockObject(copy.summaryTopic1, planData['summaryTopic1Option.copy'], copy.summaryTopic1IconContent),
					createIconBlockObject('Comprehensive Eye Exam', this.getMonthsFrequencyText(planData.eyeExamsFrequency), copy.summaryTopic2IconContent),
				];

				if (!planData.hideDiscountsAvailable) {
					iconBlockList.push(createIconBlockObject(copy.summaryTopic3, planData['summaryTopic3Option.copy'], copy.summaryTopic3IconContent));
				}

				if (!planData.suppressSummaryTopic7) {
					iconBlockList.push(createIconBlockObject(copy.summaryTopic7, planData['summaryTopic7Option.copy'], copy.summaryTopic7IconContent));
				}

				break;
			case 'DENTAL':
				iconBlockList = [
					createIconBlockObject(copy.summaryTopic1, planData['summaryNetworkCoverage.copy'], copy.summaryTopic1IconContent),
					createIconBlockObject(copy.summaryTopic4, planData['summaryCopays.copy'], copy.summaryTopic4IconContent),
					createIconBlockObject(copy.summaryTopic2, planData['summaryPreventiveCare.copy'], copy.summaryTopic2IconContent),
					createIconBlockObject(copy.summaryTopic6, planData['summaryReferrals.copy'], copy.summaryTopic5IconContent),
				];
				break;
			default:
				iconBlockList = [];
				break;
		}

		return iconBlockList.map((option, i) => (
			<TempoIconBlock
				key={ i }
				title={ option.title }
				text={ option.text }
				svgIcon={ option.svgIcon }
			/>
		));
	}

	renderDescription() {
		const { planData, copy } = this.props;
		const showPrescriptionSearch = !planData.suppressPrescriptionCoverage && copy.customPrescriptionSearchLink;

		return (
			<React.Fragment>
				{ this.reactFromHtml.parseToFragment(planData.planDescription) }
				{ planData.providerSearchLink
					&& (
						<>
							<h3 className="unity-type-h6 u-color-black u-m-b-0">
								{ copy.summaryCardProviderSearch || copy.summaryProviderSearch }
							</h3>
							<Button
								url={ planData.providerSearchLink }
								text={ copy.summaryProviderSearchLinkText }
								eventType="plan_card"
								eventName={ getPlanCardEventName(planData.planName, 'link', copy.summaryProviderSearchLinkText) }
							/>
						</>
					)
				}
				{ showPrescriptionSearch
					&& (
						<>
							<h3 className="unity-type-h6 u-color-black u-m-b-0">
								{ copy.summaryPrescriptionSearch }
							</h3>
							<Button
								url={ copy.customPrescriptionSearchLink }
								text={ copy.summaryPrescriptionSearchLinkText }
								showIcon={ false }
								eventType="plan_card"
								eventName={ getPlanCardEventName(planData.planName, 'link', copy.summaryPrescriptionSearchLinkText) }
							/>
						</>
					)
				}
			</React.Fragment>
		);
	}

	render() {
		const { planData } = this.props;
		const { isToggleOn, collapseText, expandText } = this.state;
		const toggleButtonText = isToggleOn ? collapseText : expandText;

		return (
			<>
				<div
					aria-hidden="true"
					aria-labelledby={ `accordion-${planData.planId}-button` }
					className="c-card__collapse collapse"
					id={ `accordion-${planData.planId}` }
					ref={ this.accordionContent }
					role="region"
					// eslint-disable-next-line jsx-a11y/no-noninteractive-tabindex
					tabIndex="0"
				>
					<div className="c-card__body">
						<div className="o-grid">
							<div className="o-grid__item-12 o-grid__item-6@md o-grid__item-5@lg">
								{ this.renderPlanSummary() }
							</div>
							<div className="o-grid__item-12 o-grid__item-6@md o-grid__item-7@lg">
								{ this.renderDescription() }
							</div>
						</div>
					</div>
				</div>
				<div className="c-card__footer">
					<button
						id={ `accordion-${planData.planId}-button` }
						className="c-button c-button--naked"
						aria-expanded="false"
						aria-controls={ `accordion-${planData.planId}` }
						data-event-type="plan_card"
						data-event-name={ getPlanCardEventName(planData.planName, 'accordion', toggleButtonText) }
						ref={ this.accordionLink }
						onClick={ this.toggleAccordionOnClick }
						onKeyDown={ this.toggleAccordionOnKeyDown }
						type="button"
					>
						{ toggleButtonText }
						{ this.renderExpandIcon() }
					</button>
				</div>
			</>
		);
	}
}

PlanAccordion.propTypes = {
	planData: PropTypes.shape({
		planId: PropTypes.string,
		planDescription: PropTypes.string,
		providerSearchLink: PropTypes.string,
		prescriptionLinks: PropTypes.string,
	}).isRequired,
	copy: PropTypes.shape().isRequired,
	planType: PropTypes.string.isRequired,
};

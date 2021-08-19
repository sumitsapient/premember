import React, { Component } from 'react';
import PropTypes from 'prop-types';

import FilterBar from './filterBar.component';
import { getFormattedPrice } from '../../utils/plan';

function Heading(props) {
	const {
		className,
		sizeStyle,
		title,
		typeSize,
	} = props;
	const HeadingTag = `${typeSize || 'h3'}`;
	const classNames = (sizeStyle ? `${className} unity-type-${sizeStyle}` : className);

	return (<HeadingTag className={ classNames }>{title}</HeadingTag>);
}

Heading.propTypes = {
	className: PropTypes.string,
	sizeStyle: PropTypes.string,
	title: PropTypes.string,
	typeSize: PropTypes.string,
};

Heading.defaultProps = {
	className: null,
	sizeStyle: null,
	title: null,
	typeSize: null,
};

class RatesTable extends Component {
	constructor(props) {
		super(props);

		let premiumRatesProp = 'premiumRates';
		let limitProp = 'outOfPocketLimit';

		if (props.type === 'dental') {
			limitProp = 'annualMaximum';
		}

		if (props.type === 'vision') {
			premiumRatesProp = 'premiumRates';
			limitProp = null;
		}

		this.state = {
			activeCode: '',
			premiumRate: '',
			payRate: '',
			labelMapping: {
				biweekly: 'Biweekly',
				monthly: 'Monthly',
				biweeklyCategory1: 'Biweekly',
				biweeklyCategory2: 'Biweekly',
			},
			premiumRatesProp,
			limitProp,
		};
	}

	componentDidMount() {
		const { type } = this.props;

		if (type === 'health') {
			this.getRate('selfOnly', 'biweekly', 'nonPostalEmployee');
		} else if (type === 'dental' || type === 'vision') {
			this.getRate('selfOnly', 'biWeekly');
		}
	}

	/**
	 * Returns matching property from labelMapping object (case-insensitive)
	 *
	 * @param {String} label - Label that should be found and returned from label mapping object
	 *
	 * @returns {String} Label mapping value
	 */
	getLabel = (label) => {
		const { labelMapping } = this.state;
		let match;

		if (Object.prototype.hasOwnProperty.call(labelMapping, label)) {
			return labelMapping[label];
		}

		Object.keys(labelMapping).forEach((mapping) => {
			if (mapping.toLowerCase() === label.toLowerCase()) {
				match = labelMapping[mapping];
			}
		});

		return match;
	}

	getRate = (planSize, costPayrate, postal) => {
		const { data, type } = this.props;
		const { premiumRatesProp } = this.state;
		const planObject = data[premiumRatesProp][planSize];
		let cost;

		if (Object.prototype.hasOwnProperty.call(planObject, 'enrollmentCode')) {
			this.setState({
				activeCode: planObject.enrollmentCode,
			});
		}

		// ignore postal type if dental
		if (type === 'dental' || type === 'vision') {
			if (Object.prototype.hasOwnProperty.call(planObject, costPayrate)) {
				cost = planObject[costPayrate].toString();
			} else {
				// do case-insensitive search
				Object.keys(planObject).forEach((prop) => {
					if (prop.toLowerCase() === costPayrate.toLowerCase()) {
						cost = planObject[prop].toString();
					}
				});
			}
		} else {
			const employeeTypeObject = planObject[postal];
			cost = employeeTypeObject[costPayrate].toString();
		}

		this.setState({
			premiumRate: cost,
			payRate: costPayrate,
		});
	}

	render() {
		const {
			data,
			type,
		} = this.props;
		const {
			activeCode,
			premiumRate,
			payRate,
			limitProp,
		} = this.state;
		const formattedPremiumRate = getFormattedPrice(premiumRate);
		return (
			<div className="c-rates-table o-container">
				<Heading
					typeSize={ data.headingConfig.titleTypeSize }
					sizeStyle={ data.headingConfig.titleSizeStyle }
					className="u-text-center"
					title={ data.title }
				/>

				<FilterBar
					updateRate={ this.getRate }
					type={ type }
				/>

				{ !!activeCode && (
					<p className="u-text-center u-m-b-lg">
						<span>
							<strong>Enrollment Code:</strong>
							{`${activeCode}`}
						</span>
					</p>
				) }

				<div className="o-grid rates-value-container u-f-justify-center u-m-t">
					{type === 'health'
						&& (
							<div className="o-grid__item o-grid__item-12 o-grid__item-4@md c-rates-table__deductible">
								<Heading
									typeSize={ data.headingConfig.subtitleTypeSize }
									sizeStyle={ data.headingConfig.subtitleSizeStyle }
									className="u-color-blue u-text-center"
									title="Deductible"
								/>
								<div className="u-d-flex">
									<div className="o-grid__item o-grid__item-6@md u-text-center">
										<p className="unity-type-h3 u-m-b-0 u-text-serif">{`$${data.planCosts.deductible.individual}`}</p>
										<span>Individual</span>
									</div>
									<div className="o-grid__item o-grid__item-6@md u-text-center">
										<p className="unity-type-h3 u-m-b-0 u-text-serif">{`$${data.planCosts.deductible.family}`}</p>
										<span>Family</span>
									</div>
								</div>
							</div>
						)
					}

					{ type === 'health' && (
						<div className="o-grid__item o-grid__item-12 o-grid__item-4@md c-rates-table__outofpocket">
							<Heading
								typeSize={ data.headingConfig.subtitleTypeSize }
								sizeStyle={ data.headingConfig.subtitleSizeStyle }
								className="u-color-blue u-text-center"
								title="Out-of-pocket Limit"
							/>
							<div className="u-d-flex">
								<div className="o-grid__item o-grid__item-6@md u-text-center">
									<p className="unity-type-h3 u-m-b-0 u-text-serif">{`$${data.planCosts[limitProp].individual}`}</p>
									<span>Individual</span>
								</div>
								<div className="o-grid__item o-grid__item-6@md u-text-center">
									<p className="unity-type-h3 u-m-b-0 u-text-serif">{`$${data.planCosts[limitProp].family}`}</p>
									<span>Family</span>
								</div>
							</div>
						</div>
					) }

					{ type === 'dental' && (
						<div className="o-grid__item o-grid__item-12 o-grid__item-4@md c-rates-table__deductible">
							<Heading
								typeSize={ data.headingConfig.subtitleTypeSize }
								sizeStyle={ data.headingConfig.subtitleSizeStyle }
								className="u-color-blue u-text-center"
								title="Deductible"
							/>
							<div className="u-d-flex">
								<div className="o-grid__item o-grid__item-6@md u-text-center">
									<p className="unity-type-h3 u-m-b-0 u-text-serif">{`$${data.planCosts.deductible.individual}`}</p>
									<span>Individual</span>
								</div>
								<div className="o-grid__item o-grid__item-6@md u-text-center">
									<p className="unity-type-h3 u-m-b-0 u-text-serif">{`$${data.planCosts.deductible.family}`}</p>
									<span>Family</span>
								</div>
							</div>
						</div>
					) }

					{ type === 'dental' && (
						<div className="o-grid__item o-grid__item-12 o-grid__item-4@md c-rates-table__outofpocket">
							<Heading
								typeSize={ data.headingConfig.subtitleTypeSize }
								sizeStyle={ data.headingConfig.subtitleSizeStyle }
								className="u-color-blue u-text-center"
								title="Annual Maximum"
							/>
							<div className="u-d-flex">
								<div className="o-grid__item o-grid__item-6@md u-text-center">
									<p className="unity-type-h3 u-m-b-0 u-text-serif">{`${data.planCosts[limitProp].individual}`}</p>
									<span>Individual</span>
								</div>
								<div className="o-grid__item o-grid__item-6@md u-text-center">
									<p className="unity-type-h3 u-m-b-0 u-text-serif">{`${data.planCosts[limitProp].family}`}</p>
									<span>Family</span>
								</div>
							</div>
						</div>
					) }

					{type === 'vision' && (
						<div className="o-grid__item o-grid__item-12 o-grid__item-4@md c-rates-table__outofpocket u-b-l-0">
							<Heading
								typeSize={ data.headingConfig.subtitleTypeSize }
								sizeStyle={ data.headingConfig.subtitleSizeStyle }
								className="u-color-blue u-text-center"
								title="Copays"
							/>
							<div className="u-d-flex">
								<div className="o-grid__item o-grid__item-6@md u-text-center">
									<p className="unity-type-h3 u-m-b-0 u-text-serif">{`$${data.copays.annualExams}`}</p>
									<span>Individual</span>
								</div>
								<div className="o-grid__item o-grid__item-6@md u-text-center">
									<p className="unity-type-h3 u-m-b-0 u-text-serif">{`$${data.copays.eyeGlasses}`}</p>
									<span>Family</span>
								</div>
							</div>
						</div>
					) }

					<div className="o-grid__item o-grid__item-12 o-grid__item-4@md c-rates-table__premium">
						<Heading
							typeSize={ data.headingConfig.subtitleTypeSize }
							sizeStyle={ data.headingConfig.subtitleSizeStyle }
							className="u-color-blue u-text-center"
							title="Rate"
						/>
						<div className="u-d-flex">
							<div className="o-grid__item u-text-center">
								<p className="unity-type-h3 u-m-b-0 u-text-serif">{`$${formattedPremiumRate}`}</p>
								<span>{ this.getLabel(payRate) }</span>
							</div>
						</div>
					</div>
				</div>
			</div>
		);
	}
}

RatesTable.propTypes = {
	data: PropTypes.shape().isRequired,
	type: PropTypes.oneOf([
		'health',
		'dental',
		'vision',
	]).isRequired,
};

export default RatesTable;

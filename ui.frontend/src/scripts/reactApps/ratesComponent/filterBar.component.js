/* eslint-disable jsx-a11y/label-has-associated-control */
import React, { Component } from 'react';
import PropTypes from 'prop-types';

class FilterBar extends Component {
	constructor(props) {
		super(props);

		this.state = {
			employeeType: 'nonPostalEmployee',
			planSize: 'selfOnly',
			payFrequency: 'biweekly',
			payFrequencyOptions: {
				postalEmployee: [
					{
						value: 'biweeklyCategory1',
						label: 'Biweekly Category 1',
					},
					{
						value: 'biweeklyCategory2',
						label: 'Biweekly Category 2',
					},
				],
				nonPostalEmployee: [
					{
						value: 'biweekly',
						label: 'Biweekly',
					},
					{
						value: 'monthly',
						label: 'Monthly',
					},
				],
			},
			selectedPayFrequencyOptions: [
				{
					value: 'biweekly',
					label: 'Biweekly',
				},
				{
					value: 'monthly',
					label: 'Monthly',
				},
			],
		};
	}

	handleChange = (event) => {
		const { target } = event;
		const { selectedIndex } = target;
		const { value } = target.options[selectedIndex];

		if (target.name === 'employeeType') {
			if (value === 'postalEmployee') {
				this.setState(prevState => (
					{
						[target.name]: value,
						payFrequency: 'biweeklyCategory1',
						selectedPayFrequencyOptions: prevState.payFrequencyOptions.postalEmployee,
					}
				));
			} else if (value === 'nonPostalEmployee') {
				this.setState(prevState => (
					{
						[target.name]: value,
						payFrequency: 'biweekly',
						selectedPayFrequencyOptions: prevState.payFrequencyOptions.nonPostalEmployee,
					}
				));
			}
		}

		this.setState({
			[target.name]: value,
		}, () => this.updateRate());
	};

	updateRate() {
		const {
			employeeType,
			payFrequency,
			planSize,
		} = this.state;
		const { updateRate } = this.props;
		updateRate(planSize, payFrequency, employeeType);
	}

	render() {
		const { type } = this.props;
		const { selectedPayFrequencyOptions } = this.state;

		return (
			<div className="rate-comparison-filter u-d-flex@md u-f-justify-center u-m-t u-m-b-sm">
				<div role="group" className="u-d-flex u-f-align-center" aria-labelledby="select-group-legend">
					<div className="o-grid u-f-justify-center">
						<div className="o-grid__item-12 u-m-b-xxs">
							<p id="select-group-legend" className="u-text-bold u-text-center u-w-100% u-m-b-0">Adjust By:</p>
						</div>
						{ (type !== 'dental' && type !== 'vision') && (
							<div className="o-grid__item-auto">
								<label
									className="u-color-gray-dark"
									htmlFor="employeeType"
								>
									<small>Are you a postal employee</small>
								</label>
								<select
									name="employeeType"
									id="employeeType"
									className="c-select"
									onChange={ (event) => { this.handleChange(event); } }
								>
									<option value="nonPostalEmployee">Non-Postal Employee</option>
									<option value="postalEmployee">Postal Employee</option>
								</select>
							</div>
						) }
						<div className="o-grid__item-auto">
							<label
								className="u-color-gray-dark"
								htmlFor="planSize"
							>
								<small>Who is this for</small>
							</label>
							<select
								name="planSize"
								id="planSize"
								className="c-select"
								onChange={ (event) => { this.handleChange(event); } }
							>
								<option value="selfOnly">Self</option>
								<option value="selfAndFamily">Self And Family</option>
								<option value="selfPlusOne">Self Plus One</option>
							</select>
						</div>
						<div className="o-grid__item-auto">
							<label
								className="u-color-gray-dark"
								htmlFor="payFrequency"
							>
								<small>What is pay schedule?</small>
							</label>
							<select
								name="payFrequency"
								id="payFrequency"
								className="c-select"
								onChange={ (event) => { this.handleChange(event); } }
							>
								{
									selectedPayFrequencyOptions.map((item, index) => (
										<option
											key={ index }
											value={ item.value }>
											{ item.label }
										</option>
									))
								}
							</select>
						</div>
					</div>
				</div>
			</div>
		);
	}
}

FilterBar.propTypes = {
	updateRate: PropTypes.func.isRequired,
	type: PropTypes.oneOf([
		'health',
		'dental',
		'vision',
	]).isRequired,
};

export default FilterBar;

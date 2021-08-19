import React, {
	useEffect,
	useRef,
	useState,
} from 'react';
import PropTypes from 'prop-types';

import { sentenceCase } from '../../utils/helpers';

const HealthPlansEnrollment = ({ data }) => {
	const [code, setCode] = useState('');
	const planSelect = useRef(null);
	const coverageSelect = useRef(null);
	const plans = {};
	const coverages = [];

	const updateCode = () => {
		const planVal = planSelect.current.value;
		const coverageVal = coverageSelect.current.value;

		if (Object.prototype.hasOwnProperty.call(plans, planVal)) {
			if (Object.prototype.hasOwnProperty.call(plans[planVal].coverages, coverageVal)) {
				setCode(plans[planVal].coverages[coverageVal]);
			}
		}
	};

	// create plan object
	data.forEach((plan) => {
		const {
			premiumRates,
			title,
			type,
		} = plan;
		const cov = {};

		if (premiumRates) {
			Object.keys(premiumRates).forEach((rateType) => {
				if (rateType !== 'planType') {
					const val = premiumRates[rateType];
					const { enrollmentCode } = val;

					// add to coverages, if it doesn't exist
					if (!coverages.includes(rateType)) {
						coverages.push(rateType);
					}

					if (enrollmentCode) {
						cov[rateType] = enrollmentCode;
					}
				}
			});
		}

		plans[type] = {
			title,
			coverages: cov,
		};
	});

	useEffect(() => {
		updateCode();
	}, []);

	return (
		<div className="o-grid">
			{/* Plan */}
			<div className="o-grid__item-12 o-grid__item-auto@md u-m-b">
				{/* eslint-disable-next-line jsx-a11y/label-has-associated-control */}
				<label htmlFor="select-enrollment-code-plan" className="u-visually-hidden">Health Plan</label>
				<select
					ref={ planSelect }
					className="c-select"
					name="plan"
					id="select-enrollment-code-plan"
					onChange={ updateCode }
				>
					{
						Object.keys(plans).map(name => (
							<option value={ name } key={ name }>{ plans[name].title }</option>
						))
					}
				</select>
			</div>

			{/*	Coverage type */}
			<div className="o-grid__item-12 o-grid__item-auto@md u-m-b">
				{/* eslint-disable-next-line jsx-a11y/label-has-associated-control */}
				<label htmlFor="select-enrollment-code-coverage" className="u-visually-hidden">Who do you want covered?</label>
				<select
					ref={ coverageSelect }
					className="c-select"
					name="coverage"
					id="select-enrollment-code-coverage"
					onChange={ updateCode }
				>
					{
						coverages.map(coverage => (
							<option value={ coverage } key={ coverage }>{ sentenceCase(coverage, 'camel') }</option>
						))
					}
				</select>
			</div>

			<div
				className="o-grid__item-12"
				aria-live="polite"
			>
				<span className="u-text-bold">Enrollment Code:</span>
				<span className="u-p-l-sm">{ code }</span>
			</div>
		</div>
	);
};

HealthPlansEnrollment.propTypes = {
	data: PropTypes.arrayOf(PropTypes.object).isRequired,
};

export default HealthPlansEnrollment;

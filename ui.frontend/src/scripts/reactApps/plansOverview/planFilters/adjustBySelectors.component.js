import React from 'react';
import PropTypes from 'prop-types';

const AdjustBySelectors = ({
	checkConditionalStatement,
	filterFields,
	selected,
	updateSelected,
}) => {
	const dependencyMap = {};

	// Create dependency map
	filterFields.forEach((field) => {
		const { displayCondition } = field;

		if (displayCondition) {
			const parentProp = displayCondition.split('=')[0].trim();

			if (!Object.prototype.hasOwnProperty.call(dependencyMap, parentProp)) {
				dependencyMap[parentProp] = [];
			}

			if (!dependencyMap[parentProp].includes(field.name)) {
				dependencyMap[parentProp].push(field.name);
			}
		}
	});

	/**
	 * Update select value on change
	 *
	 * @param {Event} evt - Event
	 */
	const updateSelectVal = (evt) => {
		const el = evt.target;
		const name = el.getAttribute('name');
		const newVals = {
			[el.id]: el.value,
		};

		// check if any dependent values need to be updated
		if (Object.prototype.hasOwnProperty.call(dependencyMap, name)) {
			dependencyMap[name].forEach((dependency) => {
				// find matching dependency
				const obj = filterFields.find((field) => {
					const { displayCondition } = field;

					if (field.name === dependency && displayCondition) {
						if (displayCondition.match(new RegExp(`${name}.*${el.value}`, 'g'))) {
							return true;
						}
					}

					return false;
				});

				if (obj) {
					newVals[obj.name] = obj.options[0].value;
				}
			});
		}

		// update values
		updateSelected(newVals);
	};

	return (
		<fieldset className="o-grid u-f-align-center u-b-0 u-p-0 u-m-t-lg">
			<div className="o-grid__item-12 u-m-b-xs">
				<legend className="u-d-block u-text-bold u-m-0 u-p-0">Adjust by:</legend>
			</div>

			{filterFields.map((select, index) => {
				const { name, displayCondition, planType } = select;

				// check display condition
				if (displayCondition && !checkConditionalStatement(displayCondition)) {
					return null;
				}

				// check if plan type matches
				if (planType && !planType.includes(selected.planType)) {
					return null;
				}

				return (
					<div className="o-grid__item-12 o-grid__item-4@md o-grid__item-auto@lg u-m-b-xs" key={ `ab-select-${index}` }>
						<label className="u-color-gray-dark" htmlFor={ name }>
							<small>{select.label}</small>
						</label>
						<select
							className="c-select"
							id={ name }
							name={ name }
							value={ selected[name] }
							onChange={ updateSelectVal }
						>
							{select.options.map((option, index2) => (
								<option
									key={ `ab-select-${index}-option-${index2}` }
									value={ option.value }
								>
									{ option.text }
								</option>
							))}
						</select>
					</div>
				);
			})}
		</fieldset>
	);
};

AdjustBySelectors.propTypes = {
	checkConditionalStatement: PropTypes.func.isRequired,
	filterFields: PropTypes.arrayOf(PropTypes.object).isRequired,
	selected: PropTypes.objectOf(PropTypes.string).isRequired,
	updateSelected: PropTypes.func.isRequired,
};

export default React.memo(AdjustBySelectors);

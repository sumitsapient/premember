import React, { useState, useEffect, useRef } from 'react';
import PropTypes from 'prop-types';
import { normalizeText } from '../../../utils/helpers';

const PlanSort = ({ sortOptions, onSortChange }) => {
	const [selectedLabel, setSelectedLabel] = useState(sortOptions[0].label);
	const selectInput = useRef(null);

	useEffect(() => {
		selectInput.current.selectedIndex = 0;
	}, []);

	const handleChange = (event) => {
		const { target } = event;
		const { selectedIndex } = target;
		const { label, value } = target.options[selectedIndex];

		setSelectedLabel(label);
		onSortChange(value);
	};

	return (
		<div className="plan-sort u-d-flex@md">
			<label className="plan-sort__label u-d-block u-m-b-0 unity-type-small u-text-uppercase u-text-bold" htmlFor="plan-sort-select">
				{ 'Sort by' }
			</label>
			<div className="plan-sort__field u-d-inline-block">
				<select
					id="plan-sort-select"
					className="plan-sort__select u-text-bold u-color-blue"
					aria-label="Sort by (changes order of elements in the plan options list)"
					aria-controls="plan-cards-list"
					data-event-type="plan_sort"
					data-event-name={ normalizeText(selectedLabel) }
					ref={ selectInput }
					onChange={ handleChange }
				>
					{
						sortOptions.map(option => (
							<option key={ option.value } value={ option.value }>
								{ option.label }
							</option>
						))
					}
				</select>
				<span className="plan-sort__dropdown-icon u-d-block u-text-center u-bg-blue" />
			</div>
		</div>
	);
};

PlanSort.propTypes = {
	sortOptions: PropTypes.arrayOf(
		PropTypes.shape({
			label: PropTypes.string.isRequired,
			value: PropTypes.string.isRequired,
			property: PropTypes.string.isRequired,
			direction: PropTypes.number.isRequired,
		}),
	).isRequired,
	onSortChange: PropTypes.func.isRequired,
};

export default PlanSort;

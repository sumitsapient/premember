import React from 'react';
import PropTypes from 'prop-types';

const PlanSort = ({
	onSortChange,
	sortOptions,
	sortValue,
	updateSelected,
}) => {
	/**
	 * Handle select value change
	 *
	 * @param {Event} event - Change event
	 */
	const handleChange = (event) => {
		const { target } = event;
		const { selectedIndex } = target;
		const { value } = target.options[selectedIndex];

		updateSelected(value, 'sort');
		onSortChange(value);
	};

	return (
		<div className="o-grid u-f-align-center u-m-b">
			<div className="o-grid__item-auto">
				<label htmlFor="sort-select">
					{'SORT BY'}
				</label>
			</div>
			<div className="o-grid__item-auto">
				<select
					className="c-select u-b-0 u-text-bold u-color-blue"
					id="sort-select"
					value={ sortValue }
					onChange={ handleChange }
				>
					{sortOptions.map(option => (
						<option
							key={ option.value }
							value={ option.value }
						>
							{ option.label }
						</option>
					))}
				</select>
			</div>
		</div>
	);
};

PlanSort.propTypes = {
	onSortChange: PropTypes.func.isRequired,
	sortOptions: PropTypes.arrayOf(PropTypes.object).isRequired,
	sortValue: PropTypes.string.isRequired,
	updateSelected: PropTypes.func.isRequired,
};

export default PlanSort;

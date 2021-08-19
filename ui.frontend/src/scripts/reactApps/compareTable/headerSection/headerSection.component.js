import React, {
	useState,
	useEffect,
	useLayoutEffect,
} from 'react';
import PropTypes from 'prop-types';
import { isEmpty, filterArray } from '../../../utils/helpers';
import { getSelectedOptions, getCompareTableEventName } from '../../../utils/plan';
import { createURL } from '../../../utils/link';

const HeaderSection = ({
	plansList,
	onChange,
	headersNumber,
	selectedOptions,
	gridClasses,
}) => {
	const [availableOptions, setAvailableOptions] = useState([]);
	const [refArray] = useState([]);

	useEffect(() => {
		refArray.forEach((item) => {
			const elem = item;
			elem.selectedIndex = 0;
		});
	}, [refArray]);

	useLayoutEffect(() => {
		const availablePlans = [];

		plansList.forEach((item) => {
			availablePlans.push({ id: item.id, name: item.details.data.planName });
		});

		setAvailableOptions(oldValue => [...oldValue, ...availablePlans]);
	}, []);

	const renderOptions = (itemSelectedOnField) => {
		const options = availableOptions.map(obj => obj);

		if (!isEmpty(selectedOptions)) {
			const selected = selectedOptions.map(obj => obj.value);
			filterArray(options, obj => selected.indexOf(obj.id) !== -1 && itemSelectedOnField !== obj.id);
		}

		return options.map((plan, id) => (
			<option value={ plan.id } key={ id }>
				{ plan.name }
			</option>
		));
	};

	const renderSelectedOptionsTiles = () => {
		const options = getSelectedOptions(selectedOptions, headersNumber);
		return options.map((option, i) => (
			<div
				key={ i }
				role="columnheader"
				className={ gridClasses }
			>
				{/* Plan name */}
				<span
					aria-hidden="true"
					className="u-visually-hidden"
					id={ `header-${i}` }
				>
					{ option.planName ? option.planName : ''}
				</span>
				<label htmlFor={ i }>
					{ 'Add a plan' }
				</label>
				<select
					id={ i }
					className="c-select"
					aria-controls="compare-table-body"
					ref={ (ref) => {
						refArray[i] = ref;
					} }
					data-event-type="compare"
					data-event-name={ !option.planName ? '' : getCompareTableEventName(option.planName) }
					onChange={ onChange }
					value={ !option.value ? 'default' : option.value }
				>
					<option value="default">Add another plan</option>
					{ renderOptions(option.value) }
				</select>
				{/* View plan details */}
				{
					option.value ? (
						<a
							href={ createURL(option.value) }
							data-event-type="compare"
							data-event-name={ getCompareTableEventName(option.planName, 'link', 'View Plan Details') }
						>
							{ 'View Plan Details' }
						</a>
					) : (
						<span
							aria-hidden="true"
							style={ { visibility: 'hidden' } }
						>
							{ 'View Plan Details Placeholder' }
						</span>
					)
				}
			</div>
		));
	};

	return (
		<div
			className="c-compare-table-header"
			role="rowgroup"
		>
			<div role="row">
				<fieldset className="o-grid u-b-0 u-m-b-0">
					<div className={ `o-grid__item-12 ${gridClasses} u-d-flex u-f-align-center` }>
						<legend className="u-text-bold">Compare plans:</legend>
					</div>
					{ renderSelectedOptionsTiles() }
				</fieldset>
			</div>
		</div>
	);
};

HeaderSection.propTypes = {
	plansList: PropTypes.arrayOf(PropTypes.object).isRequired,
	headersNumber: PropTypes.number.isRequired,
	onChange: PropTypes.func.isRequired,
	selectedOptions: PropTypes.arrayOf(PropTypes.object).isRequired,
	gridClasses: PropTypes.string.isRequired,
};

export default HeaderSection;

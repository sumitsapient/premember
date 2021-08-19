import React, { useEffect, useState } from 'react';
import PropTypes from 'prop-types';

import axios from 'axios';

import TempoIconBlock from '../../sharedComponents/tempoIconBlock.component';

import { sentenceCase } from '../../../utils/helpers';

const PlanCardColumn = ({
	icons,
	items,
	title,
}) => {
	const [loadedIcons, setLoadedIcons] = useState([]);

	/**
	 * Converts number to currency format
	 *
	 * @param {Number} val - Number that should be converted to currency format
	 *
	 * @returns {string}
	 */
	const convertToCurrencyFormat = (val) => {
		const currencyOptions = {
			style: 'currency',
			currency: 'USD',
		};

		// remove decimals if integer
		if (val % 1 === 0) {
			currencyOptions.minimumFractionDigits = 0;
			currencyOptions.maximumFractionDigits = 1;
		}

		return val.toLocaleString('en-US', currencyOptions);
	};

	const renderItem = (description, index) => (
		<div className="o-grid__item-auto o-grid__item-12@md" key={ `item-${index}` }>
			<p className="unity-type-h2">
				{ convertToCurrencyFormat(items[description]) }
			</p>
			<p className="u-color-gray-dark">
				{ sentenceCase(description, 'camel') }
			</p>
		</div>
	);

	const renderIcon = (description, icon, image) => (
		<div
			className="o-grid__item-6 o-grid__item-4@md u-m-b"
			key={ `item-${description}` }
		>
			<TempoIconBlock
				svgIcon={ icon || image }
				text={ description }
				image={ !icon }
				smallText
				twoColor
				vertical
			/>
		</div>
	);

	/**
	 * Generates icon object, then gets and sets SVGs from URL
	 */
	const generateIcons = () => {
		const newLoadedIcons = [...loadedIcons];

		Promise.all(icons.map(icon => axios.get(icon.icon))).then((responseArr) => {
			responseArr.forEach((response, index) => {
				newLoadedIcons.push({
					icon: response.data || null,
					image: false,
					title: icons[index].title,
				});
			});

			setLoadedIcons(newLoadedIcons);
		}).catch((error) => {
			console.error(error);
			icons.forEach((icon) => {
				newLoadedIcons.push({
					icon: false,
					image: icon.icon,
					title: icon.title,
				});
			});
			setLoadedIcons(newLoadedIcons);
		});
	};

	useEffect(() => {
		if (icons) {
			generateIcons();
		}
	}, []);

	return (
		<div className="o-grid__item-12 o-grid__item@md">
			<div className="c-card__plan-costs">
				{
					title && (
						<h4 className="unity-type-h6">
							{title}
						</h4>
					)
				}

				<div className={ `c-card__plan-cost-items o-grid${Object.keys(items).length < 2 ? ' c-card__plan-cost-items--center' : ''}` }>
					{
						items && (
							Object.keys(items).map((description, index) => renderItem(description, index))
						)
					}
				</div>

				{
					icons && (
						<div className="o-grid">
							{ loadedIcons.map(col => renderIcon(col.title, col.icon, col.image)) }
						</div>
					)
				}
			</div>
		</div>
	);
};

PlanCardColumn.propTypes = {
	title: PropTypes.oneOfType([
		PropTypes.string,
		PropTypes.oneOf([false]),
	]),
	items: PropTypes.oneOfType([
		PropTypes.objectOf(PropTypes.oneOfType([
			PropTypes.string,
			PropTypes.number,
		])),
		PropTypes.oneOf([false]),
	]),
	icons: PropTypes.oneOfType([
		PropTypes.arrayOf(PropTypes.exact({
			title: PropTypes.string,
			icon: PropTypes.string,
		})),
		PropTypes.oneOf([false]),
	]),
};

PlanCardColumn.defaultProps = {
	title: false,
	items: false,
	icons: false,
};

export default PlanCardColumn;

import React from 'react';
import PropTypes from 'prop-types';

import SimpleTextTile from './tiles/simpleTextTile.component';
import CareCostsTile from './tiles/careCostsTile.component';
import ExtendedTextTile from './tiles/extendedTextTile.component';
import ExtendedCostsTile from './tiles/extendedCostsTile.component';
import SimpleCostsTile from './tiles/priceTile.component';
import PercentageTile from './tiles/percentageTile.component';
import ExtendedCostsTextTile from './tiles/extendedCostsTextTile.component';
import PerMonthTile from './tiles/perMonthTile.component';
import ExtendedPerMonthTile from './tiles/extendedPerMonthTile.component';
import LinkTile from './tiles/linkTile.component';

const DataRow = ({
	id,
	sectionID,
	plansList,
	rowTitle,
	rowDescription,
	rowType,
	type,
	tilesTitle,
	tiles,
	gridClasses,
}) => {
	const renderHeading = tilesTitle !== 'Family' && tilesTitle !== 'Individual + One' && tilesTitle !== 'Individual + Children' && tilesTitle !== 'Individual + Family';

	const renderTile = (tile, index) => {
		const Wrapper = ({ children }) => (
			<div
				className={ gridClasses }
				role="cell"
				aria-describedby={ `header-${index} rowTitle-${sectionID}-${id}` }
			>
				<div className="c-card__table-item">
					{ children }
				</div>
			</div>
		);

		Wrapper.propTypes = {
			children: PropTypes.objectOf(PropTypes.any).isRequired,
		};

		switch (type) {
			case 'simple_text':
				return (
					<Wrapper key={ index }>
						<SimpleTextTile data={ tile } />
					</Wrapper>
				);
			case 'extended_text':
				return (
					<Wrapper key={ index }>
						<ExtendedTextTile data={ tile } />
					</Wrapper>
				);
			case 'extended_costs':
				return (
					<Wrapper key={ index }>
						<ExtendedCostsTile data={ tile } />
					</Wrapper>
				);
			case 'care_service_costs':
				return (
					<Wrapper key={ index }>
						<CareCostsTile
							data={ tile }
							plansData={ plansList[index] }
							rowType={ rowType }
						/>
					</Wrapper>
				);
			case 'price':
				return (
					<Wrapper key={ index }>
						<SimpleCostsTile
							title={ tilesTitle }
							data={ tile }
						/>
					</Wrapper>
				);
			case 'percentage':
				return (
					<Wrapper key={ index }>
						<PercentageTile
							title={ tilesTitle }
							data={ tile }
						/>
					</Wrapper>
				);
			case 'link':
				return (
					<Wrapper key={ index }>
						<LinkTile
							title={ tilesTitle }
							data={ tile }
							rowTitle={ rowTitle }
						/>
					</Wrapper>
				);
			case 'per_month':
				// Check if override exists
				if (Object.prototype.hasOwnProperty.call(tile, 'value1') && tile.value1) {
					return (
						<Wrapper key={ index }>
							<strong>
								{ tile.value1 }
							</strong>
						</Wrapper>
					);
				}

				return (
					<Wrapper key={ index }>
						<PerMonthTile
							title={ tilesTitle }
							data={ tile }
						/>
					</Wrapper>
				);
			case 'extended_per_month':
				return (
					<Wrapper key={ index }>
						<ExtendedPerMonthTile data={ tile } />
					</Wrapper>
				);
			case 'extended_costs_text':
				return (
					<Wrapper key={ index }>
						<ExtendedCostsTextTile
							data={ tile }
							costType={ tilesTitle }
						/>
					</Wrapper>
				);
			default:
				return '';
		}
	};

	const filteredTiles = tiles.filter(tile => Object.keys(tile).length > 0);

	// Don't render empty sub rows
	if (filteredTiles.length < 1 && !renderHeading) {
		return null;
	}

	return (
		<div
			className={ `c-card__table o-grid${!renderHeading ? ' c-card__table--sub-group' : ''}` }
			role="row"
		>
			<div
				aria-labelledby={ `title-${sectionID} subTitle-${sectionID}` }
				className={ `o-grid__item-12 ${gridClasses}` }
				role="rowheader"
			>
				<div className="c-card__table-item c-card__table-item--heading">
					{
						rowTitle && renderHeading && (
							<p
								className="u-text-bold"
								id={ `rowTitle-${sectionID}-${id}` }
							>
								{ rowTitle }
							</p>
						)
					}
					{
						rowDescription && renderHeading && (
							<p>
								{ rowDescription }
							</p>
						)
					}
				</div>
			</div>
			{ type !== 'care_service_costs' && (
				tiles.map((tile, i) => renderTile(tile, i))
			) }
			{ type === 'care_service_costs' && (
				tiles.map((tile, i) => renderTile(tile, i))
			) }
		</div>
	);
};

DataRow.defaultProps = {
	rowTitle: '',
	rowDescription: '',
	tilesTitle: undefined,
	plansList: [],
	rowType: '',
};

DataRow.propTypes = {
	id: PropTypes.number.isRequired,
	sectionID: PropTypes.number.isRequired,
	rowTitle: PropTypes.string,
	rowDescription: PropTypes.string,
	plansList: PropTypes.arrayOf(PropTypes.object),
	type: PropTypes.string.isRequired,
	tilesTitle: PropTypes.string,
	tiles: PropTypes.arrayOf(PropTypes.object).isRequired,
	rowType: PropTypes.string,
	gridClasses: PropTypes.string.isRequired,
};

export default DataRow;

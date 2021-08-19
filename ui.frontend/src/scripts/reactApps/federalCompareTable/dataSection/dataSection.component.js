import React from 'react';
import PropTypes from 'prop-types';
import DataRow from './dataRow.component';

const DataSection = ({ id, section }) => {
	const {
		gridClasses,
		nextIsSubSection,
	} = section;

	let cardClasses = [
		'c-card',
		'c-card--compare-table',
		nextIsSubSection ? 'c-card--section-heading' : null,
		!section.sectionTitle && section.subSectionTitle ? 'c-card--section-sub' : null,
		!section.sectionTitle && !section.subSectionTitle ? 'c-card--section-no-title' : null,
	];

	cardClasses = cardClasses.filter(item => item).join(' ');

	return (
		<div className={ cardClasses }>
			{/* Title */}
			{
				section.sectionTitle && (
					<div
						className="c-card__header"
						id={ `title-${id}` }
					>
						<h2 className="c-card__title">{ section.sectionTitle }</h2>
						{
							section.sectionSubTitle && (
								<span className="c-card__title">{ section.sectionSubTitle }</span>
							)
						}
					</div>
				)
			}
			{/* Subtitle */}
			{
				section.subSectionTitle && (
					<div
						className="c-card__header c-card__header--subtitle"
						id={ `subTitle-${id}` }
					>
						<h3 className="c-card__title">{ section.subSectionTitle }</h3>
					</div>
				)
			}
			{/* Data */}
			<div role="rowgroup" className="c-card__body">
				{
					section.rows.map((row, i) => (
						<DataRow
							id={ i }
							sectionID={ id }
							key={ i }
							rowTitle={ row.rowTitle }
							rowDescription={ row.rowDescription }
							type={ row.type }
							tilesTitle={ row.tilesTitle }
							tiles={ row.tiles }
							tileCopayTypes={ row.tileCopayTypes }
							plansList={ row.planList }
							rowType={ row.rowType }
							gridClasses={ gridClasses }
						/>
					))
				}
			</div>
		</div>
	);
};

DataSection.propTypes = {
	id: PropTypes.number.isRequired,
	section: PropTypes.shape().isRequired,
};

export default DataSection;

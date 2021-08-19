import React from 'react';
import PropTypes from 'prop-types';
import { isEmpty } from '../../../../utils/helpers';
import { pluralizeText } from '../../../../utils/plan';

const PerMonthTile = ({
	title,
	data,
	rowID,
	className,
}) => (
	<div role="cell" aria-labelledby={ `rowTitle-${rowID}` } className={ `compare-table__tile${className}` }>
		{
			!isEmpty(data) && (
				<p>
					{
						isEmpty(data.value0) ? (
							<>
								<strong aria-hidden="true">N/A</strong>
								<span className="u-visually-hidden">Not Applicable</span>
							</>
						) : (
							<b>
								{ title }
								{ pluralizeText(data.value0, 'month') }
							</b>
						)
					}
				</p>
			)
		}
	</div>
);

PerMonthTile.defaultProps = {
	data: {},
	className: '',
	title: '',
};

PerMonthTile.propTypes = {
	data: PropTypes.objectOf(PropTypes.string),
	title: PropTypes.string,
	rowID: PropTypes.number.isRequired,
	className: PropTypes.string,
};

export default PerMonthTile;

import React from 'react';
import PropTypes from 'prop-types';
import { isEmpty } from '../../../../utils/helpers';
import { pluralizeText } from '../../../../utils/plan';

const PerMonthTile = ({
	title,
	data,
}) => (
	!isEmpty(data) && (
		<p className="u-m-b-0">
			{
				isEmpty(data.value0) ? (
					<>
						<strong aria-hidden="true">N/A</strong>
						<span className="u-visually-hidden">Not Applicable</span>
					</>
				) : (
					<strong>
						{ title }
						{ pluralizeText(data.value0, 'month') }
					</strong>
				)
			}
		</p>
	)
);

PerMonthTile.defaultProps = {
	data: {},
	title: '',
};

PerMonthTile.propTypes = {
	data: PropTypes.objectOf(PropTypes.string),
	title: PropTypes.string,
};

export default PerMonthTile;

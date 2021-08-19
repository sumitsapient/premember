import React from 'react';
import PropTypes from 'prop-types';
import { isEmpty } from '../../../../utils/helpers';
import { getCompareTableEventName } from '../../../../utils/plan';
import Button from '../../../sharedComponents/button.component';

const LinkTile = ({
	title,
	data,
	rowTitle,
	rowID,
	className,
}) => {
	const linkType = (value) => {
		if (!value) {
			return undefined;
		}

		if (value.includes('.pdf')) {
			return 'download';
		}

		return 'link';
	};

	const setLinkText = value => (!value ? 'View All Benefits For This Plan' : value);

	const getTileContent = () => (
		typeof data.value3 === 'undefined' || data.value3 === false || typeof data.value0 === 'undefined' ? (
			<>
				{ !!title && <p className="compare-table__tile-title">{ title }</p> }
				<Button
					url={ !data.value0 ? '#' : data.value0 }
					text={ setLinkText(data.value1) }
					eventName={
						getCompareTableEventName(data.value2, linkType(data.value0), setLinkText(data.value1), rowTitle)
					}
					eventType="compare"
				/>
			</>
		) : (
			<p>
				<span aria-hidden="true"><strong>N/A</strong></span>
				<span className="u-visually-hidden">Not Applicable</span>
			</p>
		)
	);

	return (
		<div role="cell" aria-labelledby={ `rowTitle-${rowID}` } className={ `compare-table__tile compare-table__tile--link${className}` }>
			{ !isEmpty(data) && getTileContent() }
		</div>
	);
};

LinkTile.defaultProps = {
	title: undefined,
	data: {},
	rowTitle: '',
	className: '',
};

LinkTile.propTypes = {
	title: PropTypes.string,
	data: PropTypes.shape(),
	rowTitle: PropTypes.string,
	rowID: PropTypes.number.isRequired,
	className: PropTypes.string,
};

export default LinkTile;

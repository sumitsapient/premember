import React from 'react';
import PropTypes from 'prop-types';
import { isEmpty } from '../../../../utils/helpers';
import { getCompareTableEventName } from '../../../../utils/plan';
import Button from '../../../sharedComponents/button.component';

const LinkTile = ({
	title,
	data,
	rowTitle,
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
		typeof data.value3 === 'undefined' || data.value3 === false ? (
			<>
				{ !!title && <p className="u-m-b-0">{ title }</p> }
				<Button
					className="u-m-vert-0"
					eventName={
						getCompareTableEventName(data.value2, linkType(data.value0), setLinkText(data.value1), rowTitle)
					}
					eventType="compare"
					text={ setLinkText(data.value1) }
					url={ !data.value0 ? '#' : data.value0 }
				/>
			</>
		) : (
			<p className="u-m-b-0">
				<span aria-hidden="true"><strong>Not Applicable</strong></span>
				<span className="u-visually-hidden">Not Applicable</span>
			</p>
		)
	);

	return (
		!isEmpty(data) && getTileContent()
	);
};

LinkTile.defaultProps = {
	title: undefined,
	data: {},
	rowTitle: '',
};

LinkTile.propTypes = {
	title: PropTypes.string,
	data: PropTypes.shape(),
	rowTitle: PropTypes.string,
};

export default LinkTile;

import React from 'react';
import PropTypes from 'prop-types';
import { ReactFromHtml } from '@bitjourney/react-from-html';
import { isEmpty, checkIfArrayHasEmptyElements } from '../../../../utils/helpers';

const reactFromHtml = new ReactFromHtml();

const SimpleTextTile = ({
	data,
}) => {
	const listOfContentElements = [];
	const textElementsList = Object.values(data);

	if (isEmpty(data)) {
		return '';
	}

	if (checkIfArrayHasEmptyElements(textElementsList)) {
		return (
			<p>
				<strong aria-hidden="true">Not Applicable</strong>
				<span className="u-visually-hidden">Not Applicable</span>
			</p>
		);
	}

	textElementsList.map((value) => {
		if (isEmpty(value)) {
			listOfContentElements.push('');
		} else {
			listOfContentElements.push(reactFromHtml.parseToNodeList(value));
		}
	});

	return listOfContentElements;
};

SimpleTextTile.defaultProps = {
	data: {},
};

SimpleTextTile.propTypes = {
	data: PropTypes.objectOf(PropTypes.string),
};

export default SimpleTextTile;

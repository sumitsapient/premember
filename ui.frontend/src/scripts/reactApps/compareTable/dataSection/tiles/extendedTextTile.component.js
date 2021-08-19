import React from 'react';
import PropTypes from 'prop-types';
import { ReactFromHtml } from '@bitjourney/react-from-html';
import {
	isEmpty,
	checkIfArrayHasEmptyElements,
} from '../../../../utils/helpers';
import { replaceTokenWithValue } from '../../../../utils/plan';

const reactFromHtml = new ReactFromHtml();

const ExtendedTextTile = ({ data }) => {
	const textElementsList = Object.values(data);
	const filteredTextElementsList = textElementsList.filter(
		(_, i) => i % 2 === 0,
	);
	const listOfContentElements = [];

	if (isEmpty(data)) {
		return '';
	}

	if (checkIfArrayHasEmptyElements(filteredTextElementsList)) {
		return (
			<p>
				<strong aria-hidden="true">Not Applicable</strong>
				<span className="u-visually-hidden">Not Applicable</span>
			</p>
		);
	}

	for (let i = 0; i < textElementsList.length; i += 2) {
		if (isEmpty(textElementsList[i])) {
			listOfContentElements.push('');
		} else {
			const renderPriceValue = `${
				textElementsList[i + 1] ? `$${textElementsList[i + 1]}` : 'not covered'
			}`;
			const formattedValue = replaceTokenWithValue(
				textElementsList[i],
				renderPriceValue,
			);
			listOfContentElements.push(reactFromHtml.parseToNodeList(formattedValue));
		}
	}

	return listOfContentElements;
};

ExtendedTextTile.defaultProps = {
	data: {},
};

ExtendedTextTile.propTypes = {
	data: PropTypes.objectOf(PropTypes.string),
};

export default ExtendedTextTile;

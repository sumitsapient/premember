import React from 'react';
import PropTypes from 'prop-types';
import { ReactFromHtml } from '@bitjourney/react-from-html';
import { isEmpty, checkIfArrayHasEmptyElements } from '../../../../utils/helpers';
import { replaceTokenWithValue } from '../../../../utils/plan';

const reactFromHtml = new ReactFromHtml();

const ExtendedTextTile = ({ data, rowID, className }) => {
	const renderContent = (objectElem) => {
		const textElementsList = Object.values(objectElem);
		const filteredTextElementsList = textElementsList.filter((_, i) => i % 2 === 0);
		const listOfContentElements = [];

		if (isEmpty(objectElem)) {
			return '';
		}

		if (checkIfArrayHasEmptyElements(filteredTextElementsList)) {
			return (
				<p>
					<strong aria-hidden="true">N/A</strong>
					<span className="u-visually-hidden">Not Applicable</span>
				</p>
			);
		}

		for (let i = 0; i < textElementsList.length; i += 2) {
			if (isEmpty(textElementsList[i])) {
				listOfContentElements.push('');
			} else {
				const renderPriceValue = `${textElementsList[i + 1] ? `$${textElementsList[i + 1]}` : 'N/A'}`;
				const formattedValue = replaceTokenWithValue(textElementsList[i], renderPriceValue);
				listOfContentElements.push(reactFromHtml.parseToNodeList(formattedValue));
			}
		}

		return listOfContentElements;
	};

	return (
		<div role="cell" aria-labelledby={ `rowTitle-${rowID}` } className={ `compare-table__tile${className}` }>
			{ renderContent(data) }
		</div>
	);
};

ExtendedTextTile.defaultProps = {
	data: {},
	className: '',
};

ExtendedTextTile.propTypes = {
	data: PropTypes.objectOf(PropTypes.string),
	rowID: PropTypes.number.isRequired,
	className: PropTypes.string,
};

export default ExtendedTextTile;

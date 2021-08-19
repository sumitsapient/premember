import React from 'react';
import PropTypes from 'prop-types';
import { ReactFromHtml } from '@bitjourney/react-from-html';
import { isEmpty, checkIfArrayHasEmptyElements } from '../../../../utils/helpers';

const reactFromHtml = new ReactFromHtml();

const SimpleTextTile = ({
	id,
	data,
	rowID,
	className,
}) => {
	const renderContent = (objectElem) => {
		const listOfContentElements = [];
		const textElementsList = Object.values(objectElem);

		if (isEmpty(objectElem)) {
			return '';
		}

		if (checkIfArrayHasEmptyElements(textElementsList)) {
			return (
				<>
					<p>
						<strong aria-hidden="true">N/A</strong>
						<span className="u-visually-hidden">Not Applicable</span>
					</p>
				</>
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

	return (
		<div role="cell" aria-describedby={ `rowTitle-${rowID} header-${id}` } className={ `compare-table__tile${className}` }>
			{ renderContent(data) }
		</div>
	);
};

SimpleTextTile.defaultProps = {
	data: {},
	className: '',
};

SimpleTextTile.propTypes = {
	id: PropTypes.number.isRequired,
	data: PropTypes.objectOf(PropTypes.string),
	rowID: PropTypes.number.isRequired,
	className: PropTypes.string,
};

export default SimpleTextTile;

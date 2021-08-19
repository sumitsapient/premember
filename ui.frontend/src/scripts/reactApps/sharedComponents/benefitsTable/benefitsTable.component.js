import React, { useEffect, useRef } from 'react';
import PropTypes from 'prop-types';

import RowGroup from './benefitsTableRowGroup.component';
import Row from './benefitsTableRow.component';
import Cell from './benefitsTableCell.component';
import Notes from './benefitsTableNotes.component';

const CLASSES = {
	cell_hidden: 'benefits-table__cell--visually-hidden',
};

const BenefitsTable = ({
	ariaLabel,
	children,
	hasSibling,
}) => {
	const ref = useRef(null);
	const classes = ['benefits-table'];

	if (hasSibling) {
		classes.push('benefits-table--has-sibling');
	}

	const setAttrs = () => {
		const table = ref.current;
		const cells = table.querySelectorAll('[role="cell"]');
		const headings = table.querySelectorAll('[role="columnheader"]');
		const colHeadings = [];

		headings.forEach((heading) => {
			colHeadings.push({
				value: heading.innerText.trim(),
				hidden: heading.classList.contains(CLASSES.cell_hidden),
			});
		});

		cells.forEach((cell) => {
			const parent = cell.parentNode;
			const index = Array.prototype.indexOf.call(parent.children, cell);

			if (colHeadings[index] && !colHeadings[index].hidden) {
				cell.setAttribute('data-heading', colHeadings[index].value);
			}
		});
	};

	useEffect(() => setAttrs(), []);

	return (
		<div
			ref={ ref }
			className={ classes.join(' ') }
			role="table"
			aria-label={ ariaLabel }
		>
			{ children }
		</div>
	);
};

BenefitsTable.propTypes = {
	ariaLabel: PropTypes.string,
	children: PropTypes.oneOfType([
		PropTypes.any,
	]),
	hasSibling: PropTypes.bool,
};

BenefitsTable.defaultProps = {
	children: null,
	ariaLabel: null,
	hasSibling: false,
};

BenefitsTable.RowGroup = RowGroup;
BenefitsTable.Row = Row;
BenefitsTable.Cell = Cell;
BenefitsTable.Notes = Notes;

export default BenefitsTable;

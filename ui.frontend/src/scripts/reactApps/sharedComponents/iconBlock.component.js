import React from 'react';
import PropTypes from 'prop-types';
import { ReactFromHtml } from '@bitjourney/react-from-html';

const reactFromHtml = new ReactFromHtml();

const IconBlock = ({ title, text, svgIcon }) => {
	const checkDataType = (data) => {
		if (Array.isArray(data)) {
			return data.map((option, i) => {
				if (option !== '') {
					return (
						<p key={ i } className="c-icon-block__text">{ option }</p>
					);
				}

				return '';
			});
		}

		return (
			<p className="c-icon-block__text">{ data }</p>
		);
	};

	return (
		<div className="iconBlock">
			<div className="c-icon-block o-media c-icon-block--sidebyside">
				<div className="o-media__img c-icon-block--sidebyside">
					<div className="c-icon-box c-icon-box--circle u-bg-blue-medium">
						<div className="cmp-icon">
							{ reactFromHtml.parseToFragment(svgIcon) }
						</div>
					</div>
				</div>
				<div className="o-media__body">
					<h3 className="c-icon-block__title unity-type-h5 u-text-bold">{ title }</h3>
					{ checkDataType(text) }
				</div>
			</div>
		</div>
	);
};

IconBlock.defaultProps = {
	text: '',
};

IconBlock.propTypes = {
	title: PropTypes.string.isRequired,
	text: PropTypes.oneOfType([
		PropTypes.string,
		PropTypes.arrayOf(PropTypes.string),
	]),
	svgIcon: PropTypes.string.isRequired,
};

export default IconBlock;

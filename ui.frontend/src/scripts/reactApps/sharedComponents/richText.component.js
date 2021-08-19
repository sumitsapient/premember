import React from 'react';
import PropTypes from 'prop-types';
import { ReactFromHtml } from '@bitjourney/react-from-html';

const reactFromHtml = new ReactFromHtml();

const RichText = ({ content, className, singleTab }) => (

	<div className="aem-component-rte">
		<div className={ `cmp-text ${className}` }>
			{singleTab === true
				? <h2>{reactFromHtml.parseToFragment(content.replace(/<[^>]+>/g, ''))}</h2>
				: reactFromHtml.parseToFragment(content)
			}
		</div>
	</div>
);

RichText.defaultProps = {
	className: '',
	singleTab: false,
};

RichText.propTypes = {
	content: PropTypes.string.isRequired,
	className: PropTypes.string,
	singleTab: PropTypes.bool,
};

export default RichText;

import React from 'react';
import PropTypes from 'prop-types';

import DOMPurify from 'dompurify';

const TempoIconBlock = ({
	circle,
	color,
	image,
	smallText,
	svgIcon,
	text,
	title,
	twoColor,
	vertical,
}) => {
	const mediaClasses = [
		'o-media',
		vertical ? 'o-media--stacked' : null,
	];

	const imgClasses = [
		'o-media__img',
		!vertical ? 'u-m-r' : null,
	];

	const iconClasses = [
		'c-tempo-icon',
		'c-tempo-icon--md',
		twoColor ? 'c-tempo-icon--2c' : null,
		!twoColor && circle && color === 'blue' ? 'c-tempo-icon--blue' : null,
		circle && color === 'blue' ? 'c-tempo-icon--2c-blue' : null,
		circle && color === 'white' ? 'c-tempo-icon--2c' : null,
	];

	const TextTag = smallText ? 'small' : 'p';

	/**
	 * Formats array of strings:
	 * * removes empty items
	 * * joins into a single space separated string
	 *
	 * @param classes {Array<string>}
	 *
	 * @return {string}
	 */
	const formatClasses = classes => classes.filter(item => item).join(' ');

	return (
		<div className={ formatClasses(mediaClasses) }>
			<div className={ formatClasses(imgClasses) }>
				{ image ? (
					<div className={ formatClasses(iconClasses) }>
						{/* eslint-disable-next-line jsx-a11y/alt-text */}
						<img
							src={ svgIcon }
							aria-hidden="true"
						/>
					</div>
				) : (
					<div
						className={ formatClasses(iconClasses) }
						// eslint-disable-next-line react/no-danger
						dangerouslySetInnerHTML={ {
							__html: DOMPurify.sanitize(svgIcon, {
								USE_PROFILES: {
									svg: true,
									svgFilters: true,
								},
							}),
						} }
					/>
				) }
			</div>
			{ (title || text) && (
				<div className="o-media__body">
					{ title && (
						<h3 className="unity-type-h6">{ title }</h3>
					) }
					{ text && (
						<TextTag className={ smallText ? 'u-color-gray-dark' : null }>
							{ Array.isArray(text) ? (
								text.map((txt, index) => <span key={ index } className="u-d-block">{ txt }</span>)
							) : text }
						</TextTag>
					) }
				</div>
			) }
		</div>
	);
};

TempoIconBlock.defaultProps = {
	circle: false,
	color: null,
	image: false,
	smallText: false,
	text: null,
	title: null,
	twoColor: true,
	vertical: false,
};

TempoIconBlock.propTypes = {
	circle: PropTypes.bool,
	color: PropTypes.oneOf([
		'blue',
		'white',
		null,
	]),
	image: PropTypes.bool,
	smallText: PropTypes.bool,
	svgIcon: PropTypes.string.isRequired,
	text: PropTypes.oneOfType([
		PropTypes.string,
		PropTypes.arrayOf(PropTypes.string),
	]),
	title: PropTypes.string,
	twoColor: PropTypes.bool,
	vertical: PropTypes.bool,
};

export default TempoIconBlock;

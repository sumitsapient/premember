import React from 'react';
import PropTypes from 'prop-types';

const Button = ({
	className,
	url,
	text,
	showIcon,
	eventType,
	eventName,
}) => {
	const renderLinkIconByType = (link) => {
		if (link.includes('.pdf')) {
			return (
				<span className="o-svg-icon o-svg-icon--lg o-svg-icon-pdf u-m-l-xs">
					<svg focusable="false" viewBox="0 0 24 24" version="1.1">
						<g stroke="none" strokeWidth="2" fill="none" fillRule="evenodd" fontFamily="Helvetica-Bold, Helvetica" fontSize="18" fontWeight="bold">
							<text fill="#2D2D39">
								<tspan x="0" y="23">PDF</tspan>
							</text>
						</g>
					</svg>
				</span>
			);
		}

		return (
			<span className="o-svg-icon o-svg-icon--lg u-m-l-xs">
				<svg viewBox="0, 0, 24, 24" focusable="false" shapeRendering="geometricPrecision">
					<path d="M20.385,20.385 L3.615,20.385 L3.615,3.615 L12,3.615 L12,1.219 L3.615,1.219 C2.285,1.219 1.219,2.297 1.219,3.615 L1.219,20.385 C1.219,21.703 2.285,22.781 3.615,22.781 L20.385,22.781 C21.703,22.781 22.781,21.703 22.781,20.385 L22.781,12 L20.385,12 L20.385,20.385 z M14.396,1.219 L14.396,3.615 L18.696,3.615 L6.921,15.39 L8.61,17.079 L20.385,5.304 L20.385,9.604 L22.781,9.604 L22.781,1.219 L14.396,1.219 z" fill="currentColor" />
				</svg>
			</span>
		);
	};

	let btnClasses = [
		'c-button',
		'c-button--naked',
		className || null,
	];
	btnClasses = btnClasses.filter(item => item).join(' ');

	return (
		<a
			className={ btnClasses }
			href={ url }
			rel={ showIcon ? 'noopener noreferrer' : '' }
			target={ showIcon ? '_blank' : '' }
			data-event-type={ eventType }
			data-event-name={ eventName }>
			{ text }
			{ showIcon && renderLinkIconByType(url) }
			<span className="u-visually-hidden">Opens in a new tab</span>
		</a>
	);
};

Button.defaultProps = {
	className: null,
	showIcon: true,
	eventType: '',
	eventName: '',
};

Button.propTypes = {
	className: PropTypes.string,
	url: PropTypes.string.isRequired,
	text: PropTypes.string.isRequired,
	showIcon: PropTypes.bool,
	eventType: PropTypes.string,
	eventName: PropTypes.string,
};

export default Button;

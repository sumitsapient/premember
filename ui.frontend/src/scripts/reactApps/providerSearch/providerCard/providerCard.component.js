import React from 'react';
import PropTypes from 'prop-types';
import { normalizeText } from '../../../utils/helpers';

const ProviderCard = ({ option }) => {
	const {
		planName,
		providerLink,
		pdfLink,
		hasStateSpecificProvider,
		stateSpecificProvider,
	} = option;

	const getProviderLinkEventName = () => `${normalizeText(planName)}|${normalizeText(providerLink.text)}`;
	const getPdfLinkEventName = () => `${normalizeText(planName)}|${normalizeText(pdfLink.text)}`;
	const getStateSpecificLinkEventName = () => `${normalizeText(planName)}|${normalizeText(stateSpecificProvider.link.text)}`;

	return (
		<div className="provider-card o-box u-bg-white">
			<h2 className="provider-card__title u-text-bold">{ planName }</h2>
			<button
				className="provider-card__button c-button c-button--primary u-w-100 js-language-exit-modal-trigger"
				data-href={ providerLink.link }
				data-toggle="modal"
				data-target="#language-exit-modal"
				data-event-type="provider_card"
				data-event-name={ getProviderLinkEventName() }
				type="button"
			>
				{ providerLink.text }
				<span className="o-svg-icon o-svg-icon--lg u-m-l-xs">
					<svg viewBox="0, 0, 24, 24" focusable="false" shapeRendering="geometricPrecision">
						<path d="M20.385,20.385 L3.615,20.385 L3.615,3.615 L12,3.615 L12,1.219 L3.615,1.219 C2.285,1.219 1.219,2.297 1.219,3.615 L1.219,20.385 C1.219,21.703 2.285,22.781 3.615,22.781 L20.385,22.781 C21.703,22.781 22.781,21.703 22.781,20.385 L22.781,12 L20.385,12 L20.385,20.385 z M14.396,1.219 L14.396,3.615 L18.696,3.615 L6.921,15.39 L8.61,17.079 L20.385,5.304 L20.385,9.604 L22.781,9.604 L22.781,1.219 L14.396,1.219 z" fill="currentColor" />
					</svg>
				</span>
				<span className="u-visually-hidden">Opens in a new tab</span>
			</button>
			{pdfLink.link.length > 0 && (
				<a
					className="provider-card__button c-button c-button--naked u-m-t-sm u-w-100 u-f-justify-start"
					href={ pdfLink.link }
					data-event-type="provider_card"
					data-event-name={ getPdfLinkEventName() }
				>
					{pdfLink.text}
				</a>
			)
			}
			{
				hasStateSpecificProvider && (
					<>
						<p className="u-m-t u-m-b-0 u-text-medium">{ stateSpecificProvider.description }</p>
						<button
							className="c-button c-button--naked c-button--primary u-p-0 js-language-exit-modal-trigger"
							data-href={ stateSpecificProvider.link.link }
							data-toggle="modal"
							data-target="#language-exit-modal"
							data-event-type="provider_card"
							data-event-name={ getStateSpecificLinkEventName() }
							type="button"
						>
							{ stateSpecificProvider.link.text }
							<span className="o-svg-icon o-svg-icon--lg u-m-l-xs">
								<svg viewBox="0, 0, 24, 24" focusable="false" shapeRendering="geometricPrecision">
									<path d="M20.385,20.385 L3.615,20.385 L3.615,3.615 L12,3.615 L12,1.219 L3.615,1.219 C2.285,1.219 1.219,2.297 1.219,3.615 L1.219,20.385 C1.219,21.703 2.285,22.781 3.615,22.781 L20.385,22.781 C21.703,22.781 22.781,21.703 22.781,20.385 L22.781,12 L20.385,12 L20.385,20.385 z M14.396,1.219 L14.396,3.615 L18.696,3.615 L6.921,15.39 L8.61,17.079 L20.385,5.304 L20.385,9.604 L22.781,9.604 L22.781,1.219 L14.396,1.219 z" fill="currentColor" />
								</svg>
							</span>
							<span className="u-visually-hidden">Opens in a new tab</span>
						</button>
					</>
				)
			}
		</div>
	);
};

ProviderCard.propTypes = {
	option: PropTypes.shape({
		planName: PropTypes.string.isRequired,
		providerLink: PropTypes.shape(),
		pdfLink: PropTypes.shape(),
		hasStateSpecificProvider: PropTypes.bool.isRequired,
		stateSpecificProvider: PropTypes.shape(),
	}).isRequired,
};

export default ProviderCard;

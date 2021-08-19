import React, { useEffect, useRef, useState } from 'react';
import PropTypes from 'prop-types';

import ZipModal from '../zipModal/zipModal.component';

import { ZIP_EVENT_NAME } from '../../../utils/zip';

const zipMenuEl = document.getElementById('menu-zip-text-pre');
const zipMenuInputEl = document.getElementById('zip-code-input');

const ZipSelector = ({
	btnDefault,
	hasPlans,
	planType,
	showZipOnLoad,
	updatePlans,
	updateSelected,
	zipCode,
}) => {
	const [isLoaded, setIsLoaded] = useState(false);
	const [zipVal, setZipVal] = useState(zipCode);
	const zipRef = useRef(zipVal);
	const menuZipForm = document.getElementById('sub-menu-zip-form');
	const menuZipInput = document.getElementById('zip-code-input');
	let title = 'Plan Options for Federal Employees in ';

	if (!hasPlans && zipVal) {
		title = `Sorry, we don't offer ${planType} plans in `;
	}

	/**
	 * Updates zip code in header
	 */
	const updateZipInHeader = (val) => {
		// prevent setting on initial render
		if (isLoaded) {
			if (zipMenuEl) zipMenuEl.textContent = val.toString();
			if (zipMenuInputEl) zipMenuInputEl.value = val.toString();
		}
		// set loaded to true after first run
		if (!isLoaded) {
			setIsLoaded(true);
		}
	};

	/**
	 * Updates zip code value if no updateSelected function is passed
	 *
	 * @param {String} val - Zip code value
	 */
	const updateZipVal = (val) => {
		zipRef.current = val;
		setZipVal(val);
		updateZipInHeader(val);
	};

	// Update header zip code value on zip code update
	useEffect(() => updateZipVal(zipCode), [zipCode]);

	// Add event listener for zip update in nav
	useEffect(() => {
		if (menuZipForm && menuZipInput) {
			menuZipForm.addEventListener(ZIP_EVENT_NAME, () => {
				const newZipVal = menuZipInput.value;

				if (newZipVal !== zipRef.current) {
					if (updateSelected) {
						updateSelected(newZipVal, 'zipCode');
					} else {
						updateZipVal(newZipVal);
					}

					if (updatePlans) {
						updatePlans();
					}
				}
			});
		}
	}, []);

	/**
	 * Builds anchor component with given place replacing current
	 *
	 * @param text {String} - Anchor text
	 * @param addr {String} - Anchor path
	 * @return {JSX.Element}
	 */
	const buildAnchor = (text, addr) => {
		const newPath = window.location.pathname.split('/');
		newPath[newPath.length - 1] = addr;
		return (
			<a href={ newPath.join('/') }>{ text }</a>
		);
	};

	return (
		<>
			<ZipModal
				id="zip-code"
				showOnLoad={ showZipOnLoad }
				updatePlans={ updatePlans }
				updateSelected={ updateSelected || updateZipVal }
				zipCode={ zipVal }
			/>

			<h2 className="unity-type-h2">
				<span className="zip-title">
					{ title }
				</span>
				<button
					type="button"
					data-toggle="modal"
					data-target="#zip-code"
					className="c-button c-button--naked"
					style={ {
						minHeight: 'auto',
						minWidth: 'auto',
						lineHeight: 'inherit',
						padding: '0 0.25rem',
						color: '#196ECF',
						cursor: 'pointer',
					} }
				>
					<span
						className="zip-text"
						style={ {
							borderBottom: '2px solid #196ECF',
							pointerEvents: 'none',
						} }
					>
						{ zipVal || btnDefault }
					</span>
					<span className="o-svg-icon o-svg-icon--lg">
						<svg viewBox="0, 0, 24, 24" focusable="false">
							<g fill="none" fillRule="evenodd">
								<g fill="currentColor">
									<g>
										<path d="M12 2C8.14 2 5 5.14 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.86-3.14-7-7-7zm-1.56 10H9v-1.44l3.35-3.34 1.43 1.43L10.44 12zm4.45-4.45l-.7.7-1.44-1.44.7-.7c.15-.15.39-.15.54 0l.9.9c.15.15.15.39 0 .54z" transform="translate(-1147 -40) translate(1147 40)" />
									</g>
								</g>
							</g>
						</svg>
					</span>
				</button>
			</h2>

			{ (!hasPlans && zipVal) && (
				<p>
					{'However, we offer '}
					{ buildAnchor('Dental Plans', 'dental-plans') }
					{' and '}
					{ buildAnchor('Vision Plans', 'vision-plans') }
					{' in your Zip Code.'}
				</p>
			) }
		</>
	);
};

ZipSelector.propTypes = {
	btnDefault: PropTypes.string,
	hasPlans: PropTypes.bool,
	planType: PropTypes.string,
	showZipOnLoad: PropTypes.bool,
	updatePlans: PropTypes.func,
	updateSelected: PropTypes.func,
	zipCode: PropTypes.string,
};

ZipSelector.defaultProps = {
	btnDefault: 'Enter Zip',
	hasPlans: true,
	planType: null,
	showZipOnLoad: false,
};

export default React.memo(ZipSelector);

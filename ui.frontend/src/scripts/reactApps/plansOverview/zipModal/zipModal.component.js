import React, { useEffect, useRef } from 'react';
import PropTypes from 'prop-types';

import UnityModal from 'unity-core/src/scripts/modal';

import {
	getRegionFromZip,
	setRegionCookie,
	setDentalRegionCookie,
	setZipCookie,
	isZip,
	removeRegionCookie,
} from '../../../utils/zip';

const ZipModal = ({
	id,
	showOnLoad,
	updatePlans,
	updateSelected,
	zipCode,
}) => {
	const module = useRef({});
	const modalEl = useRef(null);
	const triggerEl = useRef(null);
	const inputEl = useRef(null);
	const errorEl = useRef(null);
	const modalTitleID = `${id}-modal-title`;

	/**
	 * Move modal to end of body tag
	 */
	const moveModal = () => {
		document.body.appendChild(modalEl.current);
	};

	/**
	 * Toggles modal open/close
	 *
	 * @param {Event} [event] - Click event
	 */
	const toggleModal = (event) => {
		if (event) {
			event.preventDefault();
		}

		if (modalEl.current.getAttribute('aria-hidden') === 'true') {
			module.current.showModal(modalEl.current);
		} else {
			module.current.hideModal(event || null);
		}
	};

	/**
* Removes the error on the associated input.
*
* @param {Element} input The input field.
* @param {Element} error The error output element.
*/
	const removeError = (input, error) => {
		const errorField = error;
		input.classList.remove('has-error');
		input.setAttribute('aria-invalid', 'false');
		errorField.classList.remove('active');
		errorField.innerHTML = '';
	};

	/**
	 * Updates the error text and shows the element.
	 *
	 * @param {Element} input The input field.
	 * @param {Element} error The error output element.
	 * @param {String} text The text to display.
	 */
	const showError = (input, error, text) => {
		const errorField = error;
		errorField.innerHTML = text;
		errorField.classList.add('active');
		input.classList.add('has-error');
		input.setAttribute('aria-invalid', 'true');
	};

	/**
	 * Initializes modal and binds click event to modal toggle buttons
	 */
	const initModal = () => {
		const openButtons = document.querySelectorAll(`[data-toggle="modal"][data-target="#${id}"]`);

		// init unity modal
		module.current = new UnityModal();
		module.current.init();

		// focus on element that triggered modal on close
		modalEl.current.addEventListener(module.current.str.EVENT_HIDE, () => {
			if (triggerEl.current) {
				triggerEl.current.focus();
			}
		});

		if (openButtons.length > 0) {
			openButtons.forEach(button => button.addEventListener('click', (evt) => {
				evt.preventDefault();
				triggerEl.current = evt.target;
				toggleModal(evt);
			}));
		}

		if (showOnLoad) {
			toggleModal();
		}
	};

	const checkZipCode = (newUrlString) => {
		const zipCheck = newUrlString.slice(newUrlString.length - 5, newUrlString.length);
		let modifiedStr;
		if (zipCheck.match(/^[0-9]*$/gm)) {
			modifiedStr = newUrlString.slice(0, newUrlString.length - 6);
		} else {
			modifiedStr = newUrlString;
		}
		return modifiedStr;
	};

	const updateURL = (zip) => {
		const urlString = window.location.href;
		let updatedZipCodeUrl;

		if (urlString.endsWith('.html')) {
			const newUrlString = urlString.slice(0, urlString.length - 5);
			// check for zip and return str
			const modifiedStr = checkZipCode(newUrlString);

			updatedZipCodeUrl = `${modifiedStr}.${zip}.html`;
		} else {
			const newUrlString = urlString;
			// check for zip and return str
			const modifiedStr = checkZipCode(newUrlString);
			updatedZipCodeUrl = `${modifiedStr}.${zip}`;
		}

		window.location.href = updatedZipCodeUrl;
	};

	/**
	 * Get region and set cookies code on submit
	 *
	 * @param {Event} event - Submit event
	 */
	const handleZipSubmit = async (event) => {
		event.preventDefault();

		const input = inputEl.current;
		const error = errorEl.current;
		const zip = input.value;

		if (isZip(zip)) {
			if (zip.match(/^[0-9]*$/gm) && zip.length === 5) {
				removeError(input, error);
				const regionData = await getRegionFromZip(zip);

				// if no error do below
				if (regionData.regionName.length > 0 || zip === '99999') {
					setZipCookie(zip);
					// set region cookie
					if (regionData.regionName) {
						setRegionCookie(regionData.regionName);
					}

					if (zip === '99999') {
						removeRegionCookie();
					}

					if (regionData.dentalRegionName) {
						setDentalRegionCookie(regionData.dentalRegionName);
					}

					updateSelected(zip, 'zipCode');

					if (updatePlans) {
						updatePlans();
					}

					toggleModal();
					updateURL(zip);
				} else {
					showError(input, error, 'Please enter a valid zipcode');
				}
			} else {
				showError(input, error, 'Please enter a valid zipcode');
			}
		} else {
			showError(input, error, 'Please enter a valid zipcode');
		}
	};

	/**
	 * Renders modal close button
	 */
	const renderCloseButton = () => (
		<div className="o-modal__close">
			<button
				type="button"
				className="c-button-close c-card--modal__close"
				onClick={ toggleModal }
			>
				<span className="o-svg-icon">
					<svg viewBox="0, 0, 24, 24" focusable="false" shapeRendering="geometricPrecision">
						<path d="M22.792,3.382 L20.618,1.208 L12,9.826 L3.382,1.208 L1.208,3.382 L9.826,12 L1.208,20.618 L3.382,22.792 L12,14.174 L20.618,22.792 L22.792,20.618 L14.174,12 z" fill="currentColor" />
					</svg>
				</span>
				<span className="u-visually-hidden">Close</span>
			</button>
		</div>
	);

	/**
	 * Renders modal header
	 */
	const renderHeader = () => (
		<div className="c-card__header u-p-t-xl">
			<h2 className="c-card__title unity-type-h2" id={ modalTitleID }>
				{'Enter your Zip Code for local benefits'}
			</h2>
			<p className="u-color-gray-dark">
				{'If you’re located outside of the Contiguous United States enter 99999.'}
			</p>
		</div>
	);

	/**
	 * Renders modal footer
	 */
	const renderFooter = () => (
		<div className="c-card__footer">
			<div className="o-list-pack">
				<ul className="o-list-pack__list">
					<li className="o-list-pack__item">
						<button
							type="button"
							className="c-button c-button--secondary"
							onClick={ toggleModal }
						>
							{'Cancel'}
						</button>
					</li>
					<li className="o-list-pack__item">
						<button
							type="submit"
							className="c-button c-button--primary"
						>
							{'Save'}
						</button>
					</li>
				</ul>
			</div>
		</div>
	);

	/**
	 * Renders modal body
	 */
	const renderBody = () => (
		<div className="c-card__body">
			<label htmlFor="zip-code-modal">
				{'Zip Code'}
			</label>
			<input
				ref={ inputEl }
				className="c-input c-zip-code-modal"
				type="text"
				id="zip-code-modal"
				name="zip-code-modal"
				defaultValue={ zipCode }
			/>
			<div
				ref={ errorEl }
				role="alert"
				aria-live="assertive"
				className="zipSearchModal__error js-zip-search__error u-color-red-dark"
			/>
		</div>
	);

	/**
	 * Runs once component is mounted (only once)
	 */
	useEffect(() => {
		moveModal();
		initModal();
	}, []);

	return (
		<div
			ref={ modalEl }
			id={ id }
			role="dialog"
			tabIndex="-1"
			aria-modal="true"
			aria-describedby={ modalTitleID }
			className="o-modal fade zipModal"
			data-hide="modal"
			aria-hidden="true"
			style={ {
				zIndex: 20,
			} }
		>
			<div className="o-modal__dialog modal-dialog" role="document">
				<form className="o-modal__container c-card u-w-m480" onSubmit={ handleZipSubmit }>
					{renderCloseButton()}
					{renderHeader()}
					{renderBody()}
					{renderFooter()}
				</form>
			</div>
		</div>
	);
};

ZipModal.propTypes = {
	id: PropTypes.string.isRequired,
	showOnLoad: PropTypes.bool,
	updatePlans: PropTypes.func,
	updateSelected: PropTypes.func.isRequired,
	zipCode: PropTypes.string,
};

ZipModal.defaultProps = {
	showOnLoad: false,
	updatePlans: null,
	zipCode: '',
};

export default React.memo(ZipModal);

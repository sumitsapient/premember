import UHCJS from '../../uhcjs';
import constants from '../../utils/constants';
import { tabbedToNext, tabbedToPrevious, isSafari } from '../../utils/helpers';

const selectors = {
	modal: '.o-modal',
	modalContainer: '.o-modal__container',
	modalCloseButton: '.c-card--modal__close',
	modalCancelButton: '.c-button-cancel',
	modalContinueButton: '.js-language-exit-modal-button',
	exitModalTriggerButton: '.js-language-exit-modal-trigger',
};

class Modal {
	constructor(elem) {
		this.init(elem);
	}

	init(elem) {
		this.bindElements(elem);

		if (isSafari()) {
			this.changeAriaAttributeForSafari();
		}

		if (!this.exitModalButtons || !this.$modal) {
			return;
		}

		this.attachEvents();
	}

	bindElements(elem) {
		this.$element = elem;
		this.$modal = this.$element.querySelector(selectors.modal);
		this.$modalContainer = this.$element.querySelector(selectors.modalContainer);
		this.$modalCloseButton = this.$modalContainer.querySelector(selectors.modalCloseButton);
		this.$modalCancelButton = this.$modalContainer.querySelector(selectors.modalCancelButton);
		this.$continueButton = this.$modalContainer.querySelector(selectors.modalContinueButton);
		this.exitModalButtons = Array.from(document.querySelectorAll(selectors.exitModalTriggerButton));
		this.body = document.body;
	}

	attachEvents() {
		this.exitModalButtons.forEach((item) => {
			item.addEventListener('click', this.clickModalTriggerButton.bind(this));
			item.addEventListener('keydown', (event) => {
				const isPressedEnterKey = event.keyCode === constants.keyCodes.enterKey && event.target === item;

				if (isPressedEnterKey) {
					this.clickModalTriggerButton(event);
				}
			});
		});
		this.$modalContainer.addEventListener('keydown', this.focusTrap.bind(this));
		this.$continueButton.addEventListener('click', this.setModalState.bind(this));
		this.$modalCloseButton.addEventListener('click', this.setModalState.bind(this));

		if (this.$modalCancelButton) {
			this.$modalCancelButton.addEventListener('click', this.setModalState.bind(this));
		}

		this.$modal.addEventListener('click', (event) => {
			const isClickedOutsideModal = !this.$modalContainer.contains(event.target)
										&& event.target.classList.contains('show');

			if (isClickedOutsideModal) {
				this.setModalState();
			}
		});
	}

	clickModalTriggerButton(event) {
		event.preventDefault();
		this.currentModalTriggerButton = event.target;
		this.assignLinkToButton(this.currentModalTriggerButton);
		this.setModalState();
	}

	focusTrap(event) {
		const isClickedButton = event.keyCode === constants.keyCodes.enterKey;

		if (event.target === this.$modalCloseButton) {
			if (tabbedToNext(event) && this.$modalCancelButton) {
				event.preventDefault();
				requestAnimationFrame(() => this.$modalCancelButton.focus());
			} else if (tabbedToNext(event) || tabbedToPrevious(event)) {
				event.preventDefault();
				requestAnimationFrame(() => this.$continueButton.focus());
			} else if (isClickedButton) {
				event.preventDefault();
				this.setModalState();
			}

			return;
		}

		if (event.target === this.$modalCancelButton) {
			if (tabbedToPrevious(event)) {
				event.preventDefault();
				requestAnimationFrame(() => this.$modalCloseButton.focus());
			}

			return;
		}

		if (event.target === this.$continueButton) {
			if (tabbedToNext(event) || tabbedToPrevious(event)) {
				event.preventDefault();
				requestAnimationFrame(() => this.$modalCloseButton.focus());
			} else if (tabbedToPrevious(event) && this.$modalCancelButton) {
				event.preventDefault();
				requestAnimationFrame(() => this.$modalCancelButton.focus());
			}
		}
	}

	setModalState() {
		if (!this.$modal.classList.contains('show')) {
			this.$modal.classList.add('show');
			this.$modal.style.display = 'block';
			this.body.classList.add('modal-open');
			requestAnimationFrame(() => this.$modalCloseButton.focus());
		} else {
			this.$modal.classList.remove('show');
			this.$modal.style.display = 'none';
			this.body.classList.remove('modal-open');
			requestAnimationFrame(() => this.currentModalTriggerButton.focus());
		}
	}

	assignLinkToButton(item) {
		const triggerButtonHref = item.href || item.dataset.href;

		if (triggerButtonHref) {
			this.$continueButton.setAttribute('href', triggerButtonHref);
		}
	}

	changeAriaAttributeForSafari() {
		if (this.$modal) {
			const getAriaValue = this.$modal.getAttribute('aria-describedby');
			this.$modal.removeAttribute('aria-describedby');
			this.$modal.setAttribute('aria-labelledby', getAriaValue);
		}
	}
}

UHCJS.registerComponent({
	name: 'siteExitModal',
	selector: '.siteExitModal',
	constr: Modal,
});

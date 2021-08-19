import UHCJS from '../../uhcjs';
import constants from '../../utils/constants';
import { throttle, getScrollbarWidth, isBreakpointMatched } from '../../utils/helpers';
import { hasClass } from '../../utils/dom';

const selectors = {
	rightAlignedImage: '.aem-image--right',
	topRightAlignedImage: '.aem-image--top-right',
	bottomRightAlignedImage: '.aem-image--bottom-right',
};

const throttleTime = 16;
const basePositionShiftRight = -16;
const bodyBorderWidth = 15;

class Image {
	constructor(elem) {
		this.init(elem);
	}

	init(elem) {
		this.bindElements(elem);
		this.attachEvents();
		this.adjustPosition();
	}

	bindElements(elem) {
		this.$element = elem;
	}

	attachEvents() {
		window.addEventListener('resize', throttle(this.adjustPosition.bind(this), throttleTime));
	}

	adjustPosition() {
		const scrollbarWidth = getScrollbarWidth();
		const isAdjustmentNeeded = scrollbarWidth > 0 && isBreakpointMatched('xlarge');
		const rightAlignedClassName = selectors.rightAlignedImage.substring(1);
		const isRightAligned = hasClass(this.$element, rightAlignedClassName);

		if (isAdjustmentNeeded) {
			// Formulas taken from CSS file, adjusted to include scrollbar
			let positionRight = isRightAligned ? -32 : -(window.innerWidth / 2 - 632);

			positionRight += scrollbarWidth / 2;
			positionRight = Math.min(positionRight, basePositionShiftRight);

			this.$element.style.right = `${Math.floor(positionRight)}px`;

			if (isRightAligned) {
				this.checkPosition(positionRight);
			}
		} else {
			this.$element.style.right = '';
		}
	}

	checkPosition(shiftRight) {
		const documentWidth = document.documentElement.clientWidth;
		const imageMisalignedDocumentMaxWidth = constants.breakpoints.xlarge + 16;

		if (documentWidth <= imageMisalignedDocumentMaxWidth) {
			const clientRect = this.$element.getBoundingClientRect();
			const imageMisalignmentValue = Math.ceil(documentWidth - bodyBorderWidth - clientRect.right);

			this.$element.style.right = `${shiftRight - imageMisalignmentValue}px`;
		}
	}
}

UHCJS.registerComponent({
	name: 'image',
	selector: Object.values(selectors).join(','),
	constr: Image,
});

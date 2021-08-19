
import UHCJS from '../../uhcjs';

const selectors = {
	healthBenefits: '.healthbenefits',
	visionBenefits: '#federal-health-plan-benefits-visionBenefits',
	visionTabPanel: '#vision-tabpanel',
	visionTab: '#vision-tab',
};

class HealthBenefitsTabs {
	constructor(elem) {
		this.init(elem);
	}

	init(elem) {
		this.bindElements(elem);
		this.tryToRemoveVisionTab(elem);
	}

	bindElements(elem) {
		this.$visionBenefits = elem.querySelector(selectors.visionBenefits);
	}

	tryToRemoveVisionTab(elem) {
		if (this.$visionBenefits) {
			const data = JSON.parse(this.$visionBenefits.dataset.benefits);
			if (data && data.planBenefitsSummary && data.planBenefitsSummary.showBenefit5 === 'false') {
				elem.querySelector(selectors.visionTab).remove();
				elem.querySelector(selectors.visionTabPanel).remove();
			}
		}
	}
}

UHCJS.registerComponent({
	name: 'healthBenefitsTabs',
	selector: '.tabs',
	constr: HealthBenefitsTabs,
});

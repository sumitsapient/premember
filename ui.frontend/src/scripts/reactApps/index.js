import React from 'react';
import ReactDOM from 'react-dom';

import reactComp from './componentRegistry';
import PlanCardsList from './planCardsList/planCardsList.component';
import CompareTable from './compareTable/compareTable.component';
import ProviderSearch from './providerSearch/providerSearch.component';
import RatesTable from './ratesComponent/ratesTable.component';
import MedicareBenefits from './medicareBenefits/medicareBenefits.component';
import PlansOverview from './plansOverview/plansOverview.component';
import ZipSelector from './plansOverview/planFilters/zipSelector.component';
import BenefitsList from './medicalPlanBenefitsList/benefitsList.component';
import DentalBenefits from './dentalPlanBenefits/dentalBenefits.component';
import PlanTools from './planTools/planTools.component';
import VisionBenefits from './visionBenefits/visionBenefits.component';
import FederalCompareTable from './federalCompareTable/federalCompareTable.component';
import BenefitsOfficers from './benefitsOfficers/benefitsOfficers.component';
import HealthPlansEnrollment from './healthPlansEnrollment/healthPlansEnrollment.component';
import AnchorNavigation from './anchorNavigation/anchorNavigation.component';

import { getCookie } from '../utils/helpers';
import { getZipCookie } from '../utils/zip';

if (reactComp.planCardsList) {
	ReactDOM.render(<PlanCardsList />, reactComp.planCardsList);
}

if (reactComp.compareTable) {
	const dataPlans = JSON.parse(reactComp.compareTable.dataset.plan);
	ReactDOM.render(<CompareTable data={ dataPlans } />, reactComp.compareTable);
}

if (reactComp.providerSearch) {
	const { providersByPlans } = JSON.parse(reactComp.providerSearch.dataset.providers);
	ReactDOM.render(<ProviderSearch data={ providersByPlans } />, reactComp.providerSearch);
}

if (reactComp.plansOverview) {
	const zipCode = getZipCookie();
	const dataPlans = JSON.parse(reactComp.plansOverview.dataset.plans);
	const dataPlansUrl = reactComp.plansOverview.dataset.plansUrl;
	const dataPlanType = reactComp.plansOverview.dataset.planType;

	ReactDOM.render(
		<PlansOverview
			data={ dataPlans }
			plansUrl={ dataPlansUrl }
			planType={ dataPlanType }
			zipCode={ zipCode }
		/>, reactComp.plansOverview,
	);
}

if (reactComp.zipSelector) {
	const zipCode = getZipCookie();
	ReactDOM.render(<ZipSelector zipCode={ zipCode } />, reactComp.zipSelector);
}

if (reactComp.ratesTable) {
	const ratesData = JSON.parse(reactComp.ratesTable.dataset.plan);
	const { planType } = reactComp.ratesTable.dataset;
	let regionCode = null;
	// get corresponding region cookie
	switch (planType) {
		case 'health':
			regionCode = getCookie('federal-region-code');
			break;
		case 'dental':
			regionCode = getCookie('federal-dental-region-code');
			break;
		default:
	}

	ReactDOM.render(
		<RatesTable
			data={ ratesData }
			regionCode={ regionCode }
			type={ planType }
		/>, reactComp.ratesTable,
	);
}

if (reactComp.medicareBenefits.length > 0) {
	reactComp.medicareBenefits.forEach((benefit) => {
		const data = JSON.parse(benefit.dataset.planBenefits);

		ReactDOM.render(
			<MedicareBenefits
				data={ data }
			/>,
			benefit,
		);
	});
}

if (reactComp.allBenefits.length > 0) {
// loop through all instances of health benefits component on the page and instantiate react with the attached data
	reactComp.allBenefits.forEach((item) => {
		const regionCode = getCookie('federal-region-code');
		const planBenefits = JSON.parse(item.dataset.benefits);
		ReactDOM.render(
			<BenefitsList
				data={ planBenefits }
				regionCode={ regionCode }
			/>, item,
		);
	});
}

if (reactComp.dentalPlanBenefits.length > 0) {
	// loop through all instances of dental benefits component on the page and instantiate react with the attached data
	reactComp.dentalPlanBenefits.forEach((item) => {
		const regionCode = getCookie('federal-region-code');
		const planBenefits = JSON.parse(item.dataset.benefits);
		ReactDOM.render(
			<DentalBenefits
				data={ planBenefits }
				regionCode={ regionCode }
			/>, item,
		);
	});
}

if (reactComp.planTools.length > 0) {
	reactComp.planTools.forEach((item) => {
		ReactDOM.render(
			<PlanTools />,
			item,
		);
	});
}

if (reactComp.visionBenefits) {
	const regionCode = getCookie('federal-region-code');
	const planBenefits = JSON.parse(reactComp.visionBenefits.dataset.benefits);
	ReactDOM.render(
		<VisionBenefits
			data={ planBenefits }
			regionCode={ regionCode }
		/>, reactComp.visionBenefits,
	);
}

if (reactComp.compareTableFederal.length > 0) {
	// loop through all instances of dental benefits component on the page and instantiate react with the attached data
	reactComp.compareTableFederal.forEach((item) => {
		const regionCode = getCookie('federal-region-code');
		const data = JSON.parse([item.dataset.plans]);
		const { type } = item.dataset;
		let planInfo;
		if (type === 'HEALTH') {
			planInfo = data.healthPlansList;
		}
		if (type === 'DENTAL') {
			planInfo = data.dentalPlansList;
		}
		if (type === 'VISION') {
			planInfo = data.visionPlansList;
		}
		ReactDOM.render(
			<FederalCompareTable
				labelsData={ data.labels }
				planInfoData={ planInfo }
				regionCode={ regionCode }
				planType={ type }
			/>, item,
		);
	});
}

if (reactComp.benefitsOfficers) {
	const data = JSON.parse(reactComp.benefitsOfficers.dataset.plan);

	ReactDOM.render(
		<BenefitsOfficers data={ data } />,
		reactComp.benefitsOfficers,
	);
}

if (reactComp.healthPlansEnrollment) {
	const data = JSON.parse(reactComp.healthPlansEnrollment.dataset.plans);

	ReactDOM.render(
		<HealthPlansEnrollment data={ data } />,
		reactComp.healthPlansEnrollment,
	);
}

if (reactComp.anchorNavigation) {
	ReactDOM.render(
		<AnchorNavigation />,
		reactComp.anchorNavigation,
	);
}

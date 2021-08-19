import React, { Component } from 'react';
import PropTypes from 'prop-types';

import MedicalBenefits from './tables/medicalBenefits.component';
import PrescriptionBenefits from './tables/prescriptionlBenefits.component';
import DentalBenefits from './tables/dentalBenefits.component';
import WellnessBenefits from './tables/wellnessBenefits.component';
import VisionBenefits from './tables/visionBenefits.component';

class BenefitsList extends Component {
	render() {
		const { data } = this.props;
		const renderBenefits = (type) => {
			switch (type) {
				case 'medicalBenefits':
					return (
						<MedicalBenefits
							data={ data }
						/>
					);
				case 'prescriptionBenefits':
					return (
						<PrescriptionBenefits
							data={ data }
						/>
					);
				case 'behavioralWellnessHealth':
					return (
						<WellnessBenefits
							data={ data }
						/>
					);
				case 'dentalBenefits':
					return (
						<DentalBenefits
							data={ data }
						/>
					);
				case 'visionBenefits':
					return (
						<VisionBenefits
							data={ data }
						/>
					);
				default:
					return '';
			}
		};
		return (
			<div className="c-benefits-list u-bg-white u-h-100%">
				{ renderBenefits(data.benefitKey) }
			</div>
		);
	}
}

BenefitsList.propTypes = {
	data: PropTypes.shape().isRequired,
};

export default BenefitsList;

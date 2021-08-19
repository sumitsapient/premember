import React from 'react';
import PropTypes from 'prop-types';

const BenefitsOfficers = ({ data }) => (
	<div className="o-grid u-f-justify-center">
		{
			data.map((officer, index) => (
				<div
					className="o-grid__item-12 o-grid__item-6@sm o-grid__item-4@lg u-m-b"
					key={ index }
				>
					<div
						className="c-card u-bg-gray"
						style={ {
							boxShadow: 'none',
						} }
					>
						{/* Image */}
						{
							officer.photoFilePath && (
								<div className="c-card__img">
									<div className="o-ar o-ar--16:9">
										<img
											src={ officer.photoFilePath }
											alt={ officer.name }
											className="o-ar__item"
										/>
									</div>
								</div>
							)
						}

						<div className="c-card__header u-text-center u-p-vert u-m-b-0">
							{/* Name */}
							{
								officer.name && (
									<div className="unity-type-h3 u-text-serif">{ officer.name }</div>
								)
							}

							{/* Description */}
							{
								officer.description && (
									<div>{ officer.description }</div>
								)
							}
						</div>

						<div className="c-card__body u-text-center u-p-b">
							{/* Email */}
							{
								officer.email && (
									<a
										href={ `mailto:${officer.email}` }
										className="u-d-block u-text-bold u-m-b-xs"
									>
										{ officer.email }
									</a>
								)
							}

							{/* Phone number */}
							{
								officer.phoneNumber && (
									<a
										href={ `tel:${officer.phoneNumber}` }
										className="u-d-block u-text-bold"
									>
										{ officer.phoneNumber }
									</a>
								)
							}
						</div>
					</div>
				</div>
			))
		}
	</div>
);

BenefitsOfficers.propTypes = {
	data: PropTypes.arrayOf(PropTypes.exact({
		description: PropTypes.string,
		email: PropTypes.string,
		name: PropTypes.string,
		phoneNumber: PropTypes.string,
		photoFilePath: PropTypes.string,
	})).isRequired,
};

export default BenefitsOfficers;

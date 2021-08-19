import React from 'react';
import PropTypes from 'prop-types';


const DentalBenefits = ({ data }) => {
	const { benefits } = data;

	return (
		<div className="dentalBenefits u-color-blue c-card c-card--bordered u-h-100%">
			<div className="c-card__body u-d-flex u-f-justify-center">
				{
					benefits.benefits.dentalDiscountDentalServices === 'on' && (
						<div className="dental-benefit">
							<div className="c-icon-box c-icon-box--large u-bg-blue-accent c-icon-box--circle u-color-blue">
								<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 36 40"><path d="M19.2 40v-3.7c4-.4 6.3-1.4 8.3-3.6 1.6-1.7 2.4-3.8 2.4-6.2 0-3-1.1-5.4-3.1-7-1.7-1.4-3.5-2.2-7.6-3.2V8.5c1.9 0 3.5 1.9 3.6 4.2h6.1c-.3-5.4-3.8-8.7-9.7-9.4V0h-2.5v3.4c-3.3.4-5 1-6.8 2.4-2.2 1.7-3.2 4.1-3.2 7.1 0 4.7 2.5 7.3 8.4 8.8.3.1.6.1.7.2.4.1.6.2.6.2.1 0 .1 0 .3.1v8.9c-2.9-.6-4.6-2.7-4.6-5.7v-.1H6c.6 6.8 4.2 10.4 10.7 11V40h2.5zm-2.5-24.3c-3-.8-3.9-1.6-3.9-3.5 0-2.2 1.5-3.6 3.9-3.7v7.2zm2.5 15.5V23c3.6 1 4.6 1.9 4.6 3.9.1 2.3-1.5 3.7-4.6 4.3z" fill="#002677" /></svg>
							</div>
							<p className="unity-type-h3">Discount Dental Services</p>
							<p>{benefits.dentalDiscountDentalServicesNotes}</p>
						</div>
					)
				}
				{
					benefits.benefits.dentalPPOPreventiveDentalServices === 'on' && (
						<div className="dental-benefit">
							<div className="c-icon-box c-icon-box--large u-bg-blue-accent c-icon-box--circle u-color-blue">
								<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 36 40"><path d="M18 22c.6 0 1.2.1 1.7.4 1.3.6 2.3 1.8 2.6 3.4l2.2 11.7c.1.3.3.4.5.4.1 0 .3 0 .4-.1 4.3-3.9 3.5-12 3.1-14.4-.1-1 .1-2 .6-2.8l3-4.7s0-.1.1-.1c1.4-1.8 2-4.1 1.7-6.4-.5-3.4-3.2-6.2-6.5-6.8-.5-.1-.9-.1-1.4-.1-2.4 0-4.7 1-6.2 2.9-.4.5-1 .7-1.6.7-.6 0-1.2-.3-1.5-.8-1.5-1.9-3.8-3-6.2-3-.5 0-.9 0-1.4.1-3.4.4-6.2 3.2-6.7 6.6-.4 2.3.2 4.6 1.5 6.4v.1l3 4.8c.5.9.8 1.9.6 2.9-.4 2.4-1.3 10.5 2.9 14.4.1.1.2.2.4.2s.4-.1.5-.4l2.3-11.6c.3-1.8 1.6-3.2 3.2-3.6.4-.2.8-.2 1.2-.2zm7 18.1c-1.3 0-2.4-.9-2.6-2.2l-2.2-11.7c-.2-.8-.6-1.5-1.3-1.8-.3-.1-.6-.2-.9-.2-.2 0-.4 0-.6.1-.8.2-1.4.9-1.6 1.9l-2.3 11.6c-.3 1.3-1.4 2.2-2.7 2.2-.7 0-1.3-.3-1.8-.7-2.1-2-3.4-5-3.8-8.9-.3-3.1 0-6 .3-7.5.1-.5 0-.9-.3-1.3l-3-4.7C.5 14.6-.2 11.7.3 8.8 1 4.5 4.5.9 8.7.2c.5-.2 1.1-.3 1.7-.3 3 0 5.8 1.4 7.7 3.7 2-2.3 4.8-3.6 7.8-3.6.6 0 1.2.1 1.8.2 4.2.8 7.6 4.4 8.3 8.7.4 2.9-.3 5.8-2.1 8.1l-3 4.7c-.2.4-.3.8-.3 1.3.2 1.5.5 4.4.2 7.5-.5 3.9-1.8 6.9-4 8.8-.5.5-1.2.8-1.8.8z" fillRule="evenodd" clipRule="evenodd" fill="#002677" /></svg>
							</div>
							<p className="unity-type-h3">Preventive Dental Services</p>
							<p>{benefits.dentalPPOPreventiveDentalServicesNotes}</p>
						</div>
					)
				}
				{
					typeof benefits.general.dentalCalendarYearMaximumPerMember !== 'undefined'
					&& benefits.general.dentalCalendarYearMaximumPerMember.length > 0 && (
						<div className="dental-benefit">
							<div className="c-icon-box c-icon-box--large u-bg-blue-accent c-icon-box--circle u-color-blue">
								<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 40 40"><path d="M9 6v3c0 .6.4 1 1 1s1-.4 1-1V6h16v3c0 .6.4 1 1 1s1-.4 1-1V6h7v6H2V6h7zm15 29H2V14h34v3c0 .6.4 1 1 1s1-.4 1-1V5c0-.6-.4-1-1-1h-8V1c0-.6-.4-1-1-1s-1 .4-1 1v3H11V1c0-.6-.4-1-1-1S9 .4 9 1v3H1c-.6 0-1 .4-1 1v31c0 .6.4 1 1 1h23c.6 0 1-.4 1-1s-.4-1-1-1zm10 .2v-4.5c2 .6 2.6 1.1 2.6 2.1 0 1.3-.9 2.1-2.6 2.4zm-1.4-8.4c-1.7-.4-2.1-.9-2.1-1.9 0-1.2.8-1.9 2.1-2v3.9zm5.7 2c-1-.8-2-1.2-4.2-1.7v-4.3c1 0 2 1 2 2.3h3.4c-.2-2.9-2.1-4.8-5.4-5.1v-1.8h-1.4V20c-1.8.2-2.8.5-3.8 1.3-1.2.9-1.8 2.2-1.8 3.9 0 2.6 1.4 4 4.7 4.8.2.1.3.1.4.1.2.1.3.1.3.1.1 0 .1 0 .2.1v4.9c-1.6-.3-2.6-1.5-2.6-3.1V32h-3.4c.4 3.7 2.3 5.7 6 6v2H34v-2c2.3-.2 3.5-.7 4.6-1.9.9-.9 1.4-2.1 1.4-3.4 0-1.7-.6-3-1.7-3.9z" fillRule="evenodd" clipRule="evenodd" fill="#002677" /></svg>
							</div>
							<p className="unity-type-h3">{`$${benefits.general.dentalCalendarYearMaximumPerMember} annual maximum`}</p>
						</div>
					)
				}
			</div>
		</div>
	);
};

DentalBenefits.propTypes = {
	data: PropTypes.shape().isRequired,
};

export default DentalBenefits;

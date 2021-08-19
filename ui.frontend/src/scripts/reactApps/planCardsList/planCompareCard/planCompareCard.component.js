import React from 'react';
import PropTypes from 'prop-types';

const PlanCompareCard = ({
	title,
	linkText,
	linkUrl,
	blueBg,
}) => (
	<div className="card plan-compare-card">
		<div className={ `c-card aem-component c-card--teal-blue${blueBg ? ' u-bg-blue-light' : ''}` }>
			<div className="c-card__header">
				<h2 className="c-card__title">
					{ title }
				</h2>
			</div>
			<div className="c-card__footer">
				<div className="aem-buttons">
					<div className="button">
						<a
							className="c-button js-track-event c-button--primary"
							href={ linkUrl }
							data-event-type="button"
							data-event-name="link_to_plan_compare"
						>
							{ linkText }
						</a>
					</div>
				</div>
			</div>
		</div>
	</div>
);

PlanCompareCard.defaultProps = {
	blueBg: false,
};


PlanCompareCard.propTypes = {
	title: PropTypes.string.isRequired,
	linkText: PropTypes.string.isRequired,
	linkUrl: PropTypes.string.isRequired,
	blueBg: PropTypes.bool,
};

export default PlanCompareCard;

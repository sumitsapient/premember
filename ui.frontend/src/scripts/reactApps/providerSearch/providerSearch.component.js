import React, { useState } from 'react';
import PropTypes from 'prop-types';
import RichText from '../sharedComponents/richText.component';
import ProviderCard from './providerCard/providerCard.component';
import constants from '../../utils/constants';
import { normalizeText } from '../../utils/helpers';

const ProviderSearch = ({ data }) => {
	const [tabElements] = useState([]);
	const [activeTab, setActiveTab] = useState(0);

	const getPreviousTabIndex = () => (activeTab === 0 ? 0 : activeTab - 1);
	const getNextTabIndex = () => (activeTab === data.length - 1 ? data.length - 1 : activeTab + 1);

	const getTabEventName = title => normalizeText(title);

	const handleKeyDown = (event) => {
		const { keyCode } = event;
		const isPreviousKey = keyCode === constants.keyCodes.arrowLeftKey || keyCode === constants.keyCodes.arrowUpKey;
		const isNextKey = keyCode === constants.keyCodes.arrowRightKey || keyCode === constants.keyCodes.arrowDownKey;
		const changeTab = isPreviousKey || isNextKey;
		let nextTabIndex;

		if (isPreviousKey) {
			nextTabIndex = getPreviousTabIndex();
		} else if (isNextKey) {
			nextTabIndex = getNextTabIndex();
		}

		if (changeTab) {
			event.preventDefault();

			setActiveTab(nextTabIndex);
			window.requestAnimationFrame(() => tabElements[nextTabIndex].focus());
		}
	};

	if (!data.length) {
		return 'No providers search options defined for the employer.';
	}

	return (
		<div className="cmp-tabs">
			{ data.length > 1
				&& (
					<ol role="tablist" className="cmp-tabs__tablist" aria-multiselectable="false">
						{
							data.map((provider, i) => (
								<li
									id={ `${getTabEventName(provider.tabTitle)}-tab` }
									key={ getTabEventName(provider.tabTitle) }
									ref={ (ref) => {
										tabElements[i] = ref;
									} }
									role="tab"
									className={ `cmp-tabs__tab ${activeTab === i ? 'cmp-tabs__tab--active' : ''}` }
									aria-controls={ `${getTabEventName(provider.tabTitle)}-tabpanel` }
									tabIndex={ activeTab === i ? 0 : -1 }
									aria-selected={ activeTab === i }
									data-event-type="tab"
									data-event-name={ getTabEventName(provider.tabTitle) }
									onClick={ () => setActiveTab(i) }
									onKeyDown={ handleKeyDown }
								>
									{provider.tabTitle}
								</li>
							))
						}
					</ol>
				)
			}
			{
				data.map((provider, i) => (
					<div
						id={ `${getTabEventName(provider.tabTitle)}-tabpanel` }
						key={ normalizeText(provider.tabTitle) }
						role="tabpanel"
						aria-labelledby={ `${getTabEventName(provider.tabTitle)}-tab` }
						tabIndex="0"
						className={ `cmp-tabs__tabpanel ${activeTab === i ? 'cmp-tabs__tabpanel--active' : ''}` }
						aria-hidden={ activeTab !== i ? true : null }
					>
						<div className="o-grid o-grid--aem-container">
							<div className="o-grid__item o-grid__item-12 u-m-b-sm">
								<RichText
									className="unity-type-large@md"
									content={ provider.tabText }
									singleTab={ data.length === 1 }
								/>
							</div>
							{ provider.byOptions.map(option => (
								<ProviderCard
									key={ option.planName }
									option={ option }
								/>
							)) }
						</div>
					</div>
				))
			}
		</div>
	);
};

ProviderSearch.propTypes = {
	data: PropTypes.arrayOf(
		PropTypes.shape({
			tabTitle: PropTypes.string.isRequired,
			tabText: PropTypes.string.isRequired,
			byOptions: PropTypes.arrayOf(
				PropTypes.shape({
					planName: PropTypes.string.isRequired,
					providerLink: PropTypes.shape(),
					hasStateSpecificProvider: PropTypes.bool.isRequired,
					stateSpecificProvider: PropTypes.shape(),
				}),
			),
		}),
	).isRequired,
};

export default ProviderSearch;

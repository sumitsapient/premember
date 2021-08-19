// Stylesheets
import '../styles/main.scss';

// UHCJS api for vanilla Javascript components
import UHCJS from './uhcjs';
import './components/components';

// React Components
import './reactApps/index';

window.addEventListener('load', () => {
	UHCJS.init();
}, false);

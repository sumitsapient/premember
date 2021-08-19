(function (window, $, channel, Granite, Coral) {
    "use strict";

    // class of the edit dialog content
    const CLASS_EDIT_DIALOG = "cmp-contentfragment__editor";

    // class of the DOM element containing fragment path and variation selector.
    const CLASS_CONTENT = ".generalEmployerPlanTab";

    const FRAGMENT_PATH_DOM_SELECTOR = "foundation-autocomplete[class='coral-Form-field']";

    const VARIATION_NAME_SELECTOR = "[name='./variationName']";
    const VARIATION_NAME_DOM_SELECTOR = ".coral-Form-field.coral3-Select";
    const VARIATION_NAME_INPUT_SELECTOR = "input" + VARIATION_NAME_SELECTOR;

    let contentController;

    /**
     * A class which encapsulates the state and logic related to fragment path and variation name selector.
     */
    let ContentController = function (content) {
        this.content = content;
        this.fetchedState = null;

        this.fragmentPathDom = this.content.querySelector(FRAGMENT_PATH_DOM_SELECTOR);
        this.currentFragmentPath = this.fragmentPathDom.value;

        this.variationNameDom = this.content.querySelector(VARIATION_NAME_DOM_SELECTOR);
        this.variationDialogPath = this.variationNameDom.dataset.fieldPath;

        if (!this.currentFragmentPath) {
            this.disableVariationSelector();
        }

        $(this.fragmentPathDom).on("foundation-field-change", onFragmentPathChange);
    };

    ContentController.prototype.disableVariationSelector = function () {
        if (this.variationNameDom) {
            this.variationNameDom.setAttribute("disabled", "");
        }
    };

    ContentController.prototype.enableVariationSelector = function () {
        if (this.variationNameDom) {
            this.variationNameDom.removeAttribute("disabled");
        }
    };

    ContentController.prototype.flushVariations = function () {
        let variationsSelector = this.content.querySelector(VARIATION_NAME_INPUT_SELECTOR);
        variationsSelector.setAttribute("value", "");
    };

    /**
     * Creates an http request object for retrieving fragment's variation names and returns it.
     *
     * @returns {Object} the resulting variation names
     */
    ContentController.prototype.prepareRequest = function () {
        const data = {
            fragmentPath: this.currentFragmentPath
        };
        const url = Granite.HTTP.externalize(this.variationDialogPath) + ".html";
        const request = $.get({
            url: url,
            data: data
        });
        return request;
    };

    /**
     * Retrieves the html for variation names and updates DOM for variation element.
     */
    ContentController.prototype.fetchVariations = function () {
        const variationNameRequest = this.prepareRequest();
        let self = this;
        // wait for requests to load
        $.when(variationNameRequest).then(function (result) {
            const newVariationNameDom = $(result).find(VARIATION_NAME_SELECTOR)[0];
            // get the fields from the resulting markup and create a test state
            Coral.commons.ready(newVariationNameDom, function () {
                self.fetchedState = {
                    variationNameDom: newVariationNameDom
                };

                if (self.fetchedState.variationNameDom) {
                    self.saveFetchedState();
                }
            });
        });
    };

    /**
     * Replace the current state with the values present in fetchedState and discard the fetchedState thereafter.
     */
    ContentController.prototype.saveFetchedState = function () {
        this._updateVariationDOM(this.fetchedState.variationNameDom);
        this.discardFetchedState();
        this.enableVariationSelector();
    };

    /**
     * Updates dom of variation name select dropdown.
     *
     * @param {HTMLElement} variationDom - dom for variation name dropdown
     */
    ContentController.prototype._updateVariationDOM = function (variationDom) {
        this.variationNameDom.parentNode.replaceChild(variationDom, this.variationNameDom);
        this.variationNameDom = variationDom;
    };

    ContentController.prototype.discardFetchedState = function () {
        this.fetchedState = null;
    };

    /**
     * Fetches variants for current fragment path, if present. Otherwise flushes and disables variation selector.
     */
    ContentController.prototype.fetchAndSetVariations = function () {
        this.fetchVariations();
        if (this.currentFragmentPath === null || this.currentFragmentPath.length === 0) {
            this.flushVariations();
            this.disableVariationSelector();
        }
    };

    /**
     * Executes after the fragment path has changed and fetches variation names for given content fragment.
     */
    function onFragmentPathChange() {
        contentController.currentFragmentPath = contentController.fragmentPathDom.value;
        contentController.fetchAndSetVariations();
    }

    function initialize(dialog) {
        // get parent DOM element containing fragmentPath and variationName elements
        const content = dialog.querySelector(CLASS_CONTENT);
        if (content !== null) {
            contentController = new ContentController(content);
        } else {
            throw "Could not find DOM element of class: " + CLASS_CONTENT + " that contains fragment path and its variation."
        }
    }

    /**
     * Initializes the dialog after it has loaded.
     */
    channel.on("foundation-contentloaded", function (e) {
        if (e.target.getElementsByClassName(CLASS_EDIT_DIALOG).length > 0) {
            Coral.commons.ready(e.target, function (dialog) {
                initialize(dialog);
            });
        }
    });

})(window, jQuery, jQuery(document), Granite, Coral);
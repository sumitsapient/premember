(function ($, $document) {

	"use strict";

	// **************************************************************************
	// ** CONSTANTS
	// **************************************************************************

	const EVENT_CLICK = "click";

	const ATTRIBUTE_MULTIFIELD_ADD = "coral-multifield-add";
	const ATTRIBUTE_MULTIFIELD_MAX_ITEMS = "data-max-items";

	const SELECTOR_MULTIFIELD_ADD_ITEM_BUTTON = `button[${ATTRIBUTE_MULTIFIELD_ADD}]`;
	const SELECTOR_MULTIFIELD_ITEM = "coral-multifield-item";

	// **************************************************************************
	// ** CLASSES
	// **************************************************************************

	class Listener {

		constructor(selector, event, handler) {
			this.selector = selector;
			this.event = event;
			this.handler = handler;
		}

		getSelector() {
			return this.selector;
		}

		getEvent() {
			return this.event;
		}

		getHandler() {
			return this.handler;
		}
	}

	// **************************************************************************
	// ** LISTENER HANDLERS
	// **************************************************************************

	function multifieldMaxItemsHandler(context) {

		const $field = $(context).parent();
		const maxSize = $field.attr(ATTRIBUTE_MULTIFIELD_MAX_ITEMS);

		if (maxSize) {

			const ui = $(window).adaptTo("foundation-ui");
			const currentSize = $field.children(SELECTOR_MULTIFIELD_ITEM).length;

			if (currentSize >= maxSize) {
				ui.alert("Warning", "Maximum  " + maxSize + " items are allowed!", "notice");
				return false;
			}
		}
	}

	// **************************************************************************
	// ** LISTENERS
	// **************************************************************************

	const listeners = [];
	listeners.push(new Listener(SELECTOR_MULTIFIELD_ADD_ITEM_BUTTON, EVENT_CLICK, multifieldMaxItemsHandler));

	// **************************************************************************
	// ** REGISTER LOGIC
	// **************************************************************************

	$document.on("foundation-contentloaded", function () {
    $.each(listeners, function (index, listener) {
      $(listener.getSelector()).on(listener.getEvent(), function () {
        return listener.getHandler()(this);
      });
    });
  });

})($, $(document));
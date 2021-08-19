(function (window, $, channel, Granite, Coral) {
    "use strict";

    const SELECTOR_PLAN_BENEFIT_TYPE_FIELD = ".plan-benefit-type-select";
    const SELECTOR_DIALOG_FORM = ".foundation-form";
    const PLAN_TYPES = {
        "health": ["medical", "prescription", "dental", "wellness", "vision"],
        "medicare": ["medical", "pharmacy", "extras"],
        "vision": ["general"],
        "dental": ["general"]
    };

    function loadBenefitsTypes(selectElement, storedBenefitKey) {
        let selectedType = detectPlanType(window.location.href);
        if (!selectedType) {
            throw "Plan type couldn't be detected. Invalid plan type";
        }
        let benefitTypes = PLAN_TYPES[selectedType];
        benefitTypes.forEach(benefitType => {
            let item = convertToSelectItem(benefitType, storedBenefitKey);
            selectElement.items.add(item);
        });
    }

    function detectPlanType(url) {
        for (const property in PLAN_TYPES) {
            let planName = property;
            if (url.includes(planName + "-plans")) {
                return planName;
            }
        }
        return "";
    }

    function convertToSelectItem(planName, storedBenefitKey) {
        return {
            value: planName,
            content: {
                textContent: planName.charAt(0).toUpperCase() + planName.slice(1)
            },
            selected: planName === storedBenefitKey
        }
    }

    function initialize(dialog) {
        const selectElement = dialog.querySelector(SELECTOR_PLAN_BENEFIT_TYPE_FIELD);
        if (selectElement == null) {
            throw "Could not find DOM element of class: " + SELECTOR_PLAN_BENEFIT_TYPE_FIELD
            + " that contains select field for benefits type."
        }
        const form = dialog.querySelector(SELECTOR_DIALOG_FORM);
        const componentPath = form.action + ".json";
        $.get(componentPath, function (data) {
            loadBenefitsTypes(selectElement, data.benefitKey);
        });
    }

    channel.on("foundation-contentloaded", function (e) {
        Coral.commons.ready(e.target, function (dialog) {
            initialize(dialog);
        });
    });

})(window, jQuery, jQuery(document), Granite, Coral);
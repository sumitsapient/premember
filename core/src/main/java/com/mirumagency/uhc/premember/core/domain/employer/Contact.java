package com.mirumagency.uhc.premember.core.domain.employer;

import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;

import java.util.Optional;

public class Contact {

    public static final String CONTACT_INFORMATION_NODE = "contactInformation";
    public static final String OPENING_DAYS_PROP = "openingDays";
    public static final String OPENING_HOURS_PROP = "openingHours";
    public static final String PHONE_PROP = "phone";
    public static final String PERSON_1_NODE = "person1";
    public static final String PERSON_2_NODE = "person2";
    public static final String CARD_TITLE = "cardTitle";
    public static final String ADDITIONAL_DETAILS = "additionalDetails";

    private final String openingDays;
    private final String openingHours;
    private final PhoneNumber phoneNumber;
    private final ContactPerson firstContactPerson;
    private final ContactPerson secondContactPerson;
    private final String cardTitle;
    private final String additionalDetails;

    public static Contact of(NiceResource employerData) {
        Optional<NiceResource> contactInformation = employerData.getChild(CONTACT_INFORMATION_NODE);
        Optional<NiceResource> firstPerson = employerData.getChild(PERSON_1_NODE);
        Optional<NiceResource> secondPerson = employerData.getChild(PERSON_2_NODE);

        return contactInformation.map(info ->
                new Contact(
                        info.getString(OPENING_DAYS_PROP),
                        info.getString(OPENING_HOURS_PROP),
                        info.getString(PHONE_PROP),
                        ContactPerson.of(firstPerson),
                        ContactPerson.of(secondPerson),
                        info.getString(CARD_TITLE),
                        info.getString(ADDITIONAL_DETAILS))
        ).orElse(empty());
    }

    private static Contact empty() {
        return new Contact();
    }

    private Contact() {
        this.openingDays = "";
        this.openingHours = "";
        this.phoneNumber = PhoneNumber.empty();
        this.firstContactPerson = ContactPerson.empty();
        this.secondContactPerson = ContactPerson.empty();
        this.cardTitle = "";
        this.additionalDetails = "";
    }

    private Contact(String openingDays,
                    String openingHours,
                    String phoneNumber,
                    ContactPerson firstContactPerson,
                    ContactPerson secondContactPerson,
                    String cardTitle,
                    String additionalDetails) {
        this.openingDays = openingDays;
        this.openingHours = openingHours;
        this.phoneNumber = PhoneNumber.of(phoneNumber);
        this.firstContactPerson = firstContactPerson;
        this.secondContactPerson = secondContactPerson;
        this.cardTitle = cardTitle;
        this.additionalDetails = additionalDetails;
    }

    public String getOpeningDays() {
        return openingDays;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public ContactPerson getFirstContactPerson() {
        return firstContactPerson;
    }

    public ContactPerson getSecondContactPerson() {
        return secondContactPerson;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public String getAdditionalDetails() {
        return additionalDetails;
    }
}

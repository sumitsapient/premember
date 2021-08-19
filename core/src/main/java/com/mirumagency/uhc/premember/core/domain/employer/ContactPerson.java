package com.mirumagency.uhc.premember.core.domain.employer;

import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;

import java.util.Optional;

import static org.apache.http.util.Asserts.notNull;

public class ContactPerson {

    private static final String PERSON_NAME = "name";
    private static final String PERSON_JOB_TITLE = "jobTitle";
    private static final String PERSON_CONTACT_TEXT = "contactText";
    private static final String PERSON_CONTACT_LINK = "contactLink";
    private static final String PERSON_PHONE = "phone";

    private final String firstAndLastName;
    private final String jobTitle;
    private final ContactLink contactLink;
    private final String contactText;
    private final PhoneNumber phoneNumber;

    public static ContactPerson empty() {
        return new ContactPerson();
    }

    public static ContactPerson of(Optional<NiceResource> person) {
        return person.map(ContactPerson::of).orElse(empty());
    }

    public static ContactPerson of(NiceResource person) {
        notNull(person, "person");
        return new ContactPerson(
                person.getString(PERSON_NAME),
                person.getString(PERSON_JOB_TITLE),
                person.getString(PERSON_CONTACT_LINK),
                person.getString(PERSON_CONTACT_TEXT),
                PhoneNumber.of(person.getString(PERSON_PHONE))
        );
    }

    private ContactPerson() {
        this.firstAndLastName = "";
        this.jobTitle = "";
        this.contactLink = ContactLink.empty();
        this.contactText = "";
        this.phoneNumber = PhoneNumber.empty();
    }

    private ContactPerson(String firstAndLastName, String jobTitle, String contactLink, String contactText, PhoneNumber phoneNumber) {
        this.firstAndLastName = firstAndLastName;
        this.jobTitle = jobTitle;
        this.contactLink = ContactLink.of(contactLink);
        this.contactText = contactText;
        this.phoneNumber = phoneNumber;
    }

    public String getFirstAndLastName() {
        return firstAndLastName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public ContactLink getContactLink() {
        return contactLink;
    }

    public String getContactText() {
        return contactText;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }
}

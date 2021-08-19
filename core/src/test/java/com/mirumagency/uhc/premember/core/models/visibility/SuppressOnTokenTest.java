package com.mirumagency.uhc.premember.core.models.visibility;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SuppressOnTokenTest {

    @Test
    public void shouldShowWhenNoValue() {
        //given
        SuppressOnToken suppressOnToken = SuppressOnToken.builder().build();
        //when
        boolean isVisible = !suppressOnToken.isSuppressed();

        //then
        assertTrue(isVisible);
    }

    @Test
    public void shouldShowBasedOnBooleanValue() {
        //given
        SuppressOnToken suppressOnToken = SuppressOnToken.builder()
                .tokenValue("false")
                .build();
        //when
        boolean isVisible = !suppressOnToken.isSuppressed();

        //then
        assertTrue(isVisible);
    }

    @Test
    public void shouldHideBasedOnBooleanValue() {
        //given
        SuppressOnToken suppressOnToken = SuppressOnToken.builder()
                .tokenValue("true")
                .build();
        //when
        boolean isVisible = !suppressOnToken.isSuppressed();

        //then
        assertFalse(isVisible);
    }

    @Test
    public void shouldShowBasedOnStringValue() {
        //given
        SuppressOnToken suppressOnToken = SuppressOnToken.builder()
                .tokenValue("some value")
                .build();
        //when
        boolean isVisible = !suppressOnToken.isSuppressed();

        //then
        assertTrue(isVisible);
    }

    @Test
    public void shouldShowBasedOnEmptyValue() {
        //given
        SuppressOnToken suppressOnToken = SuppressOnToken.builder()
                .tokenValue("")
                .build();
        //when
        boolean isVisible = !suppressOnToken.isSuppressed();

        //then
        assertTrue(isVisible);
    }

    @Test
    public void shouldHideBasedOnSpecificValue() {
        //given
        SuppressOnToken suppressOnToken = SuppressOnToken.builder()
                .tokenValue("some value")
                .specificTokenValue("some value")
                .build();
        //when
        boolean isVisible = !suppressOnToken.isSuppressed();

        //then
        assertFalse(isVisible);
    }

    @Test
    public void shouldShowBasedOnSpecificValueNotMatching() {
        //given
        SuppressOnToken suppressOnToken = SuppressOnToken.builder()
                .tokenValue("some value")
                .specificTokenValue("some other value")
                .build();
        //when
        boolean isVisible = !suppressOnToken.isSuppressed();

        //then
        assertTrue(isVisible);
    }
}
package com.mirumagency.uhc.premember.core.models.visibility;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShowOnTokenTest {

    @Test
    public void shouldShowWhenNoValue() {
        //given
        ShowOnToken showOnToken = ShowOnToken.builder().build();
        //when
        boolean isShown = showOnToken.isShown();

        //then
        assertTrue(isShown);
    }

    @Test
    public void shouldShowBasedOnBooleanValue() {
        //given
        ShowOnToken showOnToken = ShowOnToken.builder()
                .tokenValue("true")
                .build();
        //when
        boolean isShown = showOnToken.isShown();

        //then
        assertTrue(isShown);
    }

    @Test
    public void shouldHideBasedOnBooleanValue() {
        //given
        ShowOnToken showOnToken = ShowOnToken.builder()
                .tokenValue("false")
                .build();
        //when
        boolean isShown = showOnToken.isShown();

        //then
        assertFalse(isShown);
    }

    @Test
    public void shouldHideBasedOnStringValue() {
        //given
        ShowOnToken showOnToken = ShowOnToken.builder()
                .tokenValue("some value")
                .build();
        //when
        boolean isShown = showOnToken.isShown();

        //then
        assertFalse(isShown);
    }

    @Test
    public void shouldHideBasedOnEmptyValue() {
        //given
        ShowOnToken showOnToken = ShowOnToken.builder()
                .tokenValue("")
                .build();
        //when
        boolean isShown = showOnToken.isShown();

        //then
        assertFalse(isShown);
    }

    @Test
    public void shouldShowBasedOnSpecificValue() {
        //given
        ShowOnToken showOnToken = ShowOnToken.builder()
                .tokenValue("some value")
                .specificTokenValue("some value")
                .build();
        //when
        boolean isShown = showOnToken.isShown();

        //then
        assertTrue(isShown);
    }

    @Test
    public void shouldHideBasedOnSpecificValueNotMatching() {
        //given
        ShowOnToken showOnToken = ShowOnToken.builder()
                .tokenValue("some value")
                .specificTokenValue("some other value")
                .build();
        //when
        boolean isShown = showOnToken.isShown();

        //then
        assertFalse(isShown);
    }
}
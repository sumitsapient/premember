package com.mirumagency.uhc.premember.core.domain.plans.decorators.copy;

import com.google.common.collect.ImmutableMap;
import com.mirumagency.uhc.premember.core.domain.plans.PlanCopy;
import com.mirumagency.uhc.premember.core.services.SvgService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.google.common.collect.Maps.newHashMap;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SummaryTopicIconDecoratorTest {

    @Mock
    SvgService mockSvgService;

    @Test
    void shouldAccept_whenCopyContainsIconLinks() {
        // given
        when(mockSvgService.getSvgContentFromAssetPath("/first/link")).thenReturn(anyString());
        PlanCopy copy = new PlanCopy(ImmutableMap.of("summaryTopic1IconLink", "/first/link"));
        // when
        SummaryTopicIconDecorator summaryTopicIconDecorator = new SummaryTopicIconDecorator(copy, mockSvgService);
        // then
        assertTrue(summaryTopicIconDecorator.accept(copy.getData()));
    }

    @Test
    void shouldNotAccept_whenCopyIsMissingIconLinks() {
        // given
        PlanCopy copy = new PlanCopy(newHashMap());
        // when
        SummaryTopicIconDecorator summaryTopicIconDecorator = new SummaryTopicIconDecorator(copy, mockSvgService);
        // then
        assertFalse(summaryTopicIconDecorator.accept(copy.getData()));
    }

    @Test
    void shouldDecorate_whenCopyContainsIconLinks() {
        // given
        when(mockSvgService.getSvgContentFromAssetPath("/first/link")).thenReturn("firstSvgContent");
        when(mockSvgService.getSvgContentFromAssetPath("/second/link")).thenReturn("secondSvgContent");
        PlanCopy copy = new PlanCopy(ImmutableMap.of(
                "summaryTopic1IconLink", "/first/link",
                "summaryTopic2IconLink", "/second/link"));
        // when
        SummaryTopicIconDecorator summaryTopicIconDecorator = new SummaryTopicIconDecorator(copy, mockSvgService);
        // then
        assertAll(() -> {
            assertTrue(summaryTopicIconDecorator.getData().containsKey("summaryTopic1IconContent"));
            assertEquals("firstSvgContent", summaryTopicIconDecorator.getData().get("summaryTopic1IconContent"));
        });

        assertAll(() -> {
            assertTrue(summaryTopicIconDecorator.getData().containsKey("summaryTopic2IconContent"));
            assertEquals("secondSvgContent", summaryTopicIconDecorator.getData().get("summaryTopic2IconContent"));
        });
    }

    @Test
    void shouldReturnNewObject_whenDecorated() {
        // given
        when(mockSvgService.getSvgContentFromAssetPath("/first/link")).thenReturn("firstSvgContent");
        PlanCopy copy = new PlanCopy(ImmutableMap.of(
                "summaryTopic1IconLink", "/first/link"));
        // when
        SummaryTopicIconDecorator summaryTopicIconDecorator = new SummaryTopicIconDecorator(copy, mockSvgService);
        // then
        assertNotSame(copy, summaryTopicIconDecorator);
    }

    @Test
    void shouldAddNewEntries_whenDecorated() {
        // given
        when(mockSvgService.getSvgContentFromAssetPath("/first/link")).thenReturn("firstSvgContent");
        PlanCopy copy = new PlanCopy(ImmutableMap.of(
                "summaryTopic1IconLink", "/first/link"));
        // when
        SummaryTopicIconDecorator summaryTopicIconDecorator = new SummaryTopicIconDecorator(copy, mockSvgService);
        // then
        assertEquals(copy.getData().size() + 1, summaryTopicIconDecorator.getData().size());
    }

}
package com.mirumagency.uhc.premember.core.services.repository.jcr.paths;

import static java.util.Arrays.stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.mirumagency.uhc.premember.core.domain.plans.PlanType;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PlanCopyPathTest {

    @ParameterizedTest
    @MethodSource("com.mirumagency.uhc.premember.core.services.repository.jcr.paths.PlanCopyPathTest#planTypeNames")
    void shouldResolvePlanCopyPathByType(PlanType planType) {
        // given
        // when
        PlanCopyPath tested = PlanCopyPath.of(planType);
        // then
        String expected = String.format(PlanCopyPath.PLAN_COPY_PATH_FORMAT, planType.getTypeName() + "-plan-copy");
        assertEquals(expected, tested.path());
    }

    private static Stream<Arguments> planTypeNames() {
        return stream(PlanType.values())
            .map(Arguments::of);
    }

    @Test
    void shouldThrowException_whenNullProvided() {
        // given
        // when
        // then
        assertThrows(IllegalStateException.class, () -> PlanCopyPath.of(null));
    }
}
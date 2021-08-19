package com.mirumagency.uhc.premember.core.util;

import static org.apache.http.util.Asserts.notNull;

import com.mirumagency.uhc.premember.core.domain.Employer;
import com.mirumagency.uhc.premember.core.domain.plans.Plan;
import java.util.Optional;
import java.util.function.Supplier;
import javax.servlet.ServletRequest;

public class RequestCache {

    private static final String REQUEST_ATTRIBUTE_X_PREMEMBER_EMPLOYER = "X-PREMEMBER-EMPLOYER";
    private static final String REQUEST_ATTRIBUTE_X_PREMEMBER_PLAN = "X-PREMEMBER-PLAN";

    private final ServletRequest request;

    private RequestCache(ServletRequest request) {
        this.request = request;
    }

    public static RequestCache of(ServletRequest request) {
        notNull(request, "request");
        return new RequestCache(request);
    }

    public Plan getPlan(Supplier<Plan> planSupplier) {
        return getObject(planSupplier, REQUEST_ATTRIBUTE_X_PREMEMBER_PLAN);
    }

    public Employer getEmployer(Supplier<Employer> employerSupplier) {
        return getObject(employerSupplier, REQUEST_ATTRIBUTE_X_PREMEMBER_EMPLOYER);
    }

    @SuppressWarnings("unchecked")
    private <T> T getObject(Supplier<T> supplier, String cacheName) {
        T object = Optional.ofNullable(request.getAttribute(cacheName))
            .map(a -> (T) a)
            .orElseGet(supplier);
        request.setAttribute(cacheName, object);
        return object;
    }
}

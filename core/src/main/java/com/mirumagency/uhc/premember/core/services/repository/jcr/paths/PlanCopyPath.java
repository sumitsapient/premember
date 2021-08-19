package com.mirumagency.uhc.premember.core.services.repository.jcr.paths;

import static org.apache.http.util.Asserts.notNull;

import com.mirumagency.uhc.premember.core.domain.plans.PlanType;

public class PlanCopyPath extends Path {

    static final String PLAN_COPY_PATH_FORMAT = "/content/dam/premember/plans/plans-copy/%s/jcr:content/data/master";
    private static final String PLAN_COPY_NAME_FORMAT = "%s-plan-copy";

    private final String path;

    private PlanCopyPath(String planCopyPath) {
        notNull(planCopyPath, "planCopyPath");
        path = planCopyPath;
    }

    public static PlanCopyPath of(PlanType planType) {
        notNull(planType, "planType");
        return new PlanCopyPath(resolveCopyPath(planType));
    }

    private static String resolveCopyPath(PlanType planType) {
        return String.format(PLAN_COPY_PATH_FORMAT, resolveCopyName(planType));
    }

    private static String resolveCopyName(PlanType planType) {
        return String.format(PLAN_COPY_NAME_FORMAT, planType.getTypeName());
    }

    @Override
    public String path() {
        return path;
    }
}

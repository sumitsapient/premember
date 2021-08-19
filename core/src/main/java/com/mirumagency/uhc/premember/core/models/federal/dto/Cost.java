package com.mirumagency.uhc.premember.core.models.federal.dto;

import lombok.Getter;

@Getter
public class Cost {
	private final Double individual;
	private final Double family;

	public Cost(Double individual, Double family) {
		this.individual = individual;
		this.family = family;
	}
}

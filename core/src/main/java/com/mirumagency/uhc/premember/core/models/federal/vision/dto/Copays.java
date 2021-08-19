package com.mirumagency.uhc.premember.core.models.federal.vision.dto;

import lombok.Getter;

@Getter
public class Copays {
	private final Double annualExams;
	private final Double eyeGlasses;

	public Copays(Double annualExams, Double eyeGlasses) {
		this.annualExams = annualExams;
		this.eyeGlasses = eyeGlasses;
	}
}

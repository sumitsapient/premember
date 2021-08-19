package com.mirumagency.uhc.premember.core.domain.federal;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class PlanPDFFiles {

    private String sectionTitle;
    private List<PDFFileDTO> pdfFiles;

    @Builder
    @Getter
    @Setter
    public static class PDFFileDTO {

        private String path;
        private String title;
    }
}

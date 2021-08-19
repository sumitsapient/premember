package com.mirumagency.uhc.premember.core.services.migration;

import lombok.Getter;
import lombok.Setter;
import org.apache.poi.xssf.usermodel.XSSFSheet;

@Getter
@Setter
public class PremiumRateSource {
    private String filePath;
    private String planName;
    private XSSFSheet sheet;
}

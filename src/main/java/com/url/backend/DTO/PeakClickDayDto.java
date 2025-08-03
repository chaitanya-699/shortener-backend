package com.url.backend.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PeakClickDayDto {
    private String date;
    private int clicks;
}

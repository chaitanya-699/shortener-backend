package com.url.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnalyticsDto {
    private long totalClicks;
    private long uniqueClicks;
    private long clicksToday;
    private long clicksThisWeek;
    private long clicksThisMonth;
    private long clicksLastMonth;
    private double averageClicksPerDay;
    private PeakClickDayDto peakClickDayDto;
}

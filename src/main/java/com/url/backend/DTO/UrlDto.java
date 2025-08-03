package com.url.backend.DTO;

import com.url.backend.entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlDto {
    private String message;
    private Long id;
    private String userId;
    private String urlCode;
    private String shortUrl;
    private String originalUrl;
    private AnalyticsDto analyticsDto;
    private String description;
    private String qr;
    private int totalClicks;
    private boolean isActive;
    private boolean isBlocked;
    private LocalDateTime createdAt;
    private List<CountryTable> countryClicks;
    private List<BrowserTable> browserTables;
    private List<DeviceTable> deviceTables;
    private List<IpTable> ipTables;
    private List<RecentClicks> recentClicks;
    private List<ClicksByDate> clicksByDates;
    private List<TopReferres> topReferrers;
    private List<OperatingSystems> operatingSystems;

    public UrlDto(String message){
        this.message = message;
    }
}

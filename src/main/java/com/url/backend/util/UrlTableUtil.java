package com.url.backend.util;

import com.url.backend.DTO.AnalyticsDto;
import com.url.backend.DTO.PeakClickDayDto;
import com.url.backend.entity.*;
import com.url.backend.repo.ClicksByDateRepo;
import com.url.backend.repo.RecentClicksRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class UrlTableUtil {
    private final RecentClicksRepo recentClicksRepo;
    private final ClicksByDateRepo clicksByDateRepo;
    @Autowired
    public UrlTableUtil(RecentClicksRepo recentClicksRepo, ClicksByDateRepo clicksByDateRepo){
        this.recentClicksRepo = recentClicksRepo;
        this.clicksByDateRepo = clicksByDateRepo;
    }

    public void updateCountryClickPercentages(UserUrlEntry entry) {
        List<CountryTable> countries = entry.getCountryClicks();
        int totalClicks = countries.stream().mapToInt(CountryTable::getClicks).sum();

        if (totalClicks == 0) return ;

        for (CountryTable country : countries) {
            double percentage = (country.getClicks() * 100.0) / totalClicks;
            country.setPercentage(Math.round(percentage * 10.0) / 10.0); // round to 1 decimal
        }
    }

    public void updateBrowserClickPercentages(UserUrlEntry entry) {
        List<BrowserTable> browsers = entry.getBrowserTables();

        int totalClicks = browsers.stream()
                .mapToInt(BrowserTable::getClicks)
                .sum();

        if (totalClicks == 0) return;

        for (BrowserTable browser : browsers) {
            double percentage = (browser.getClicks() * 100.0) / totalClicks;
            browser.setPercentage(Math.round(percentage * 10.0) / 10.0); // round to 1 decimal place
        }
    }

    public void updateDeviceClickPercentages(UserUrlEntry entry) {
        List<DeviceTable> devices = entry.getDeviceTables();

        int totalClicks = devices.stream()
                .mapToInt(DeviceTable::getClicks)
                .sum();

        if (totalClicks == 0) return;

        for (DeviceTable device : devices) {
            int percentage = (int) Math.round((device.getClicks() * 100.0) / totalClicks);
            device.setPercentage(percentage);
        }
    }

    public AnalyticsDto getAnalytics(UserUrlEntry entry) {
        List<RecentClicks> allClicks = recentClicksRepo.findByUrlEntry(entry);
        long totalClicks = allClicks.size();
        long uniqueClicks = recentClicksRepo.countDistinctIpByUrlEntry(entry);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime weekStart = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime monthStart = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime lastMonthStart = LocalDate.now().minusMonths(1).withDayOfMonth(1).atStartOfDay();
        LocalDateTime lastMonthEnd = monthStart.minusSeconds(1);

        long clicksToday = recentClicksRepo.countByUrlEntryAndCreatedAtBetween(entry, todayStart, now);
        long clicksThisWeek = recentClicksRepo.countByUrlEntryAndCreatedAtBetween(entry, weekStart, now);
        long clicksThisMonth = recentClicksRepo.countByUrlEntryAndCreatedAtBetween(entry, monthStart, now);
        long clicksLastMonth = recentClicksRepo.countByUrlEntryAndCreatedAtBetween(entry, lastMonthStart, lastMonthEnd);

        // Calculate average clicks per day
        Optional<LocalDate> firstClickDate = allClicks.stream()
                .map(c -> c.getCreatedAt().toLocalDate())
                .min(Comparator.naturalOrder());

        double averagePerDay = 0;
        if (firstClickDate.isPresent()) {
            long days = ChronoUnit.DAYS.between(firstClickDate.get(), LocalDate.now()) + 1;
            averagePerDay = Math.round((totalClicks * 1.0 / days) * 10.0) / 10.0;
        }

        PeakClickDayDto peakClickDay = getPeakClickDay(entry); // from earlier
        return AnalyticsDto.builder()
                .totalClicks(totalClicks)
                .uniqueClicks(uniqueClicks)
                .clicksToday(clicksToday)
                .clicksThisWeek(clicksThisWeek)
                .clicksThisMonth(clicksThisMonth)
                .clicksLastMonth(clicksLastMonth)
                .averageClicksPerDay(averagePerDay)
                .peakClickDayDto(peakClickDay)
                .build();
    }

    public PeakClickDayDto getPeakClickDay(UserUrlEntry entry) {
        ClicksByDate peak = clicksByDateRepo.findPeakClickDay(entry.getId());
        if (peak == null) return null;
        return PeakClickDayDto.builder()
                .date(peak.getCreateAt().toLocalDate().toString())
                .clicks(peak.getClicks())
                .build();
    }

    public void updateOsClickPercentages(UserUrlEntry entry) {
        List<OperatingSystems> osStats = entry.getOperatingSystems(); // Assuming a List<OsStat> mapped in UserUrlEntry

        int totalClicks = osStats.stream()
                .mapToInt(OperatingSystems::getClicks)
                .sum();

        if (totalClicks == 0) return;

        for (OperatingSystems os : osStats) {
            int percentage = (int) Math.round((os.getClicks() * 100.0) / totalClicks);
            os.setPercentage(percentage);
        }
    }
}

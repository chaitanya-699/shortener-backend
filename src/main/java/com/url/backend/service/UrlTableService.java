package com.url.backend.service;

import com.url.backend.DTO.*;
import com.url.backend.entity.*;
import com.url.backend.repo.*;
import com.url.backend.util.GeoIpUtil;
import com.url.backend.util.UrlCodeGenerator;
import com.url.backend.util.UrlTableUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UrlTableService {

    private static final String DOMAIN = "https://masterwayne.duckdns.org/";
    private final GuestUrlRepo guestUrlRepo;
    private final AllUrlsRepo allUrlsRepo;
    private final UserUrlTableRepo userUrlTableRepo;
    private final UserUrlEntryRepo userUrlEntryRepo;
    private final UrlTableUtil urlTableUtil;
    private final CountryRepo countryRepo;
    private final BrowserRepo browserRepo;
    private final DeviceRepo deviceRepo;
    private final IpRepo ipRepo;
    private final RecentClicksRepo recentClicksRepo;
    private final ClicksByDateRepo clicksByDateRepo;
    private final TopReferrersRepo topReferrersRepo;
    private final OsRepo osRepo;

    @Autowired
    public UrlTableService(GuestUrlRepo guestUrlRepo, AllUrlsRepo allUrlsRepo,
                           UserUrlTableRepo userUrlTableRepo,
                           UserUrlEntryRepo userUrlEntryRepo,
                           UrlTableUtil urlTableUtil, CountryRepo countryRepo,
                           BrowserRepo browserRepo, DeviceRepo deviceRepo,
                           IpRepo ipRepo, RecentClicksRepo recentClicksRepo,
                           ClicksByDateRepo clicksByDateRepo, TopReferrersRepo topReferrersRepo,
                           OsRepo osRepo){

        this.guestUrlRepo = guestUrlRepo;
        this.allUrlsRepo = allUrlsRepo;
        this.userUrlTableRepo = userUrlTableRepo;
        this.userUrlEntryRepo = userUrlEntryRepo;
        this.urlTableUtil = urlTableUtil;
        this.countryRepo  = countryRepo;
        this.browserRepo = browserRepo;
        this.deviceRepo = deviceRepo;
        this.ipRepo = ipRepo;
        this.recentClicksRepo = recentClicksRepo;
        this.clicksByDateRepo = clicksByDateRepo;
        this.topReferrersRepo = topReferrersRepo;
        this.osRepo = osRepo;
    }

    public static String generateGuestId() {
        String uuidPart = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        return "guest_" + uuidPart;
    }


    public ResponseEntity<GuestResponseDto> getGuestShortUrls(GuestRequestDto guestRequestDto, HttpServletResponse response){

        String originalUrl = guestRequestDto.getOriginalUrl();
        String guestId = generateGuestId();
        String urlCode = UrlCodeGenerator.generateRandomCode();
        String shortUrl = DOMAIN + urlCode;
        String description = guestRequestDto.getDescription();
        LocalDateTime createdAt = LocalDateTime.now();
        //first save to AllUrls table;
        AllUrls allUrls = new AllUrls();
        allUrls.setOriginalUrl(originalUrl);
        allUrls.setShortUrl(shortUrl);
        allUrls.setUrlCode(urlCode);
        allUrls.setGuest(true);
        allUrls.setGuestId(guestId);
        allUrls.setCreatedAt(createdAt);

        allUrlsRepo.save(allUrls);

        //Second save to the guest table
        GuestUrlTable guestUrlTable = new GuestUrlTable();
        guestUrlTable.setGuestId(guestId);
        guestUrlTable.setCreatedAt(LocalDateTime.now());

        //create guestUrlTable array list
        GuestUrlEntry guestUrlEntry = new GuestUrlEntry();
        guestUrlEntry.setOriginalUrl(originalUrl);
        guestUrlEntry.setGuest(guestUrlTable);
        guestUrlEntry.setShortCode(urlCode);
        guestUrlEntry.setDescription(description);
        guestUrlEntry.setCreatedAt(LocalDateTime.now());
        guestUrlEntry.setShortUrl(shortUrl);
        guestUrlEntry.setCreatedAt(createdAt);

        List<GuestUrlEntry> url = new ArrayList<>();
        url.add(guestUrlEntry);

        guestUrlTable.setUrls(url);

        guestUrlRepo.save(guestUrlTable);

        //get the id
        AllUrls allUrls1 = allUrlsRepo.findByUrlCode(urlCode);

        //create GuestResponseDto and return it
        GuestResponseDto guestResponseDto = new GuestResponseDto();
        guestResponseDto.setShortUrl(shortUrl);
        guestResponseDto.setUrlCode(urlCode);
        guestResponseDto.setGuestId(guestId);
        guestResponseDto.setOriginalUrl(originalUrl);
        guestResponseDto.setId(allUrls1.getId());
        guestResponseDto.setDescription(description);
        guestResponseDto.setMessage("short url created!");
        guestResponseDto.setCreatedAt(createdAt);
        return ResponseEntity.ok(guestResponseDto);

    }

    public ResponseEntity<GuestResponseDto> getShortUrlsById(GuestDto guestDto){
        String guestId = guestDto.getGuestId();
        String originalUrl = guestDto.getOriginalUrl();
        String urlCode = UrlCodeGenerator.generateRandomCode();
        String shortUrl = DOMAIN+urlCode;
        String description = guestDto.getDescription();
        LocalDateTime createdAt = LocalDateTime.now();

        //first save to all urls
        AllUrls allUrls = new AllUrls();
        allUrls.setUrlCode(urlCode);
        allUrls.setShortUrl(urlCode);
        allUrls.setOriginalUrl(originalUrl);
        allUrls.setGuestId(guestId);
        allUrls.setGuest(true);
        allUrls.setCreatedAt(createdAt);
        allUrlsRepo.save(allUrls);
        AllUrls allUrls1 = allUrlsRepo.findByUrlCode(urlCode);

        //find guest GuestUrlTable by guest id, then update it
        GuestUrlTable guestUrlTable = guestUrlRepo.findByGuestId(guestId);
        if(guestUrlTable == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GuestResponseDto("Guest id not found"));
        }

        guestUrlTable.setCreatedAt(LocalDateTime.now());
        List<GuestUrlEntry> url = guestUrlTable.getUrls();
        GuestUrlEntry guestUrlEntry = new GuestUrlEntry();
        guestUrlEntry.setShortCode(urlCode);
        guestUrlEntry.setCreatedAt(LocalDateTime.now());
        guestUrlEntry.setOriginalUrl(originalUrl);
        guestUrlEntry.setDescription(description);
        guestUrlEntry.setGuest(guestUrlTable);
        guestUrlEntry.setShortUrl(shortUrl);
        url.add(guestUrlEntry);
        guestUrlTable.setUrls(url);
        guestUrlRepo.save(guestUrlTable);

        //create GuestResponseDto
        GuestResponseDto guestResponseDto = new GuestResponseDto();
        guestResponseDto.setShortUrl(shortUrl);
        guestResponseDto.setGuestId(guestId);
        guestResponseDto.setId(allUrls1.getId());
        guestResponseDto.setOriginalUrl(originalUrl);
        guestResponseDto.setUrlCode(urlCode);
        guestResponseDto.setDescription(description);
        guestResponseDto.setMessage("short url created!");
        guestResponseDto.setCreatedAt(createdAt);
        return ResponseEntity.ok(guestResponseDto);
    }

    public ResponseEntity<AllGuestUrlsDto> getAllFromGuestId(String guestId){
        GuestUrlTable guestUrlTable = guestUrlRepo.findByGuestId(guestId);
        if(guestUrlTable == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AllGuestUrlsDto("guest id not Found"));
        }
        AllGuestUrlsDto allGuestUrlsDto = new AllGuestUrlsDto();
        allGuestUrlsDto.setMessage("success");
        List<GuestUrlEntry> urlEntries = guestUrlTable.getUrls();
        allGuestUrlsDto.setUrlEntries(urlEntries);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(allGuestUrlsDto);
    }

    public ResponseEntity<ServerMsgDto> deleteGuestUrl(DeleteGuestUrlDto deleteGuestUrlDto) {
        String urlCode = deleteGuestUrlDto.getUrlCode();
        Long id = deleteGuestUrlDto.getId();
        String guestId = deleteGuestUrlDto.getGuestId();

        AllUrls allUrls = allUrlsRepo.findByUrlCode(urlCode);
        if (allUrls == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ServerMsgDto("URL not found"));
        }

        allUrls.setBlocked(true);
        allUrlsRepo.save(allUrls);

        GuestUrlTable guestUrlTable = guestUrlRepo.findByGuestId(guestId);

        if (guestUrlTable == null || guestUrlTable.getUrls() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ServerMsgDto("Guest not found or no URLs associated"));
        }

        List<GuestUrlEntry> urls = guestUrlTable.getUrls();
        boolean removed = urls.removeIf(url -> Objects.equals(url.getId(), id));
        if (removed) {
            guestUrlTable.setUrls(urls);
            guestUrlRepo.save(guestUrlTable);
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new ServerMsgDto(removed ? "Deleted successfully" : "URL entry not found"));
    }


    public void updateUrlDataBase(ClientDto clientDto, String urlCode) {
        String ip = clientDto.getIp();
        String country = GeoIpUtil.getCountryFromIp(ip);
        String browser = clientDto.getBrowser();
        String os = clientDto.getOs();
        String device = clientDto.getDevice();
        String referrer = clientDto.getReferrer();
        String userAgent = clientDto.getUserAgent();

        AllUrls allUrls = allUrlsRepo.findByUrlCode(urlCode);
        allUrls.setClicks(allUrls.getClicks() + 1);
        allUrlsRepo.save(allUrls);

        if (allUrls.isGuest()) {
            String guestId = allUrls.getGuestId();
            GuestUrlTable guestUrlTable = guestUrlRepo.findByGuestId(guestId);
            List<GuestUrlEntry> urls = guestUrlTable.getUrls();

            for (GuestUrlEntry url : urls) {
                if (url.getShortCode().equals(urlCode)) {
                    url.setClicks(url.getClicks() + 1);
                    break;
                }
            }

            guestUrlRepo.save(guestUrlTable);
            return;
        }

        if (allUrls.isUser()) {
            String userId = allUrls.getUserId();
            UserUrlTable userUrlTable = userUrlTableRepo.findByUserId(userId);

            List<UserUrlEntry> urls = userUrlTable.getUserUrls();
            Optional<UserUrlEntry> userUrlEntry = urls.stream()
                    .filter(entry -> entry.getUrlCode().equals(urlCode))
                    .findFirst();

            if (userUrlEntry.isPresent()) {
                UserUrlEntry entry = userUrlEntry.get();
                entry.setTotalClicks(entry.getTotalClicks() + 1);

                // COUNTRY
                List<CountryTable> countryTables = entry.getCountryClicks();
                CountryTable countryTable = countryTables.stream()
                        .filter(c -> c.getCountry().equals(country))
                        .findFirst()
                        .orElseGet(() -> {
                            CountryTable ct = new CountryTable();
                            ct.setCountry(country);
                            ct.setClicks(0);
                            ct.setUrlEntry(entry);
                            countryTables.add(ct); // ✅ FIX
                            return ct;
                        });
                countryTable.setClicks(countryTable.getClicks() + 1);
                countryRepo.save(countryTable);
                urlTableUtil.updateCountryClickPercentages(entry);

                // BROWSER
                List<BrowserTable> browserTables = entry.getBrowserTables();
                BrowserTable browserTable = browserTables.stream()
                        .filter(b -> b.getBrowser().equals(browser))
                        .findFirst()
                        .orElseGet(() -> {
                            BrowserTable bt = new BrowserTable();
                            bt.setBrowser(browser);
                            bt.setClicks(0);
                            bt.setUrlEntry(entry);
                            browserTables.add(bt); // ✅ FIX
                            return bt;
                        });
                browserTable.setClicks(browserTable.getClicks() + 1);
                browserRepo.save(browserTable);
                urlTableUtil.updateBrowserClickPercentages(entry);

                // DEVICE
                List<DeviceTable> deviceTables = entry.getDeviceTables();
                DeviceTable deviceTable = deviceTables.stream()
                        .filter(d -> d.getDevice().equals(device))
                        .findFirst()
                        .orElseGet(() -> {
                            DeviceTable dt = new DeviceTable();
                            dt.setDevice(device);
                            dt.setClicks(0);
                            dt.setUrlEntry(entry);
                            deviceTables.add(dt); // ✅ FIX
                            return dt;
                        });
                deviceTable.setClicks(deviceTable.getClicks() + 1);
                deviceRepo.save(deviceTable);
                urlTableUtil.updateDeviceClickPercentages(entry);

                // IP
                List<IpTable> ipTables = entry.getIpTables();
                IpTable ipTable = ipTables.stream()
                        .filter(i -> i.getIp().equals(ip))
                        .findFirst()
                        .orElseGet(() -> {
                            IpTable ipt = new IpTable();
                            ipt.setIp(ip);
                            ipt.setClicks(0);
                            ipt.setUrlEntry(entry);
                            ipTables.add(ipt); // ✅ FIX
                            return ipt;
                        });
                ipTable.setClicks(ipTable.getClicks() + 1);
                ipRepo.save(ipTable);

                // RECENT CLICKS
                RecentClicks recentClick = new RecentClicks();
                recentClick.setCreatedAt(LocalDateTime.now());
                recentClick.setCountry(country);
                recentClick.setBrowser(browser);
                recentClick.setDevice(device);
                recentClick.setIp(ip);
                recentClick.setReferred(referrer);
                recentClick.setUserAgent(userAgent);
                recentClick.setUrlEntry(entry);
                entry.getRecentClicks().add(recentClick); // ✅ FIX
                recentClicksRepo.save(recentClick);

                // CLICKS BY DATE
                LocalDate today = LocalDate.now();
                List<ClicksByDate> clicksByDates = entry.getClicksByDates();
                ClicksByDate clicksByDate = clicksByDates.stream()
                        .filter(cb -> cb.getCreateAt().toLocalDate().equals(today))
                        .findFirst()
                        .orElseGet(() -> {
                            ClicksByDate cb = new ClicksByDate();
                            cb.setCreateAt(LocalDateTime.now());
                            cb.setClicks(0);
                            cb.setUrlEntry(entry);
                            clicksByDates.add(cb); // ✅ FIX
                            return cb;
                        });
                clicksByDate.setClicks(clicksByDate.getClicks() + 1);
                clicksByDateRepo.save(clicksByDate);

                // TOP REFERRERS
                List<TopReferres> topReferrers = entry.getTopReferrers();
                TopReferres topReferrer = topReferrers.stream()
                        .filter(r -> r.getReferred().equalsIgnoreCase(referrer))
                        .findFirst()
                        .orElseGet(() -> {
                            TopReferres tr = new TopReferres();
                            tr.setReferred(referrer);
                            tr.setClicks(0);
                            tr.setUrlEntry(entry);
                            topReferrers.add(tr); // ✅ FIX
                            return tr;
                        });
                topReferrer.setClicks(topReferrer.getClicks() + 1);
                topReferrersRepo.save(topReferrer);

                // OPERATING SYSTEM
                List<OperatingSystems> operatingSystems = entry.getOperatingSystems();
                OperatingSystems osEntry = operatingSystems.stream()
                        .filter(o -> o.getOsName().equalsIgnoreCase(os))
                        .findFirst()
                        .orElseGet(() -> {
                            OperatingSystems ose = new OperatingSystems();
                            ose.setOsName(os);
                            ose.setClicks(0);
                            ose.setUrlEntry(entry);
                            operatingSystems.add(ose); // ✅ FIX
                            return ose;
                        });
                osEntry.setClicks(osEntry.getClicks() + 1);
                osRepo.save(osEntry);
                urlTableUtil.updateOsClickPercentages(entry);

                // Finally, save updated entry
                userUrlEntryRepo.save(entry); // Not always required, but good practice
            }
        }
    }

    public ResponseEntity<UrlDto> registerUrl(RegisterUrlDto registerUrlDto, HttpServletRequest request, HttpServletResponse response) {
        String email = registerUrlDto.getEmail();
        String originalUrl = registerUrlDto.getOriginalUrl();
        String description = registerUrlDto.getDescription();
        String userId = registerUrlDto.getId();
        String urlCode = UrlCodeGenerator.generateRandomCode();
        String shortUrl = DOMAIN + urlCode;
        LocalDateTime createdAt = LocalDateTime.now();

        // 1. Save to AllUrls table
        AllUrls allUrls = new AllUrls();
        allUrls.setGuest(false);
        allUrls.setShortUrl(shortUrl);
        allUrls.setUrlCode(urlCode);
        allUrls.setBlocked(false);
        allUrls.setUserId(userId);
        allUrls.setCreatedAt(createdAt);
        allUrls.setOriginalUrl(originalUrl);
        allUrls.setUser(true);
        allUrlsRepo.save(allUrls);

        // 2. Retrieve or create UserUrlTable
        UserUrlTable userUrlTable = userUrlTableRepo.findByUserId(userId);
        if (userUrlTable == null) {
            userUrlTable = new UserUrlTable();
            userUrlTable.setUserId(userId);
            userUrlTable.setEmail(email);
            userUrlTable.setCreatedAt(LocalDateTime.now());
            userUrlTable.setUserUrls(new ArrayList<>());
        }

        // 3. Create and persist UserUrlEntry directly
        UserUrlEntry urlEntry = new UserUrlEntry();
        urlEntry.setQrData(shortUrl);
        urlEntry.setOriginalUrl(originalUrl);
        urlEntry.setShortUrl(shortUrl);
        urlEntry.setUrlCode(urlCode);
        urlEntry.setActive(true);
        urlEntry.setBlocked(false);
        urlEntry.setTotalClicks(0);
        urlEntry.setUserId(userId);
        urlEntry.setDescription(description);
        urlEntry.setCreate_at(createdAt); // Add createdAt field if missing

        // Set back-reference (important for bidirectional relationship)
        urlEntry.setUserUrlTable(userUrlTable);

        // Initialize analytic lists
        urlEntry.setIpTables(new ArrayList<>());
        urlEntry.setBrowserTables(new ArrayList<>());
        urlEntry.setClicksByDates(new ArrayList<>());
        urlEntry.setCountryClicks(new ArrayList<>());
        urlEntry.setDeviceTables(new ArrayList<>());
        urlEntry.setOperatingSystems(new ArrayList<>());
        urlEntry.setRecentClicks(new ArrayList<>());
        urlEntry.setTopReferrers(new ArrayList<>());

        // Ensure UserUrlTable has a valid list
        if (userUrlTable.getUserUrls() == null) {
            userUrlTable.setUserUrls(new ArrayList<>());
        }
        userUrlTable.getUserUrls().add(urlEntry);

        // Save the UserUrlTable (cascade saves entry safely)
        userUrlTable = userUrlTableRepo.save(userUrlTable);

        // ✅ Re-fetch the saved entry to avoid any transient/detached issues
        UserUrlEntry savedEntry = userUrlEntryRepo.findByUrlCode(urlCode);
        if (savedEntry == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new UrlDto("Error: Failed to save and retrieve URL entry"));
        }

        // 4. Build response DTO
        UrlDto urlDto = new UrlDto();
        urlDto.setMessage("success");
        urlDto.setUserId(userId);
        urlDto.setUrlCode(urlCode);
        urlDto.setShortUrl(shortUrl);
        urlDto.setOriginalUrl(originalUrl);
        urlDto.setCreatedAt(createdAt);
        urlDto.setDescription(description);
        urlDto.setQr(shortUrl);
        urlDto.setId(allUrls.getId());
        urlDto.setActive(true);
        urlDto.setBlocked(false);

        // 5. Populate analytics
        try {
            AnalyticsDto analyticsDto = urlTableUtil.getAnalytics(savedEntry);
            urlDto.setAnalyticsDto(analyticsDto);
            urlDto.setBrowserTables(savedEntry.getBrowserTables());
            urlDto.setClicksByDates(savedEntry.getClicksByDates());
            urlDto.setCountryClicks(savedEntry.getCountryClicks());
            urlDto.setDeviceTables(savedEntry.getDeviceTables());
            urlDto.setOperatingSystems(savedEntry.getOperatingSystems());
            urlDto.setIpTables(savedEntry.getIpTables());
        } catch (Exception e) {
            // Log and continue without analytics
            System.err.println("Analytics generation failed: " + e.getMessage());
        }

        // 6. Top Referrers
        try {
            List<TopReferres> topReferrers = topReferrersRepo.findTop3ByUrlEntryOrderByClicksDesc(savedEntry);
            urlDto.setTopReferrers(topReferrers);
        } catch (Exception e) {
            urlDto.setTopReferrers(Collections.emptyList());
        }

        // 7. Recent Clicks
        try {
            List<RecentClicks> recentClicks = recentClicksRepo.findTop5ByUrlEntryOrderByCreatedAtDesc(savedEntry);
            urlDto.setRecentClicks(recentClicks);
        } catch (Exception e) {
            urlDto.setRecentClicks(Collections.emptyList());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(urlDto);
    }



    public ResponseEntity<List<UrlDto>> getAllUserUrls(String userId) {
        UserUrlTable userUrlTable = userUrlTableRepo.findByUserId(userId);

        if (userUrlTable == null) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(Collections.emptyList());
        }

        List<UserUrlEntry> userUrls = userUrlTable.getUserUrls();
        List<UrlDto> urlDtoList = new ArrayList<>();

        for (UserUrlEntry entry : userUrls) {
            UrlDto urlDto = new UrlDto();

            urlDto.setId(entry.getId());
            urlDto.setUserId(userId);
            urlDto.setUrlCode(entry.getUrlCode());
            urlDto.setShortUrl(entry.getShortUrl());
            urlDto.setOriginalUrl(entry.getOriginalUrl());
            urlDto.setCreatedAt(entry.getCreate_at()); // Make sure this field is in entity
            urlDto.setDescription(entry.getDescription()); // Optional
            urlDto.setActive(entry.isActive());
            urlDto.setBlocked(entry.isBlocked());
            urlDto.setQr(entry.getQrData());

            urlDto.setAnalyticsDto(urlTableUtil.getAnalytics(entry));

            // Set individual analytics lists
            urlDto.setBrowserTables(entry.getBrowserTables());
            urlDto.setClicksByDates(entry.getClicksByDates());
            urlDto.setCountryClicks(entry.getCountryClicks());
            urlDto.setDeviceTables(entry.getDeviceTables());
            urlDto.setOperatingSystems(entry.getOperatingSystems());
            urlDto.setIpTables(entry.getIpTables());

            // Top 3 Referrers
            List<TopReferres> topReferrers = topReferrersRepo.findTop3ByUrlEntryOrderByClicksDesc(entry);
            urlDto.setTopReferrers(topReferrers);

            // Top 5 Recent Clicks
            List<RecentClicks> recentClicks = recentClicksRepo.findTop5ByUrlEntryOrderByCreatedAtDesc(entry);
            urlDto.setRecentClicks(recentClicks);

            // Additional analytics summary if needed
            AnalyticsDto analyticsDto = urlTableUtil.getAnalytics(entry);
            urlDto.setAnalyticsDto(analyticsDto);

            System.out.println(urlDto.toString());

            urlDtoList.add(urlDto);
        }

        return ResponseEntity.ok(urlDtoList);
    }

    public ResponseEntity<ServerMsgDto> deleteUserUrl(String userId, String urlCode) {
        UserUrlEntry urlEntry = userUrlEntryRepo.findByUrlCode(urlCode);

        if (urlEntry == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ServerMsgDto("URL not found"));
        }

        // Validate the userId owns the URL
        if (!urlEntry.getUserUrlTable().getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ServerMsgDto("You are not authorized to delete this URL"));
        }

        try {
            userUrlEntryRepo.delete(urlEntry);
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(new ServerMsgDto("Deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ServerMsgDto("Deletion failed: " + e.getMessage()));
        }
    }

}

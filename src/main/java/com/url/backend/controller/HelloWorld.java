package com.url.backend.controller;


import com.url.backend.DTO.ClientDto;
import com.url.backend.entity.AllUrls;
import com.url.backend.repo.AllUrlsRepo;
import com.url.backend.service.UrlTableService;
import com.url.backend.util.UserAgentUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController("/")
public class HelloWorld {

    @Autowired
    private AllUrlsRepo allUrlsRepo;

    @Autowired
    private UrlTableService urlTableService;

@GetMapping("{urlCode}")
public void redirectToPage(
        @PathVariable String urlCode,
        HttpServletRequest request,
        HttpServletResponse response) throws IOException {

    AllUrls allUrls = allUrlsRepo.findByUrlCode(urlCode);

    if (allUrls == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "URL not found");
        return;
    }

    String originalUrl = allUrls.getOriginalUrl();

    // Get IP Address
    String ip = request.getHeader("X-Forwarded-For");
    if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getHeader("X-Real-IP");
    }
    if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getRemoteAddr();
    }
    // Handle multiple forwarded IPs
    if (ip != null && ip.contains(",")) {
        ip = ip.split(",")[0].trim();  // Use the first IP
    }

    // Get User-Agent and Referrer
    String userAgent = request.getHeader("User-Agent");
    String referer = request.getHeader("Referer");

    // Logging for debugging
    System.out.println("🔍 User-Agent: " + userAgent);
    System.out.println("🌐 IP Address: " + ip);
    System.out.println("🔗 Referrer: " + referer);

    // Construct ClientDto
    ClientDto clientDto = UserAgentUtil.extractInfo(userAgent, ip, referer);
    System.out.println("📊 Client Info: " + clientDto);

    // Save analytics
    urlTableService.updateUrlDataBase(clientDto, urlCode);

    // Redirect to original destination
    response.sendRedirect(originalUrl);
}


    @GetMapping("/hello")
    public String helloWorld(){
        return "hello world";
    }

}


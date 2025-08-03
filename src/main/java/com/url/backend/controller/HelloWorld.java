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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController("/")
public class HelloWorld {

    @Autowired
    private AllUrlsRepo allUrlsRepo;

    @Autowired
    private UrlTableService urlTableService;

    @GetMapping("{urlCode}")
    public void redirectToPage(@RequestBody String urlCode, HttpServletRequest request, HttpServletResponse response) throws IOException {
        AllUrls allUrls = allUrlsRepo.findByUrlCode(urlCode);
        String shortUrl = allUrls.getShortUrl();
        String ua = request.getHeader("User-Agent");
        String ip = request.getRemoteAddr();
        String ref = request.getHeader("Referer");
        ClientDto clientDto = UserAgentUtil.extractInfo(ua, ip, ref);
        urlTableService.updateUrlDataBase(clientDto, urlCode);
        response.sendRedirect(shortUrl);
    }

    @GetMapping("/hello")
    public String helloWorld(){
        return "hello world";
    }

}


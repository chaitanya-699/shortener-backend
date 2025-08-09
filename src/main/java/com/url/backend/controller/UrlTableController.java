package com.url.backend.controller;


import com.url.backend.DTO.*;
import com.url.backend.service.UrlTableService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/urlTableData")
            public class UrlTableController {

        @Autowired
        private UrlTableService urlTableService;

        @PostMapping("/guest")
        public ResponseEntity<GuestResponseDto> getShortUrl(@RequestBody GuestRequestDto guestRequestDto, HttpServletResponse response){
            return urlTableService.getGuestShortUrls(guestRequestDto, response);
        }

        @PostMapping("/guestId")
        public ResponseEntity<GuestResponseDto> getShortUrlById(@RequestBody GuestDto guestDto){
        return urlTableService.getShortUrlsById(guestDto);
    }

    @PostMapping("/guestAll")
    public ResponseEntity<AllGuestUrlsDto> getAllFromGuestId(@RequestBody GuestAllDto guestAllDto){
        String guestId = guestAllDto.getGuestId();
        return urlTableService.getAllFromGuestId(guestId);
    }

    @PostMapping("/guest/delete")
    public ResponseEntity<ServerMsgDto> deleteGuestUrl(@RequestBody DeleteGuestUrlDto deleteGuestUrlDto){
        return urlTableService.deleteGuestUrl(deleteGuestUrlDto);
    }

}

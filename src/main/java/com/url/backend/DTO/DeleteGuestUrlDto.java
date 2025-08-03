package com.url.backend.DTO;

import lombok.Data;

@Data
public class DeleteGuestUrlDto {
    private String guestId;
    private Long id;
    private String urlCode;
}

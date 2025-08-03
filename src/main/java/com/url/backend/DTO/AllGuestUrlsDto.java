package com.url.backend.DTO;
import com.url.backend.entity.GuestUrlEntry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AllGuestUrlsDto {
    private String message;
    private List<GuestUrlEntry> urlEntries;

    public AllGuestUrlsDto(String message){
        this.message = message;
    }
}

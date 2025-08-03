package com.url.backend.DTO;

import lombok.Data;

@Data
public class ServerMsgDto {
    private String message;

    public ServerMsgDto(String message){
        this.message = message;
    }
}

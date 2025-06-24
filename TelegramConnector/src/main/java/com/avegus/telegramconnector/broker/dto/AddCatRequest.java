package com.avegus.telegramconnector.broker.dto;

import lombok.Data;

@Data
public class AddCatRequest {
    private String userId;
    private String catName;
    private byte[] image;
    private String imageType;
}

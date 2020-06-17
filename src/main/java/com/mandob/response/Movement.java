package com.mandob.response;

import lombok.Data;

@Data
public class Movement {
    private String Status;
    private String address;
    private String longitude;
    private String latitude;
    private String salesAgentArabicName;
    private String salesAgentEnglishName;
    private String movementTime;
}

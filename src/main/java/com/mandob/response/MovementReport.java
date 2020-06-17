package com.mandob.response;

import lombok.Data;

import java.util.List;

@Data
public class MovementReport {
    private int total;
    private List<Movement> movements;
}

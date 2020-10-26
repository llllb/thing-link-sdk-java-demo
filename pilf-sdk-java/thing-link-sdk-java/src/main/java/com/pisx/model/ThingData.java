package com.pisx.model;

import lombok.Data;

import java.util.HashMap;

@Data
public class ThingData {
    public int code;
    public HashMap<String, Object> data;

    public ThingData(int code, HashMap<String, Object> data) {
        this.code = code;
        this.data = data;
    }
}
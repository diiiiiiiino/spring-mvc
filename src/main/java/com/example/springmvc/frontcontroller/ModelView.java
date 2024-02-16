package com.example.springmvc.frontcontroller;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ModelView {
    private String viewName;
    private Map<String, Object> model = new HashMap<>();

    public ModelView(String viewName) {
        this.viewName = viewName;
    }
}

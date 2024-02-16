package com.example.springmvc.frontcontroller.v3;

import com.example.springmvc.frontcontroller.ModelView;

import java.util.Map;

public interface ControllerV3 {
    ModelView process(Map<String, String> paramMap);
}

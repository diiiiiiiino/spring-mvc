package com.example.springmvc.http_message_converter;

import org.springframework.http.converter.HttpMessageConverter;

public interface MyHttpMessageConverter<T> extends HttpMessageConverter<T> {
}

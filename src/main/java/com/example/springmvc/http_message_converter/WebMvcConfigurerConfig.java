package com.example.springmvc.http_message_converter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfigurerConfig {

    @Bean
    public WebMvcConfigurer webMvcConfigurer(){
        return new WebMvcConfigurer(){
            @Override
            public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
                WebMvcConfigurer.super.addArgumentResolvers(resolvers);
            }

            @Override
            public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
                WebMvcConfigurer.super.extendMessageConverters(converters);
            }

            @Override
            public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
                WebMvcConfigurer.super.addReturnValueHandlers(handlers);
            }
        };
    }
}

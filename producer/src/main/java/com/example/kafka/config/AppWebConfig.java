package com.example.kafka.config;

import org.springframework.context.annotation.Configuration;
//import org.springframework.http.MediaType;
//import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppWebConfig implements WebMvcConfigurer {
//    @Override
//    public void configureContentNegotiation(final ContentNegotiationConfigurer configurer) {
//        configurer
//            .favorPathExtension(false)
//            .favorParameter(false)
//            .parameterName("mediaType")
//            .ignoreAcceptHeader(false)
//            .useRegisteredExtensionsOnly(false)
//            .defaultContentType(MediaType.APPLICATION_JSON)
//            //            .mediaType("xml", MediaType.APPLICATION_XML)
//            .mediaType("json", MediaType.APPLICATION_JSON);
//    }
}
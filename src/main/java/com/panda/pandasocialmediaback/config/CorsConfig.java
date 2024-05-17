package com.panda.pandasocialmediaback.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import java.util.Collections;

@Configuration
public class CorsConfig implements WebFluxConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("http://localhost:3000")
                .allowedMethods("GET", "POST","PUT","PATCH","DELETE")
                .allowedOriginPatterns("*")
                .allowCredentials(true)
                .allowedHeaders(String.valueOf(Collections.singletonList("*")))
                .maxAge(3600L);
        WebFluxConfigurer.super.addCorsMappings(registry);
    }
}

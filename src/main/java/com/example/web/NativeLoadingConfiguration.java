package com.example.web;

import org.springframework.context.annotation.Configuration;

@Configuration
public class NativeLoadingConfiguration {
    static {
        nu.pattern.OpenCV.loadShared();
    }
}

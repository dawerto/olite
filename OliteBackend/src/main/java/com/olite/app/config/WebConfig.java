package com.olite.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Cuando el cliente pide /uploads/**, Spring sirve los archivos
        // desde la carpeta física "uploads/" en la raíz del proyecto.
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}

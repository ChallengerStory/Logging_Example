package com.aicube.log_proj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class LogProjApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(LogProjApplication.class, args);
    }

    @Bean
    public ClientLogInterceptor clientLogInterceptor() {
        return new ClientLogInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(clientLogInterceptor())
                .addPathPatterns("/**");
    }
}

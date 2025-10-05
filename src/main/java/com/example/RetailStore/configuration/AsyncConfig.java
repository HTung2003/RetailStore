package com.example.RetailStore.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // Số luồng xử lý song song
        executor.setMaxPoolSize(10); // Tối đa
        executor.setQueueCapacity(100); // Số task xếp hàng
        executor.setThreadNamePrefix("EmailThread-");
        executor.initialize();
        return executor;
    }
}

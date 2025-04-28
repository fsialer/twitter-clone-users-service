package com.fernando.ms.users.app.infrastructure.config;

import com.fernando.ms.users.app.domain.models.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

@Configuration
public class ReactiveSinkConfig {
    @Bean
    public Sinks.Many<User> userSink() {
        return Sinks.many().multicast().onBackpressureBuffer();
    }
}

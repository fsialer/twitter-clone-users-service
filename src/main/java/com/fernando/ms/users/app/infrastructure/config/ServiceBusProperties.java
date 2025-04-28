package com.fernando.ms.users.app.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.cloud.azure.servicebus")
@Getter
@Setter
public class ServiceBusProperties {
    private String connectionString;
    private String queueName;
}

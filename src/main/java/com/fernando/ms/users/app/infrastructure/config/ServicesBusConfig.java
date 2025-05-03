package com.fernando.ms.users.app.infrastructure.config;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusReceiverAsyncClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServicesBusConfig {
    @Bean
    public ServiceBusReceiverAsyncClient receiverAsyncClient(ServiceBusProperties serviceBusProperties) {
        return new ServiceBusClientBuilder()
                .connectionString(serviceBusProperties.getConnectionString())
                .receiver()
                .queueName(serviceBusProperties.getQueueName())
                .buildAsyncClient();
    }
}

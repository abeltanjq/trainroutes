package com.abeltan.trainroutes.journey;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JourneyConfiguration {
    @Bean
    public JourneyClient journeyClient() {
        return new JourneyClient();
    }
}

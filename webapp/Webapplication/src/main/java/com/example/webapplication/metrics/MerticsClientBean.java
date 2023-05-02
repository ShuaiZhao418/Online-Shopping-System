package com.example.webapplication.metrics;

import com.timgroup.statsd.NoOpStatsDClient;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MerticsClientBean {

    private boolean publicMetrics = true;
    private String prefix = "csye6225"; // Replace with your own prefix
    private String host = "localhost"; // Replace with your own StatsD host
    int port = 8125; // Replace with your own StatsD port

    @Bean
    public StatsDClient merticsClient() {
       if (publicMetrics) {
           return new NonBlockingStatsDClient(prefix, host, port);
       }
       return new NoOpStatsDClient();
    }

}

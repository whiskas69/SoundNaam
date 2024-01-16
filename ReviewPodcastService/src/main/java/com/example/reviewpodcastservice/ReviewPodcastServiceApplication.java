package com.example.reviewpodcastservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ReviewPodcastServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReviewPodcastServiceApplication.class, args);
    }

}

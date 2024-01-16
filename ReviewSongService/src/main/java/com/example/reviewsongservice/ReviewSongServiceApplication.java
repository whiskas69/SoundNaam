package com.example.reviewsongservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ReviewSongServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReviewSongServiceApplication.class, args);
    }

}

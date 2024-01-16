package com.example.reviewpodcastservice;

import com.example.reviewpodcastservice.core.data.ReviewPodcastRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewPodcastService {
    @Autowired
    private ReviewPodcastRepository repository;
}

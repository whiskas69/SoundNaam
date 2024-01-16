package com.example.reviewsongservice;

import com.example.reviewsongservice.core.data.ReviewSongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewSongService {
    @Autowired
    private ReviewSongRepository repository;

}

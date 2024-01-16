package com.example.reviewmembershipservice;

import com.example.reviewmembershipservice.core.data.ReviewMembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewMembershipService {
    @Autowired
    private ReviewMembershipRepository repository;
}

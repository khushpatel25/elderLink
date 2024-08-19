package com.elderlink.backend.services;

import com.elderlink.backend.domains.entities.ReviewEntity;

import java.util.List;

public interface ReviewService{

    public ReviewEntity createReview(ReviewEntity reviewEntity);

    public List<ReviewEntity> getReviewByVolunteerId(Long volunteerId);
}

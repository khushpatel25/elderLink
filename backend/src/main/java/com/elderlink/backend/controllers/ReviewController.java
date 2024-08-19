package com.elderlink.backend.controllers;

import com.elderlink.backend.domains.dto.ReviewDto;
import com.elderlink.backend.domains.entities.ReviewEntity;
import com.elderlink.backend.mappers.Mapper;
import com.elderlink.backend.services.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
public class ReviewController{

    @Autowired
    private Mapper<ReviewEntity, ReviewDto> reviewMapper;

    @Autowired
    private ReviewService reviewService;
    /**
     * Endpoint to create a new review.
     *
     * @param reviewDto The DTO containing the review details
     * @return ResponseEntity containing the created review details or error status
     */
    @PostMapping("/create")
    public ResponseEntity<ReviewDto> createReview(
            @Valid @RequestBody ReviewDto reviewDto
    ){
        ReviewEntity reviewEntity = reviewMapper.toEntity(reviewDto);
        ReviewEntity createdReview = reviewService.createReview(reviewEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewMapper.toDto(createdReview));
    }
    /**
     * Endpoint to get reviews by volunteer ID.
     *
     * @param volunteerId The ID of the volunteer whose reviews are to be fetched
     * @return ResponseEntity containing the list of reviews or error status
     */

    @GetMapping("/get/{volunteerId}")
    public ResponseEntity<List<ReviewEntity>> getReview(
            @Valid @PathVariable("volunteerId") Long volunteerId
    ){
        List<ReviewEntity> reviewEntityList = reviewService.getReviewByVolunteerId(volunteerId);

        return ResponseEntity.status(HttpStatus.OK).body(reviewEntityList);

    }

}

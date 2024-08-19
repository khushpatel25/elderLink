package com.elderlink.backend.services.impl;

import com.elderlink.backend.domains.entities.ReviewEntity;
import com.elderlink.backend.repositories.ReviewRepository;
import com.elderlink.backend.repositories.UserRepository;
import com.elderlink.backend.services.ReviewService;
import com.elderlink.backend.services.UserService;
import com.elderlink.backend.utils.IsUserAuthorized;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService{

    Logger logger = LoggerFactory.getLogger (ReviewService.class);
    public static final int fiverating = 5;
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private IsUserAuthorized isUserAuthorized;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;
    /**
     * Create a new review.
     *
     * @param reviewEntity The review entity to create
     * @return The created review entity
     */

    @Override
    public ReviewEntity createReview(ReviewEntity reviewEntity) {
        try {
            Long volunteerId = reviewEntity.getVolunteer().getId();
            Long elderPersonId = reviewEntity.getElderPerson().getId();

            //To check volunteer and elderPerson are exist
            if(!userService.isUserExisted (volunteerId)){
                throw new EntityNotFoundException ("Volunteer with this id doesn't exist!");
            }
            if(!userService.isUserExisted (elderPersonId)){
                throw new EntityNotFoundException("ElderPerson with this id doesn't exist!");
            }

            //To check elderPerson is authorized to create review or not
            isUserAuthorized.checkUserAuthority(elderPersonId);

            int rating = reviewEntity.getRating();


            if(rating < 1 || rating > fiverating){
                throw new IllegalArgumentException("Rating value must be between 1 and 5");
            }

            ReviewEntity review = reviewRepository.save (reviewEntity);
            logger.info ("Review Created successfully.");
            return review;
        }catch (EntityNotFoundException e){
            logger.error (e.getMessage ());
            throw new EntityNotFoundException (e.getMessage ());
        }catch (IllegalArgumentException e){
            logger.error (e.getMessage ());
            throw new IllegalArgumentException (e.getMessage ());
        }catch (Exception e){
            throw new RuntimeException ("An error occurred while creating a review! "+e.getMessage ());
        }
    }
    /**
     * Retrieve reviews by volunteer ID.
     *
     * @param volunteerId The ID of the volunteer
     * @return List of review entities
     */
    public List<ReviewEntity> getReviewByVolunteerId(Long volunteerId){

        try {
            //To check if user exist
            if(!userService.isUserExisted (volunteerId)){
                throw new EntityNotFoundException ("User with this id doesn't exist!");
            }
//            25th April

            List<ReviewEntity> VolunteerReviews = reviewRepository.findByVolunteerId(volunteerId);


            return VolunteerReviews;

        }catch (EntityNotFoundException e){
            logger.error (e.getMessage ());
            throw new EntityNotFoundException (e.getMessage ());
        }catch (IllegalArgumentException e){
            logger.error (e.getMessage ());
            throw new IllegalArgumentException (e.getMessage ());
        }catch (Exception e){
            throw new RuntimeException ("An error occurred while fetching the reviews! "+e.getMessage ());
        }

    }
}

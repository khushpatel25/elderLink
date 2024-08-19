package com.elderlink.backend.services.impl;

import com.elderlink.backend.domains.entities.ReviewEntity;
import com.elderlink.backend.domains.entities.UserEntity;
import com.elderlink.backend.repositories.ReviewRepository;
import com.elderlink.backend.repositories.UserRepository;
import com.elderlink.backend.utils.IsUserAuthorized;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class ReviewServiceImplTest{

    @Mock
    private IsUserAuthorized isUserAuthorized;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserServiceImpl userService;
    @Mock
    private ReviewRepository reviewRepository;
    @InjectMocks
    private ReviewServiceImpl reviewService;

    private UserEntity volunteer;

    private UserEntity elderPerson;

    private ReviewEntity review;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks (this);

        volunteer = mock(UserEntity.class);

        elderPerson = mock(UserEntity.class);

        review = mock(ReviewEntity.class);
        when(review.getVolunteer ()).thenReturn (volunteer);
        when(review.getElderPerson ()).thenReturn (elderPerson);
    }

    @Test
    void testCreateReviewSuccess(){

        //Arrange
        when(review.getVolunteer ().getId ()).thenReturn (1L);
        when(review.getElderPerson ().getId ()).thenReturn (2L);
        when(review.getRating ()).thenReturn (3);

        when(userRepository.existsById (anyLong ())).thenReturn (true);
        when(userService.isUserExisted (anyLong ())).thenReturn (true);

        doNothing().when(isUserAuthorized).checkUserAuthority(anyLong());

        when(reviewRepository.save (any (ReviewEntity.class))).thenReturn (review);

        //Act
        ReviewEntity result = reviewService.createReview (review);

        //Assert
        assertEquals (review,result);

    }

    @Test
    void testCreateReviewThrowsIllegalArgumentException(){

        //Arrange
        when(review.getVolunteer ().getId ()).thenReturn (1L);
        when(review.getElderPerson ().getId ()).thenReturn (2L);
        when(review.getRating ()).thenReturn (0);

        when(userRepository.existsById (anyLong ())).thenReturn (true);
        when(userService.isUserExisted (anyLong ())).thenReturn (true);

        doNothing().when(isUserAuthorized).checkUserAuthority(anyLong());

        assertThrows (IllegalArgumentException.class,()-> reviewService.createReview (review));

    }

    @Test
    void testCreateReviewThrowsEntityNotFoundExceptionForVolunteer(){

        //Arrange
        when(review.getVolunteer ().getId ()).thenReturn (1L);

        when(userRepository.existsById (volunteer.getId ())).thenReturn (false);
        when(userService.isUserExisted (volunteer.getId ())).thenReturn (false);

        doNothing().when(isUserAuthorized).checkUserAuthority(anyLong());

        assertThrows (EntityNotFoundException.class,()-> reviewService.createReview (review));

    }

    @Test
    void testCreateReviewThrowsEntityNotFoundExceptionForElderPerson(){

        //Arrange
        when(review.getVolunteer ().getId ()).thenReturn (1L);
        when(review.getElderPerson ().getId ()).thenReturn (2L);

        when(userRepository.existsById (volunteer.getId ())).thenReturn (true);
        when(userService.isUserExisted (volunteer.getId ())).thenReturn (true);

        when(userRepository.existsById (elderPerson.getId ())).thenReturn (false);
        when(userService.isUserExisted (elderPerson.getId ())).thenReturn (false);

        doNothing().when(isUserAuthorized).checkUserAuthority(anyLong());

        assertThrows (EntityNotFoundException.class,()-> reviewService.createReview (review));

    }

//    get All Reviews for a volunteer - test cases

    @Test
    void testGetReviewByVolunteerId_UserNotFound() {
        // Arrange
        Long volunteerId = 123L;
        when(userService.isUserExisted(volunteerId)).thenReturn(false);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> reviewService.getReviewByVolunteerId(volunteerId));
        assertEquals("User with this id doesn't exist!", exception.getMessage());

        // Verify
        verify(userService).isUserExisted(volunteerId);
        verifyNoInteractions(reviewRepository);
    }

    @Test
    void testGetReviewByVolunteerId_ReviewFound() {
        // Arrange
        Long volunteerId = 123L;
        when(userService.isUserExisted(volunteerId)).thenReturn(true);
        List<ReviewEntity> mockReviews = new ArrayList<>();
        // Populate mockReviews as needed
        when(reviewRepository.findByVolunteerId(volunteerId)).thenReturn(mockReviews);

        // Act
        List<ReviewEntity> result = reviewService.getReviewByVolunteerId(volunteerId);

        // Assert
        assertSame(mockReviews, result);

        // Verify
        verify(userService).isUserExisted(volunteerId);
        verify(reviewRepository).findByVolunteerId(volunteerId);
    }

    @Test
    void testGetReviewByVolunteerId_NullArgument() {
        // Arrange
        Long volunteerId = null;

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> reviewService.getReviewByVolunteerId(volunteerId));
        assertEquals("User with this id doesn't exist!", exception.getMessage());

    }


    @Test
    void testGetReviewByVolunteerId_RuntimeException() {
        // Arrange
        Long volunteerId = 123L;
        when(userService.isUserExisted(volunteerId)).thenThrow(new RuntimeException("Test Exception"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> reviewService.getReviewByVolunteerId(volunteerId));
        assertEquals("An error occurred while fetching the reviews! Test Exception", exception.getMessage());

        // Verify
        verify(userService).isUserExisted(volunteerId);
        verifyNoMoreInteractions(reviewRepository);
    }
}

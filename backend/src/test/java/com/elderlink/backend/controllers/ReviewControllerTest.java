package com.elderlink.backend.controllers;

import com.elderlink.backend.domains.dto.ReviewDto;
import com.elderlink.backend.domains.entities.ReviewEntity;
import com.elderlink.backend.mappers.Mapper;
import com.elderlink.backend.services.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    @Mock
    private Mapper<ReviewEntity, ReviewDto> reviewMapper;

    @InjectMocks
    private ReviewController reviewController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();
    }

    @Test
    void createReview_ReturnsCreated() throws Exception {
        ReviewDto reviewDto = new ReviewDto(null, 1L, 2L, "Great service", 5, null);
        ReviewEntity reviewEntity = new ReviewEntity(null, null, null, "Great service", 5, null);
        when(reviewMapper.toEntity(any(ReviewDto.class))).thenReturn(reviewEntity);
        when(reviewService.createReview(any(ReviewEntity.class))).thenReturn(reviewEntity);
        when(reviewMapper.toDto(any(ReviewEntity.class))).thenReturn(reviewDto);

        String reviewDtoJson = """
            {
                "volunteerId": 1,
                "elderPersonId": 2,
                "reviewMessage": "Great service",
                "rating": 5
            }
            """;

        mockMvc.perform(post("/api/review/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reviewDtoJson))
                .andExpect(status().isCreated());

        verify(reviewService).createReview(any(ReviewEntity.class));
        verify(reviewMapper).toDto(any(ReviewEntity.class));
    }


    @Test
    void getReview_ReturnsOk() throws Exception {
        Long volunteerId = 1L;
        List<ReviewEntity> reviewEntities = Collections.singletonList(new ReviewEntity(null, null, null, "Good job", 4, null));
        when(reviewService.getReviewByVolunteerId(volunteerId)).thenReturn(reviewEntities);

        mockMvc.perform(get("/api/review/get/{volunteerId}", volunteerId))
                .andExpect(status().isOk());

        verify(reviewService).getReviewByVolunteerId(volunteerId);
    }
}

package com.elderlink.backend.controllers;
import com.elderlink.backend.domains.dto.RequestDto;
import com.elderlink.backend.domains.entities.RequestEntity;
import com.elderlink.backend.domains.enums.RequestCategory;
import com.elderlink.backend.domains.enums.RequestStatus;
import com.elderlink.backend.domains.enums.RequestUrgencyLevel;
import com.elderlink.backend.mappers.Mapper;
import com.elderlink.backend.repositories.UserRepository;
import com.elderlink.backend.services.RequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RequestsControllerTest {

    @Mock
    private RequestService requestService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Mapper<RequestEntity, RequestDto> requestMapper;

    @InjectMocks
    private RequestsController requestsController;

    private RequestDto requestDto;
    private RequestEntity requestEntity;

    @BeforeEach
    void setUp() {
        requestDto = createTestRequestDto();
        requestEntity = new RequestEntity();
    }

    @Test
    void createRequest_Success() {
        when(requestMapper.toEntity(any(RequestDto.class))).thenReturn(requestEntity);
        when(requestService.createRequest(any(RequestEntity.class))).thenReturn(requestEntity);
        when(requestMapper.toDto(any(RequestEntity.class))).thenReturn(requestDto);

        ResponseEntity<RequestDto> response = requestsController.createRequest(requestDto);

        assertEquals(ResponseEntity.status(201).body(requestDto), response);
    }

    @Test
    void getRequestsByUserId_Success() {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);
        when(requestService.findRequestsByUserId(userId)).thenReturn(Arrays.asList(requestEntity));
        when(requestMapper.toDto(any(RequestEntity.class))).thenReturn(requestDto);

        ResponseEntity<List<RequestDto>> response = requestsController.getRequestsByUserId(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void updateRequest_Success() {
        Long requestId = 1L;
        when(requestMapper.toEntity(any(RequestDto.class))).thenReturn(requestEntity);
        when(requestService.updateRequest(eq(requestId), any(RequestEntity.class))).thenReturn(requestEntity);
        when(requestMapper.toDto(any(RequestEntity.class))).thenReturn(requestDto);

        ResponseEntity<RequestDto> response = requestsController.updateRequest(requestId, requestDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(requestDto, response.getBody());
    }

    @Test
    void deleteRequest_Success() {
        Long requestId = 1L;
        doNothing().when(requestService).deleteRequest(requestId);

        ResponseEntity<Void> response = requestsController.deleteRequest(requestId);

        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void getRequestsByUserId_NotFound() {
        Long userId = 99L;
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(UsernameNotFoundException.class, () -> requestsController.getRequestsByUserId(userId));
    }

    private RequestDto createTestRequestDto() {
        return RequestDto.builder()
                .userId(1L)
                .requestCategory(RequestCategory.TRANSPORTATION)
                .requestUrgencyLevel(RequestUrgencyLevel.URGENT)
                .requestDescription("Need a ride to the doctor.")
                .location("123 Main St, Springfield")
                .date(LocalDate.now().plusDays(1)) // Future date
                .time("14:00")
                .durationInMinutes(60)
                .requestStatus(RequestStatus.OPEN)
                .localDateTime(LocalDateTime.now())
                .updateDateTime(LocalDateTime.now())
                .build();
    }

    @Test
    void getAllRequests_Success() {
        // Setup
        when(requestService.getAllRequests()).thenReturn(Arrays.asList(requestEntity));
        when(requestMapper.toDto(any(RequestEntity.class))).thenReturn(requestDto);

        // Execution
        ResponseEntity<List<RequestDto>> response = requestsController.getAllRequests();

        // Verification
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size()); // Assuming the setup returns one request
        assertEquals(requestDto, response.getBody().get(0));
    }

    @Test
    void getRequestsByRequestId_Success() {
        // Setup
        Long requestId = 1L; // Assume this ID exists
        when(requestService.findRequestById(requestId)).thenReturn(Optional.of(requestEntity));
        when(requestMapper.toDto(any(RequestEntity.class))).thenReturn(requestDto);

        // Execution
        ResponseEntity<RequestDto> response = requestsController.getRequestsByRequestId(requestId);

        // Verification
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(requestDto, response.getBody());
    }

}
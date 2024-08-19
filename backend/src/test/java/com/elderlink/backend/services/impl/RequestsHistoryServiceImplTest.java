package com.elderlink.backend.services.impl;

import com.elderlink.backend.domains.entities.RequestEntity;
import com.elderlink.backend.domains.entities.RequestHistoryEntity;
import com.elderlink.backend.domains.entities.UserEntity;
import com.elderlink.backend.domains.enums.ActionType;
import com.elderlink.backend.repositories.RequestHistoryRepository;
import com.elderlink.backend.repositories.RequestRepository;
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
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class RequestsHistoryServiceImplTest{
    @Mock
    private IsUserAuthorized isUserAuthorized;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserServiceImpl userService;
    @Mock
    private RequestServiceImpl requestService;
    @Mock
    private RequestRepository requestRepository;
    @Mock
    private RequestHistoryRepository requestHistoryRepository;
    @InjectMocks
    private RequestsHistoryServiceImpl requestHistoryService;

    private RequestHistoryEntity requestHistoryEntity;

    private UserEntity volunteer;

    private UserEntity elderPerson;

    private RequestEntity requestEntity;

    private UserEntity user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks (this);

        user = mock (UserEntity.class);

        volunteer = mock (UserEntity.class);

        elderPerson = mock (UserEntity.class);

        requestEntity = mock (RequestEntity.class);
        when (requestEntity.getUser ()).thenReturn (user);

        requestHistoryEntity = mock (RequestHistoryEntity.class);
        when (requestHistoryEntity.getElderPerson ()).thenReturn (elderPerson);
        when (requestHistoryEntity.getVolunteer ()).thenReturn (volunteer);
        when (requestHistoryEntity.getRequest ()).thenReturn (requestEntity);
    }

    @Test
    void testCreateRequestHistorySuccess() {
        //Arrange
        when (requestEntity.getUser ().getId ()).thenReturn (5L);
        when (requestRepository.findById (anyLong ())).thenReturn (Optional.of (requestEntity));
        when (requestService.isRequestExists (anyLong ())).thenReturn (true);

        when (requestHistoryEntity.getElderPerson ().getId ()).thenReturn (5L);
        when (requestHistoryEntity.getVolunteer ().getId ()).thenReturn (6L);
        when (requestHistoryRepository.save (any (RequestHistoryEntity.class))).thenReturn (requestHistoryEntity);

        if (Objects.equals (requestEntity.getUser ().getId (), requestHistoryEntity.getElderPerson ().getId ())) {
            // Condition is true, do nothing
        }

        when (userRepository.existsById (anyLong ())).thenReturn (true);
        when (userService.isUserExisted (anyLong ())).thenReturn (true);

        doNothing ().when (isUserAuthorized).checkUserAuthority (anyLong ());

        //Act
        RequestHistoryEntity result = requestHistoryService.createRequestHistory (requestHistoryEntity);

        //Assert
        assertEquals (requestHistoryEntity, result);
    }

    @Test
    void testCreateRequestHistoryThrowsEntityNotFoundExceptionForVolunteer(){

        doNothing().when(isUserAuthorized).checkUserAuthority(anyLong());

        when(requestHistoryEntity.getVolunteer ().getId ()).thenReturn (1L);

        when(userRepository.existsById (volunteer.getId ())).thenReturn (false);
        when(userService.isUserExisted (volunteer.getId ())).thenReturn (false);

        assertThrows (EntityNotFoundException.class,()-> requestHistoryService.createRequestHistory (requestHistoryEntity));
    }

    @Test
    void testCreateRequestHistoryThrowsEntityNotFoundExceptionForElderPerson(){
        doNothing().when(isUserAuthorized).checkUserAuthority(anyLong());

        when(requestHistoryEntity.getVolunteer ().getId ()).thenReturn (1L);
        when(requestHistoryEntity.getElderPerson ().getId ()).thenReturn (2L);

        when(userRepository.existsById (volunteer.getId ())).thenReturn (true);
        when(userService.isUserExisted (volunteer.getId ())).thenReturn (true);

        when(userRepository.existsById (elderPerson.getId ())).thenReturn (false);
        when(userService.isUserExisted (elderPerson.getId ())).thenReturn (false);

        assertThrows (EntityNotFoundException.class,()-> requestHistoryService.createRequestHistory (requestHistoryEntity));
    }

    @Test
    void testGetRequestHistoriesByRequestIdSuccess() {
        // Arrange
        Long requestId = 1L;
        List<RequestHistoryEntity> expectedRequestHistories = List.of(requestHistoryEntity);
        when(requestHistoryRepository.findByRequestId(requestId)).thenReturn(expectedRequestHistories);

        // Act
        List<RequestHistoryEntity> result = requestHistoryService.getRequestHistoriesByRequestId(requestId);

        // Assert
        assertEquals(expectedRequestHistories, result);
        verify(requestHistoryRepository).findByRequestId(requestId);
    }

    @Test
    void testGetRequestHistoriesByRequestIdThrowsException() {
        // Arrange
        Long requestId = 1L;
        when(requestHistoryRepository.findByRequestId(requestId)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class,
                () -> requestHistoryService.getRequestHistoriesByRequestId(requestId));
        assertEquals("An error occurred while fetching requestHistoryByRequestId", exception.getMessage());
    }

    @Test
    void testGetRequestHistoriesByElderPersonIdSuccess() {
        // Arrange
        Long elderPersonId = 2L;
        List<RequestHistoryEntity> expectedRequestHistories = List.of(requestHistoryEntity);
        when(requestHistoryRepository.findByElderPersonId(elderPersonId)).thenReturn(expectedRequestHistories);

        // Act
        List<RequestHistoryEntity> result = requestHistoryService.getRequestHistoriesByElderPersonId(elderPersonId);

        // Assert
        assertEquals(expectedRequestHistories, result);
        verify(requestHistoryRepository).findByElderPersonId(elderPersonId);
    }

    @Test
    void testGetRequestHistoriesByElderPersonIdThrowsException() {
        // Arrange
        Long elderPersonId = 2L;
        when(requestHistoryRepository.findByElderPersonId(elderPersonId)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class,
                () -> requestHistoryService.getRequestHistoriesByElderPersonId(elderPersonId));
        assertEquals("An error occurred while fetching requestHistory by ElderPersonId!", exception.getMessage());
    }

    //    New

    //	To be written by Abhishek for Update Functionality
    @Test
    void testUpdateRequestHistory_SuccessfulUpdate() {
        // Arrange
        Long id = 1L;
        RequestHistoryEntity requestHistoryEntity = new RequestHistoryEntity();
        requestHistoryEntity.setActionType(ActionType.NOT_ACTED);
        RequestEntity requestEntity = new RequestEntity();
        requestEntity.setId(1L);
        requestHistoryEntity.setRequest(requestEntity);

        // Set up volunteer and elderPerson entities since your service might be using them.
        UserEntity volunteer = new UserEntity();
        volunteer.setId(2L); // Assume 2L is an existing volunteer ID.
        requestHistoryEntity.setVolunteer(volunteer); // This ensures getVolunteer() won't return null.

        UserEntity elderPerson = new UserEntity();
        elderPerson.setId(3L); // Assume 3L is an existing elder person ID.
        requestHistoryEntity.setElderPerson(elderPerson); // Similar setup for elderPerson.

        RequestHistoryEntity existingRequestHistory = new RequestHistoryEntity();
        existingRequestHistory.setId(id);
        existingRequestHistory.setRequest(requestEntity);
        existingRequestHistory.setVolunteer(volunteer); // Ensure the existing entity also has a volunteer set.
        existingRequestHistory.setElderPerson(elderPerson); // Ensure the existing entity has an elder person set.

        when(requestHistoryRepository.findById(id)).thenReturn(Optional.of(existingRequestHistory));
        when(requestRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(isUserAuthorized).checkUserAuthority(anyLong());
        when(requestHistoryRepository.save(any(RequestHistoryEntity.class))).thenReturn(existingRequestHistory);

        // Act
        RequestHistoryEntity result = requestHistoryService.updateRequestHistory(id, requestHistoryEntity);

        // Assert
        assertNotNull(result);
        assertEquals(ActionType.NOT_ACTED, result.getActionType());
        assertEquals(existingRequestHistory.getVolunteer(), result.getVolunteer());
        assertEquals(existingRequestHistory.getElderPerson(), result.getElderPerson());
        verify(requestHistoryRepository).save(existingRequestHistory);
    }

    // Abhishek given by Piyush
    @Test
    void testUpdateRequestHistory_Successful() {
        // Arrange
        Long id = 1L;

        UserEntity elderPerson = new UserEntity();
        elderPerson.setId(3L);

        // Mock the dependencies and their interactions
        RequestHistoryEntity mockExistingRequestHistory = mock(RequestHistoryEntity.class);
        RequestHistoryEntity updatedRequestHistoryEntity = new RequestHistoryEntity();
        updatedRequestHistoryEntity.setActionType(ActionType.ACTED); // The action type you're updating to
        RequestEntity requestEntity = new RequestEntity();
        requestEntity.setId(3L);
        updatedRequestHistoryEntity.setRequest(requestEntity);

        UserEntity volunteerEntity = new UserEntity();
        volunteerEntity.setId(2L);
        updatedRequestHistoryEntity.setVolunteer(volunteerEntity);
        updatedRequestHistoryEntity.setElderPerson(elderPerson); // Ensure elderPerson is set


        when(mockExistingRequestHistory.getElderPerson()).thenReturn(elderPerson);

        // Setup mocks
        when(requestHistoryRepository.findById(id)).thenReturn(Optional.of(mockExistingRequestHistory));
        when(requestRepository.existsById(requestEntity.getId())).thenReturn(true);
        when(userRepository.existsById(volunteerEntity.getId())).thenReturn(true);
        when(userRepository.existsById(elderPerson.getId())).thenReturn(true); // Ensure the elder person's existence is mocked
        doNothing().when(isUserAuthorized).checkUserAuthority(elderPerson.getId()); // Authorize action based on elderPerson's ID
        when(requestHistoryRepository.save(any(RequestHistoryEntity.class))).thenReturn(updatedRequestHistoryEntity);

        // Act
        RequestHistoryEntity result = requestHistoryService.updateRequestHistory(id, updatedRequestHistoryEntity);

        // Assert
        assertNotNull(result);
        verify(requestHistoryRepository).save(any(RequestHistoryEntity.class)); // Ensure save was called with the updated entity
        assertEquals(ActionType.ACTED, result.getActionType()); // Assert the action type was updated
        assertEquals(volunteerEntity.getId(), result.getVolunteer().getId());
        assertEquals(requestEntity.getId(), result.getRequest().getId());
    }

    @Test
    void testIsRequestHistoryExists_IdExists() {
        // Arrange
        Long id = 123L;
        when(requestHistoryRepository.existsById(id)).thenReturn(true);

        // Act
        boolean result = requestHistoryService.isRequestHistoryExists(id);

        // Assert
        assertTrue(result);

        // Verify
        verify(requestHistoryRepository).existsById(id);
    }

    @Test
    void testIsRequestHistoryExists_IdDoesNotExist() {
        // Arrange
        Long id = 123L;
        when(requestHistoryRepository.existsById(id)).thenReturn(false);

        // Act
        boolean result = requestHistoryService.isRequestHistoryExists(id);

        // Assert
        assertFalse(result);

        // Verify
        verify(requestHistoryRepository).existsById(id);
    }


    @Test
    void testGetRequestHistories_Success() {
        // Arrange
        List<RequestHistoryEntity> mockRequestHistories = new ArrayList<>();
        // Populate mockRequestHistories as needed
        when(requestHistoryRepository.findAll()).thenReturn(mockRequestHistories);

        // Act
        List<RequestHistoryEntity> result = requestHistoryService.getRequestHistories();

        // Assert
        assertSame(mockRequestHistories, result);

        // Verify
        verify(requestHistoryRepository).findAll();
    }

    @Test
    void testGetRequestHistories_ExceptionThrown() {
        // Arrange
        when(requestHistoryRepository.findAll()).thenThrow(new RuntimeException("Test Exception"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> requestHistoryService.getRequestHistories());
        assertEquals("An error occurred while fetching requestHistories.", exception.getMessage());

        // Verify
        verify(requestHistoryRepository).findAll();
    }

    @Test
    void testDeleteRequestHistory_Success() {
        // Arrange
        Long id = 123L;
        RequestHistoryEntity mockRequestHistoryEntity = mock(RequestHistoryEntity.class);
        RequestEntity mockRequestEntity = mock(RequestEntity.class);
        UserEntity mockUserEntity = mock(UserEntity.class);

        // Mock the behavior to return a non-null RequestEntity and subsequently a non-null UserEntity
        when(mockRequestHistoryEntity.getRequest()).thenReturn(mockRequestEntity);
        when(mockRequestEntity.getUser()).thenReturn(mockUserEntity);

        when(requestHistoryRepository.findById(id)).thenReturn(Optional.of(mockRequestHistoryEntity));

        // Act & Verify that no exception is thrown
        assertDoesNotThrow(() -> requestHistoryService.deleteRequestHistory(id));

        // Verify interactions
        verify(requestHistoryRepository).findById(id);
        verify(requestHistoryRepository).deleteById(id);
        verify(isUserAuthorized).checkUserAuthority(anyLong());
    }

    @Test
    void testDeleteRequestHistory_RuntimeException() {
        // Arrange
        Long id = 123L;
        when(requestHistoryRepository.findById(id)).thenThrow(new RuntimeException("Test Exception"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> requestHistoryService.deleteRequestHistory(id));
        assertEquals("An error occurred while deleting the requestHistory.", exception.getMessage());
        verify(requestHistoryRepository).findById(id);

    }

    @Test
    void testUpdateRequestHistory_RequestDoesNotExist() {
        // Arrange
        Long id = 1L;
        RequestHistoryEntity requestHistoryEntity = new RequestHistoryEntity();
        RequestEntity request = new RequestEntity();
        request.setId(999L); // ID for a request that doesn't exist
        requestHistoryEntity.setRequest(request);

        when(requestHistoryRepository.findById(id)).thenReturn(Optional.of(new RequestHistoryEntity()));
        when(requestRepository.existsById(request.getId())).thenReturn(false);

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                requestHistoryService.updateRequestHistory(id, requestHistoryEntity));

        // Replace with specific message checking if your method implementation supports it
        String expectedMessage = "An error occurred while updating the requestHistory.";
        assertTrue(thrown.getMessage().contains(expectedMessage));
    }





}
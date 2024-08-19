package com.elderlink.backend.services.impl;

import com.elderlink.backend.domains.entities.CreditTransactionEntity;
import com.elderlink.backend.domains.entities.RequestEntity;
import com.elderlink.backend.domains.entities.UserEntity;
import com.elderlink.backend.repositories.CreditTransactionRepository;
import com.elderlink.backend.repositories.RequestRepository;
import com.elderlink.backend.repositories.UserRepository;
import com.elderlink.backend.services.UserService;
import com.elderlink.backend.utils.IsUserAuthorized;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class CreditTransactionImplTest{
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;
    @Mock
    private RequestServiceImpl requestService;
    @Mock
    private RequestRepository requestRepository;

    @Mock
    private CreditTransactionRepository creditTransactionRepository;

    @Mock
    private IsUserAuthorized isUserAuthorized;

    @InjectMocks
    private CreditTransactionImpl creditTransactionService;

    private UserEntity sender;
    private CreditTransactionEntity creditTransactionEntity;
    private UserEntity recipient;
    private RequestEntity request;
    private UserEntity user;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks (this);

        sender = mock(UserEntity.class);

        recipient = mock(UserEntity.class);

        user = mock (UserEntity.class);

        request = mock (RequestEntity.class);
        when(request.getUser ()).thenReturn (user);

        creditTransactionEntity = mock (CreditTransactionEntity.class);
        when (creditTransactionEntity.getSender ()).thenReturn (sender);
        when(creditTransactionEntity.getRecipient ()).thenReturn (recipient);
        when(creditTransactionEntity.getRequest ()).thenReturn (request);
    }

    @Test
    public void testCreateCreditTransactionSufficientBalance() {

        when(userService.getUserById (anyLong ())).thenReturn (Optional.of (sender));
        when(userRepository.findById (anyLong ())).thenReturn (Optional.of (sender));

        when(requestService.findRequestById (anyLong ())).thenReturn (Optional.of (request));
        when(requestRepository.findById (anyLong ())).thenReturn (Optional.of (request));

        when(creditTransactionEntity.getSender ().getId ()).thenReturn (1L);
        when(creditTransactionEntity.getRecipient ().getId ()).thenReturn (2L);
        when(request.getUser ().getId ()).thenReturn (1L);

        doNothing().when(isUserAuthorized).checkUserAuthority(anyLong());

        when(sender.getCreditBalance ()).thenReturn (BigDecimal.valueOf (500));
        when(creditTransactionEntity.getHoursCredited ()).thenReturn (BigDecimal.valueOf (20));
        when(recipient.getCreditBalance ()).thenReturn (BigDecimal.valueOf (20));

        creditTransactionService.createCreditTransaction(creditTransactionEntity);

        assertEquals(BigDecimal.valueOf(500), sender.getCreditBalance());
        assertEquals(BigDecimal.valueOf (20), recipient.getCreditBalance());
    }

    @Test
    public void testCreateCreditTransactionInSufficientBalance() {

        when(userService.getUserById (anyLong ())).thenReturn (Optional.of (sender));
        when(userRepository.findById (anyLong ())).thenReturn (Optional.of (sender));

        when(requestService.findRequestById (anyLong ())).thenReturn (Optional.of (request));
        when(requestRepository.findById (anyLong ())).thenReturn (Optional.of (request));

        when(creditTransactionEntity.getSender ().getId ()).thenReturn (1L);
        when(creditTransactionEntity.getRecipient ().getId ()).thenReturn (2L);
        when(request.getUser ().getId ()).thenReturn (1L);

        doNothing().when(isUserAuthorized).checkUserAuthority(anyLong());

        when(sender.getCreditBalance ()).thenReturn (BigDecimal.valueOf (20));
        when(creditTransactionEntity.getHoursCredited ()).thenReturn (BigDecimal.valueOf (500));

        assertThrows (RuntimeException.class,()-> creditTransactionService.createCreditTransaction (creditTransactionEntity));
    }

    @Test
    public void testCreateCreditTransactionUserNotFound() {

        when(userService.getUserById (anyLong ())).thenReturn (Optional.empty ());

        doNothing().when(isUserAuthorized).checkUserAuthority(anyLong());

        assertThrows (EntityNotFoundException.class,()-> creditTransactionService.createCreditTransaction (creditTransactionEntity));
    }

    @Test
    public void testCreateCreditTransactionSenderIsNotTheOneWhoCreatedRequest() {

        when(userService.getUserById (anyLong ())).thenReturn (Optional.of (sender));
        when(userRepository.findById (anyLong ())).thenReturn (Optional.of (sender));

        when(requestService.findRequestById (anyLong ())).thenReturn (Optional.of (request));
        when(requestRepository.findById (anyLong ())).thenReturn (Optional.of (request));

        when(creditTransactionEntity.getSender ().getId ()).thenReturn (3L);
        when(request.getUser ().getId ()).thenReturn (2L);

        doNothing().when(isUserAuthorized).checkUserAuthority(anyLong());

        assertThrows (RuntimeException.class,()-> creditTransactionService.createCreditTransaction (creditTransactionEntity));
    }

    @Test
    void getTransactionBySenderIdSuccess() {
        Long senderId = 1L;
        List<CreditTransactionEntity> expectedTransactions = new ArrayList<>();
        expectedTransactions.add(creditTransactionEntity);

        when(userService.isUserExisted(senderId)).thenReturn(true);
        when(creditTransactionRepository.getCreditTransactionBySenderId(senderId)).thenReturn(expectedTransactions);

        List<CreditTransactionEntity> actualTransactions = creditTransactionService.getTransactionBySenderId(senderId);

        assertEquals(expectedTransactions.size(), actualTransactions.size());
        verify(creditTransactionRepository).getCreditTransactionBySenderId(senderId);
    }

    @Test
    void getTransactionBySenderIdUserNotExist() {
        Long senderId = 1L;

        when(userService.isUserExisted(senderId)).thenReturn(false);

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> creditTransactionService.getTransactionBySenderId(senderId));

        assertTrue(thrown.getMessage().contains("Sender with this id doesn't exist!"));
    }

    @Test
    void getTransactionByRecipientIdUserNotExist() {
        Long recipientId = 2L;

        when(userService.isUserExisted(recipientId)).thenReturn(false);

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> creditTransactionService.getTransactionRecipientId(recipientId));

        assertTrue(thrown.getMessage().contains("Recipient with this id doesn't exist!"));
    }


    @Test
    void testGetTransactionBySenderId_SenderNotFound() {
        // Arrange
        Long senderId = 123L;
        when(userService.isUserExisted(senderId)).thenReturn(false);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> creditTransactionService.getTransactionBySenderId(senderId));
        assertEquals("Sender with this id doesn't exist!", exception.getMessage());

        // Verify
        verify(userService).isUserExisted(senderId);
        verifyNoInteractions(creditTransactionRepository);
    }

    @Test
    void testGetTransactionBySenderId_TransactionsFound() {
        // Arrange
        Long senderId = 123L;
        when(userService.isUserExisted(senderId)).thenReturn(true);
        List<CreditTransactionEntity> mockTransactions = new ArrayList<>();
        // Populate mockTransactions as needed
        when(creditTransactionRepository.getCreditTransactionBySenderId(senderId)).thenReturn(mockTransactions);

        // Act
        List<CreditTransactionEntity> result = creditTransactionService.getTransactionBySenderId(senderId);

        // Assert
        assertSame(mockTransactions, result);

        // Verify
        verify(userService).isUserExisted(senderId);
        verify(creditTransactionRepository).getCreditTransactionBySenderId(senderId);
    }

    @Test
    void testGetTransactionRecipientId_RecipientNotFound() {
        // Arrange
        Long recipientId = 123L;
        when(userService.isUserExisted(recipientId)).thenReturn(false);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> creditTransactionService.getTransactionRecipientId(recipientId));
        assertEquals("Recipient with this id doesn't exist!", exception.getMessage());

        // Verify
        verify(userService).isUserExisted(recipientId);
        verifyNoInteractions(creditTransactionRepository);
    }

    @Test
    void testGetTransactionRecipientId_TransactionsFound() {
        // Arrange
        Long recipientId = 123L;
        when(userService.isUserExisted(recipientId)).thenReturn(true);
        List<CreditTransactionEntity> mockTransactions = new ArrayList<>();
        // Populate mockTransactions as needed
        when(creditTransactionRepository.getCreditTransactionBySenderId(recipientId)).thenReturn(mockTransactions);

        // Act
        List<CreditTransactionEntity> result = creditTransactionService.getTransactionRecipientId(recipientId);

        // Assert
        assertSame(mockTransactions, result);

        // Verify
        verify(userService).isUserExisted(recipientId);
        verify(creditTransactionRepository).getCreditTransactionBySenderId(recipientId);
    }

}

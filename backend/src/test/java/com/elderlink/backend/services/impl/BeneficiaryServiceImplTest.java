package com.elderlink.backend.services.impl;

import com.elderlink.backend.domains.entities.BeneficiaryEntity;
import com.elderlink.backend.domains.entities.UserEntity;
import com.elderlink.backend.repositories.BeneficiaryRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class BeneficiaryServiceImplTest{

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;
    @Mock
    private BeneficiaryRepository beneficiaryRepository;
    @Mock
    private IsUserAuthorized isUserAuthorized;

    @InjectMocks
    private BeneficiaryServiceImpl beneficiaryService;

    private UserEntity sender;
    private BeneficiaryEntity beneficiaryEntity;
    private UserEntity recipient;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks (this);

        sender = mock(UserEntity.class);

        recipient = mock(UserEntity.class);

        beneficiaryEntity = mock (BeneficiaryEntity.class);
        when (beneficiaryEntity.getSender ()).thenReturn (sender);
        when(beneficiaryEntity.getRecipient ()).thenReturn (recipient);
    }

    @Test
    public void testCreateBeneficiarySuccess() {

        when(userService.getUserById (anyLong ())).thenReturn (Optional.of (sender));
        when(userRepository.findById (anyLong ())).thenReturn (Optional.of (sender));

        when(beneficiaryEntity.getSender ().getId ()).thenReturn (1L);
        when(beneficiaryEntity.getRecipient ().getId ()).thenReturn (2L);

        doNothing().when(isUserAuthorized).checkUserAuthority(anyLong());

        when(sender.getCreditBalance ()).thenReturn (BigDecimal.valueOf (500));
        when(beneficiaryEntity.getHoursCredited ()).thenReturn (BigDecimal.valueOf (500));
        when(recipient.getCreditBalance ()).thenReturn (BigDecimal.valueOf (500));

        beneficiaryService.createBeneficiary (beneficiaryEntity);

        assertEquals(BigDecimal.valueOf(500), sender.getCreditBalance());
        assertEquals(BigDecimal.valueOf (500), recipient.getCreditBalance());
    }

    @Test
    public void testCreateCreditTransactionUserNotFound() {

        when(userService.getUserById (anyLong ())).thenReturn (Optional.empty ());

        doNothing().when(isUserAuthorized).checkUserAuthority(anyLong());

        assertThrows (EntityNotFoundException.class,()-> beneficiaryService.createBeneficiary (beneficiaryEntity));
    }

    @Test
    public void testCreateCreditTransactionSenderIsUnauthorized() {

        when(userService.getUserById (anyLong ())).thenReturn (Optional.of (sender));
        when(userRepository.findById (anyLong ())).thenReturn (Optional.of (sender));

        when(beneficiaryEntity.getSender ().getId ()).thenReturn (3L);

        doNothing().when(isUserAuthorized).checkUserAuthority(anyLong());

        assertThrows (RuntimeException.class,()-> beneficiaryService.createBeneficiary (beneficiaryEntity));
    }

}

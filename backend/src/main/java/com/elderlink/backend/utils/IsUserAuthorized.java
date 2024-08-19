package com.elderlink.backend.utils;

import com.elderlink.backend.domains.entities.UserEntity;
import com.elderlink.backend.exceptions.UserIsNotAuthorizedException;
import com.elderlink.backend.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class IsUserAuthorized {

    @Autowired
    private UserRepository userRepository;

    public void checkUserAuthority(Long userId){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated.");
        }

        System.out.println("Authentication Object : " + authentication);

        UserEntity existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User doesn't exist!"));

        if(!authentication.getName().equals(existingUser.getEmail())){
            throw new UserIsNotAuthorizedException("You are not authorized to perform this operation!");
        }

    }

}

package com.elderlink.backend.services.impl;

import com.elderlink.backend.domains.entities.UserEntity;
import com.elderlink.backend.exceptions.UserAlreadyExistException;
import com.elderlink.backend.repositories.UserRepository;
import com.elderlink.backend.services.UserService;

import com.elderlink.backend.utils.IsUserAuthorized;

import jakarta.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    Logger logger = LoggerFactory.getLogger(UserService.class);


    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IsUserAuthorized isUserAuthorized;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Check if a user exists based on the provided ID.
     *
     * @param id The ID of the user to check
     * @return True if the user exists, false otherwise
     */
    @Override
    public boolean isUserExisted(Long id) {
        try {
            return userRepository.existsById(id);
        }catch (Exception e){
            logger.error ("An error occurred while checking if user exists or not!. -> {}",e.getMessage ());
            throw new RuntimeException("An error occurred while checking if user exists or not.");
        }
    }
    /**
     * Check if a user exists based on the provided email.
     *
     * @param email The email of the user to check
     */

    @Override
    public void isUserExistByEmail(String email) {
        Optional<UserEntity> existedUser = userRepository.findByEmail(email);
           if(existedUser.isPresent()){
               log.warn("User with email {} already exists." , email);
               throw new UserAlreadyExistException("User with this email " + email + " already exists.");
           }
    }
    /**
     * Get a user by ID.
     *
     * @param id The ID of the user to retrieve
     * @return An Optional containing the user if found, otherwise empty
     */

    @Override
    public Optional<UserEntity> getUserById(Long id) {
        try {
            return userRepository.findById(id);
        }catch (Exception e){
            logger.error ("An error occurred while fetching the user. -> {}",e.getMessage ());
            throw new RuntimeException("An error occurred while fetching the user.");
        }
    }
    /**
     * Update a user.
     *
     * @param id         The ID of the user to update
     * @param userEntity The updated user entity
     * @return The updated user entity
     */

    @Override
    public UserEntity updateUser(Long id, UserEntity userEntity) {
        try {

            //To check user is not updating any other user's record
            isUserAuthorized.checkUserAuthority(id);

            UserEntity existingUser = userRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("User doesn't exist!"));

           //Put some condition so that user won't be able to update some specific field such as creditBalance, Birthdate etc

            if(userEntity.getEmail()!=null){
                logger.error("User can't update email field!");
                throw new RuntimeException("User can't update email field!");
            }

            if(userEntity.getUserType()!=null){
                logger.error("User can't update UserType!");
                throw new RuntimeException("User can't update UserType!");
            }

            if(userEntity.getCreditBalance()!=null){
                logger.error("User can't update creditBalance!");
                throw new RuntimeException("User can't update creditBalance!");
            }

            if(userEntity.getBirthDate()!=null){
                logger.error("User can't update birthDate!");
                throw new RuntimeException("User can't update birthDate!");
            }

            modelMapper.getConfiguration().setSkipNullEnabled(true);

            if(userEntity.getAddress() != null){
                System.out.println("After skipNull in address: " + existingUser.getAddress());
                modelMapper.map(userEntity.getAddress(),existingUser.getAddress());
            }

            if(userEntity.getPassword()!=null){
                userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            }

            modelMapper.map(userEntity,existingUser);
            System.out.println("After skipNull in user: " + existingUser);

            modelMapper.getConfiguration().setSkipNullEnabled(false);
            return userRepository.save(existingUser);

        } catch (RuntimeException e){
            logger.error("An error occurred while updating the user. -> {}",e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}

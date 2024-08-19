package com.elderlink.backend.controllers;

import com.elderlink.backend.auth.services.JwtService;
import com.elderlink.backend.domains.dto.UserDto;
import com.elderlink.backend.domains.entities.UserEntity;
import com.elderlink.backend.mappers.Mapper;
import com.elderlink.backend.repositories.UserRepository;

import com.elderlink.backend.services.UserService;
import com.elderlink.backend.utils.IsUserAuthorized;
import jakarta.validation.Valid;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UsersController {
     public static final int accessTokenStartInd = 7;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private IsUserAuthorized isUserAuthorized;

    @Autowired
    private UserService userService;

    @Autowired
    private Mapper<UserEntity,UserDto> userMapper;
    /**
     * Endpoint to get user details by email.
     *
     * @param authHeader The authorization header containing the access token
     * @return ResponseEntity containing the user details or error status
     */
    @GetMapping("/getUser")
    public ResponseEntity<UserDto> getUserByEmail(
            @RequestHeader("Authorization") String authHeader
    ){
        try{
            //Extracting the accessToken from header as token starts with "Bearer "

            String accessToken = authHeader.substring(accessTokenStartInd);
            String email = jwtService.extractUsername(accessToken);
            if(!userRepository.existsByEmail(email)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            Optional<UserEntity> currentUser= userRepository.findByEmail(email);
            if(currentUser.isPresent()){
                UserDto user = userMapper.toDto(currentUser.get());
                return ResponseEntity.ok().body(user);
            }else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    /**
     * Endpoint to update user details.
     *
     * @param userId   The ID of the user to update
     * @param userDto  The DTO containing the updated user details
     * @return ResponseEntity containing the updated user details or error status
     */
    @PatchMapping("/users/{userId}")
    public ResponseEntity<UserDto> updateUser(
            @Valid @PathVariable("userId") Long userId,
            @Valid @RequestBody UserDto userDto
    ){
            if(!userRepository.existsById(userId)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            UserEntity userEntity = userMapper.toEntity(userDto);
            System.out.println("Before skipNull : " + userDto);
            UserEntity updatedUser = userService.updateUser(userId,userEntity);
            return ResponseEntity.ok(userMapper.toDto(updatedUser));
    }

    /**
     * Retrieves a user by their email.
     *
     * @param userEmail The email of the user to retrieve.
     * @return ResponseEntity containing the user DTO if found, otherwise returns NOT_FOUND status.
     */
    @GetMapping("/user/{userEmail}")
    public ResponseEntity<UserDto> getUser(
            @Valid @PathVariable("userEmail") String userEmail
    ){
        if(!userRepository.existsByEmail (userEmail)){
            return ResponseEntity.status (HttpStatus.NOT_FOUND).build ();
        }
        Optional<UserEntity> user = userRepository.findByEmail (userEmail);
        return ResponseEntity.status (HttpStatus.OK).body (userMapper.toDto (user.get ()));
    }

    @GetMapping("/getUser/{userId}")
    public ResponseEntity<UserDto> getUserById(
            @Valid @PathVariable("userId") Long userId
    ){
        if(!userRepository.existsById (userId)){
            return ResponseEntity.status (HttpStatus.NOT_FOUND).build ();
        }
        Optional<UserEntity> user = userRepository.findById (userId);

        return ResponseEntity.status (HttpStatus.OK).body (userMapper.toDto (user.get ()));

    }

}

package com.elderlink.backend.services;

import com.elderlink.backend.domains.entities.UserEntity;

import java.util.Optional;

public interface UserService {

    public boolean isUserExisted(Long id);
    public Optional<UserEntity> getUserById(Long id);
    public void isUserExistByEmail(String email);
    public UserEntity updateUser(Long id, UserEntity userEntity);
}

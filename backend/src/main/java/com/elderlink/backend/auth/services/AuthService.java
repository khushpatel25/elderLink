package com.elderlink.backend.auth.services;

import com.elderlink.backend.auth.utils.AuthReq;
import com.elderlink.backend.auth.utils.AuthRes;
import com.elderlink.backend.domains.entities.UserEntity;

public interface AuthService {

    public AuthRes userRegister(UserEntity user);

    public AuthRes userAuth(AuthReq authReq);


}

package com.elderlink.backend.services;

import com.elderlink.backend.domains.entities.RequestEntity;

import java.util.List;
import java.util.Optional;

public interface RequestService {

    public boolean isRequestExists(Long id);
    public List<RequestEntity> getAllRequests();
    public RequestEntity createRequest(RequestEntity requestEntity);
    public Optional<RequestEntity> findRequestById(Long requestId);

    public List<RequestEntity> findRequestsByUserId(Long userId);

    public RequestEntity updateRequest(Long id, RequestEntity requestEntity);

    public void deleteRequest(Long id);
}

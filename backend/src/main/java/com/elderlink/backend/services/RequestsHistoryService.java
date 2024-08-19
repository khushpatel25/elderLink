package com.elderlink.backend.services;

import com.elderlink.backend.domains.entities.RequestHistoryEntity;

import java.util.List;

public interface RequestsHistoryService{
    public boolean isRequestHistoryExists(Long id);
    public RequestHistoryEntity createRequestHistory(RequestHistoryEntity requestHistoryEntity);
    public List<RequestHistoryEntity> getRequestHistories();
    public List<RequestHistoryEntity> getRequestHistoriesByRequestId(Long requestId);

    public List<RequestHistoryEntity> getRequestHistoriesByElderPersonId(Long elderPersonId);

    public RequestHistoryEntity updateRequestHistory(Long id,RequestHistoryEntity requestHistoryEntity);

    public void deleteRequestHistory(Long id);
}

package com.elderlink.backend.repositories;

import com.elderlink.backend.domains.entities.RequestHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestHistoryRepository extends JpaRepository<RequestHistoryEntity,Long>{

    List<RequestHistoryEntity> findByRequestId(Long requestId);

    List<RequestHistoryEntity> findByElderPersonId(Long elderPersonId);

}

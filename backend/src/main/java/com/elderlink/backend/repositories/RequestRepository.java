package com.elderlink.backend.repositories;

import com.elderlink.backend.domains.entities.RequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<RequestEntity,Long> {

    List<RequestEntity> findByUserId(Long userId);

}

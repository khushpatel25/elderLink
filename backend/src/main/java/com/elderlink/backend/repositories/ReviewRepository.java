package com.elderlink.backend.repositories;

import com.elderlink.backend.domains.entities.MessageEntity;
import com.elderlink.backend.domains.entities.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity,Long>{

    List<ReviewEntity> findByVolunteerId(Long VolunteerId);
}

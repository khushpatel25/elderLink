package com.elderlink.backend.repositories;

import com.elderlink.backend.domains.entities.BeneficiaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeneficiaryRepository extends JpaRepository<BeneficiaryEntity,Long>{
}

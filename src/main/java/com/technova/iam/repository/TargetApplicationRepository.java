package com.technova.iam.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.technova.iam.entity.TargetApplication;

public interface TargetApplicationRepository
        extends JpaRepository<TargetApplication, Long> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);
}
package com.technova.iam.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.technova.iam.entity.LifecycleEvent;

public interface LifecycleEventRepository
        extends JpaRepository<LifecycleEvent, Long> {
}
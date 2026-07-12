package com.technova.iam.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.technova.iam.dto.CreateTargetApplicationRequest;
import com.technova.iam.dto.UpdateTargetApplicationRequest;
import com.technova.iam.entity.TargetApplication;
import com.technova.iam.exception.ApplicationNotFoundException;
import com.technova.iam.exception.DuplicateApplicationException;
import com.technova.iam.repository.TargetApplicationRepository;

@Service
public class TargetApplicationService {

    private final TargetApplicationRepository targetApplicationRepository;

    public TargetApplicationService(
            TargetApplicationRepository targetApplicationRepository) {

        this.targetApplicationRepository = targetApplicationRepository;
    }

    public TargetApplication createApplication(
            CreateTargetApplicationRequest request) {

        if (targetApplicationRepository.existsByName(request.getName())) {

            throw new DuplicateApplicationException(
                    "Application name already exists: " + request.getName()
            );
        }

        TargetApplication application = new TargetApplication();

        application.setName(request.getName());
        application.setDescription(request.getDescription());
        application.setApplicationType(request.getApplicationType());
        application.setActive(true);

        return targetApplicationRepository.save(application);
    }

    public List<TargetApplication> getAllApplications() {

        return targetApplicationRepository.findAll();
    }

    public TargetApplication getApplicationById(Long id) {

        return targetApplicationRepository.findById(id)
                .orElseThrow(() ->
                        new ApplicationNotFoundException(
                                "Application not found with id: " + id
                        )
                );
    }

    public TargetApplication updateApplication(
            Long id,
            UpdateTargetApplicationRequest request) {

        TargetApplication application = getApplicationById(id);

        if (targetApplicationRepository.existsByNameAndIdNot(
                request.getName(),
                id)) {

            throw new DuplicateApplicationException(
                    "Application name already exists: " + request.getName()
            );
        }

        application.setName(request.getName());
        application.setDescription(request.getDescription());
        application.setApplicationType(request.getApplicationType());
        application.setActive(request.isActive());

        return targetApplicationRepository.save(application);
    }

    public void deleteApplication(Long id) {

        TargetApplication application = getApplicationById(id);

        targetApplicationRepository.delete(application);
    }
}
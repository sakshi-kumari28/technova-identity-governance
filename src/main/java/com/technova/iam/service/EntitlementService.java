package com.technova.iam.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.technova.iam.dto.CreateEntitlementRequest;
import com.technova.iam.dto.UpdateEntitlementRequest;
import com.technova.iam.entity.Entitlement;
import com.technova.iam.entity.TargetApplication;
import com.technova.iam.exception.ApplicationNotFoundException;
import com.technova.iam.exception.DuplicateEntitlementException;
import com.technova.iam.exception.EntitlementNotFoundException;
import com.technova.iam.repository.EntitlementRepository;
import com.technova.iam.repository.TargetApplicationRepository;

@Service
public class EntitlementService {

    private final EntitlementRepository entitlementRepository;
    private final TargetApplicationRepository targetApplicationRepository;

    public EntitlementService(
            EntitlementRepository entitlementRepository,
            TargetApplicationRepository targetApplicationRepository) {

        this.entitlementRepository = entitlementRepository;
        this.targetApplicationRepository = targetApplicationRepository;
    }

    public Entitlement createEntitlement(
            CreateEntitlementRequest request) {

        TargetApplication application = targetApplicationRepository
                .findById(request.getApplicationId())
                .orElseThrow(() ->
                        new ApplicationNotFoundException(
                                "Application not found with id: "
                                        + request.getApplicationId()
                        )
                );

        if (entitlementRepository
                .existsByEntitlementNameAndTargetApplicationId(
                        request.getEntitlementName(),
                        request.getApplicationId())) {

            throw new DuplicateEntitlementException(
                    "Entitlement already exists in this application: "
                            + request.getEntitlementName()
            );
        }

        Entitlement entitlement = new Entitlement();

        entitlement.setEntitlementName(request.getEntitlementName());
        entitlement.setDescription(request.getDescription());
        entitlement.setEntitlementType(request.getEntitlementType());
        entitlement.setActive(true);
        entitlement.setTargetApplication(application);

        return entitlementRepository.save(entitlement);
    }

    public List<Entitlement> getAllEntitlements() {

        return entitlementRepository.findAll();
    }

    public Entitlement getEntitlementById(Long id) {

        return entitlementRepository.findById(id)
                .orElseThrow(() ->
                        new EntitlementNotFoundException(
                                "Entitlement not found with id: " + id
                        )
                );
    }

    public Entitlement updateEntitlement(
            Long id,
            UpdateEntitlementRequest request) {

        Entitlement entitlement = getEntitlementById(id);

        TargetApplication application = targetApplicationRepository
                .findById(request.getApplicationId())
                .orElseThrow(() ->
                        new ApplicationNotFoundException(
                                "Application not found with id: "
                                        + request.getApplicationId()
                        )
                );

        if (entitlementRepository
                .existsByEntitlementNameAndTargetApplicationIdAndIdNot(
                        request.getEntitlementName(),
                        request.getApplicationId(),
                        id)) {

            throw new DuplicateEntitlementException(
                    "Entitlement already exists in this application: "
                            + request.getEntitlementName()
            );
        }

        entitlement.setEntitlementName(request.getEntitlementName());
        entitlement.setDescription(request.getDescription());
        entitlement.setEntitlementType(request.getEntitlementType());
        entitlement.setActive(request.isActive());
        entitlement.setTargetApplication(application);

        return entitlementRepository.save(entitlement);
    }

    public void deleteEntitlement(Long id) {

        Entitlement entitlement = getEntitlementById(id);

        entitlementRepository.delete(entitlement);
    }
}
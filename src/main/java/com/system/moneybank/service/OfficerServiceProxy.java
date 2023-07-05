package com.system.moneybank.service;

import com.system.moneybank.models.Officer;
import com.system.moneybank.repository.OfficerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OfficerServiceProxy {
    private final OfficerRepo officerRepo;

    public Optional<Officer> findById(Long id){
        return officerRepo.findById(id);
    }
}

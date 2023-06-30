package com.system.moneybank.repository;

import com.system.moneybank.models.Officer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfficerRepo extends JpaRepository<Officer, String> {
}

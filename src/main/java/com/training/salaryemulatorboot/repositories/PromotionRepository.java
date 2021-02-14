package com.training.salaryemulatorboot.repositories;

import com.training.salaryemulatorboot.entities.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {
}

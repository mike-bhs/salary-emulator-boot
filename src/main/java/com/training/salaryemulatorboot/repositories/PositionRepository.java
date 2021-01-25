package com.training.salaryemulatorboot.repositories;

import com.training.salaryemulatorboot.domain.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// H2
// @Profile() for testing and use H2
// Repository as class
@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
    @Query("SELECT p FROM Position p WHERE p.managerPosition.id = :positionId")
    List<Position> findSubordinates(@Param("positionId") Long positionId);
}

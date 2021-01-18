package com.training.salaryemulatorboot.repositories;

import com.training.salaryemulatorboot.domain.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionRepository extends JpaRepository<Position, String> {
    @Query("SELECT p FROM Position p WHERE p.managerPosition.id = :positionId")
    List<Position> findSubordinates(@Param("positionId") String positionId);
}

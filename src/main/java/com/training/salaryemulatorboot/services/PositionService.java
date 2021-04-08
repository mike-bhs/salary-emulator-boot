package com.training.salaryemulatorboot.services;

import com.training.salaryemulatorboot.entities.Position;
import com.training.salaryemulatorboot.repositories.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PositionService {
    private final PositionRepository positionRepository;

    public Optional<Position> findById(Long id) {
        if (id != null) {
            return positionRepository.findById(id);
        } else {
            return Optional.empty();
        }
    }
}

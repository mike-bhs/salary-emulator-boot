package com.training.salaryemulatorboot.services;

import com.training.salaryemulatorboot.entities.Position;
import com.training.salaryemulatorboot.repositories.PositionRepository;
import helpers.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class PositionServiceTest {
    @Mock
    private PositionRepository positionRepository;

    @InjectMocks
    private PositionService positionService;

    private Position position;

    @BeforeEach
    public void setUp() {
        position = Factory.getPosition();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void Should_Return_Employee_By_Id() {
        when(positionRepository.findById(position.getId())).thenReturn(Optional.of(position));

        Optional<Position> positionOptional = positionService.findById(position.getId());

        verify(positionRepository, times(1)).findById(position.getId());

        assertTrue(positionOptional.isPresent());
        assertEquals(position, positionOptional.get());
    }

    @Test
    public void Should_Return_Empty_Optional_When_Id_Is_Null() {
        Optional<Position> positionOptional = positionService.findById(null);
        assertTrue(positionOptional.isEmpty());
    }
}

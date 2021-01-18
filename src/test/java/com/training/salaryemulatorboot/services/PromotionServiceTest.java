package com.training.salaryemulatorboot.services;

import com.training.salaryemulatorboot.domain.Employee;
import com.training.salaryemulatorboot.domain.Position;
import com.training.salaryemulatorboot.domain.Promotion;
import com.training.salaryemulatorboot.repositories.PromotionRepository;
import helpers.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class PromotionServiceTest {
    @Mock
    private PromotionRepository promotionRepository;

    @InjectMocks
    private PromotionService promotionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void Should_Create_Promotion_When_Valid_Data() {
        Promotion expectedPromotion = Factory.getPromotion();
        Employee employee = Factory.getEmployee();
        UUID promotionUuid = UUID.fromString(expectedPromotion.getId());
        Position newPosition = expectedPromotion.getNewPosition();
        Date promotionDate = expectedPromotion.getPromotionDate();
        BigDecimal newSalaryAmount = expectedPromotion.getNewSalaryAmount();

        try (MockedStatic<UUID> uuidMock = Mockito.mockStatic(UUID.class)) {
            uuidMock.when(UUID::randomUUID).thenReturn(promotionUuid);
            when(promotionRepository.save(expectedPromotion)).thenReturn(expectedPromotion);

            Promotion actualPromotion = promotionService.createPromotion(employee, newPosition, newSalaryAmount, promotionDate);

            verify(promotionRepository, times(1)).save(expectedPromotion);
            assertEquals(expectedPromotion, actualPromotion);
        }
    }
}
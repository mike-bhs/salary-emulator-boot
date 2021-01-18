package com.training.salaryemulatorboot.repositries;

import com.training.salaryemulatorboot.domain.Position;
import com.training.salaryemulatorboot.repositories.PositionRepository;

import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertLinesMatch;

//@SpringBootTest
@Disabled
public class PositionRepositoryTest {
    @Autowired
    private PositionRepository positionRepository;

    @Test
    public void Should_FindAllRecords_When_Present() {
        setupTestData();

        List<Position> positions = positionRepository.findSubordinates("c277ce43-0eac-40a8-8e55-822df059f736");
        List<String> actualIds = positions.stream().map(Position::getId).collect(Collectors.toList());
        List<String> expectedIds = Arrays.asList("5620fc95-8f8b-4baa-b73d-9fddc7f0e747", "ffcf0e57-d05c-4e58-83b1-a60d845b0962");

        assertLinesMatch(actualIds, expectedIds);

        cleanupTestData();
    }

    @Test
    public void Should_ReturnEmptyList_When_RecordsMissing() {
        List<Position> positions = positionRepository.findSubordinates("c277ce43-0eac-40a8-8e55-822df059f736");

        Assertions.assertTrue(positions.isEmpty());
    }

    private void setupTestData() {
        Position manager = createPosition("c277ce43-0eac-40a8-8e55-822df059f736", "manager",  null);
        createPosition("5620fc95-8f8b-4baa-b73d-9fddc7f0e747", "consultant", manager);
        createPosition("ffcf0e57-d05c-4e58-83b1-a60d845b0962", "consultant", manager);
    }

    private void cleanupTestData() {
        positionRepository.deleteById("ffcf0e57-d05c-4e58-83b1-a60d845b0962");
        positionRepository.deleteById("5620fc95-8f8b-4baa-b73d-9fddc7f0e747");
        positionRepository.deleteById("c277ce43-0eac-40a8-8e55-822df059f736");
    }

    private Position createPosition(String id, String name, Position managerPosition) {
        Position p = new Position();
        p.setId(id);
        p.setName(name);
        p.setManagerPosition(managerPosition);

        return positionRepository.saveAndFlush(p);
    }
}

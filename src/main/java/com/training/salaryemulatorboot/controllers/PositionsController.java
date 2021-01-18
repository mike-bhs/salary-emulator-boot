package com.training.salaryemulatorboot.controllers;

import com.training.salaryemulatorboot.domain.Position;
import com.training.salaryemulatorboot.repositories.PositionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PositionsController {
    @Autowired
    private PositionRepository positionRepository;

    Logger logger = LoggerFactory.getLogger(PositionsController.class);

    @GetMapping("/positions")
    public ResponseEntity<List<Position>> getAllPositions() {
        return new ResponseEntity<>(positionRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping("/positions")
    public ResponseEntity<Object> createPosition(@Valid @RequestBody Position position) {
        Position p = new Position();
        p.setId(UUID.randomUUID().toString());
        p.setName(position.getName());

        try {
            Position newPosition = positionRepository.save(p);
            return new ResponseEntity<>(newPosition, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error(String.format("Failed to create position: %s", e.getMessage()));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/positions/{id}")
    public ResponseEntity<HttpStatus> deletePosition(@PathVariable("id") String id) {
        try {
            positionRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<Object> handleConstraintViolationException(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new LinkedHashMap<>();

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        body.put("errors", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
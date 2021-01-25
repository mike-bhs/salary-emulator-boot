package com.training.salaryemulatorboot.controllers;

import com.training.salaryemulatorboot.dto.EmployeeDTO;
import com.training.salaryemulatorboot.dto.PromotionDTO;
import com.training.salaryemulatorboot.repositories.EmployeeRepository;
import com.training.salaryemulatorboot.services.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeesController {
    private final EmployeeRepository employeeRepository;
    private final EmployeeService employeeService;
    private final Logger logger;

    public EmployeesController(EmployeeRepository empRepo, EmployeeService empService) {
        this.employeeRepository = empRepo;
        this.employeeService = empService;
        this.logger = LoggerFactory.getLogger(EmployeesController.class);
    }

    @GetMapping(produces = {"application/json"})
    public List<EmployeeDTO> getAllEmployees() { return employeeService.getAllEmployees(); }

    @GetMapping(path = {"/{employeeId}"}, produces = {"application/json"})
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable("employeeId") Long employeeId) {
        Optional<EmployeeDTO> employeeData = employeeService.findById(employeeId);

        return employeeData
                .map(employeeDTO -> new ResponseEntity<>(employeeDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // TODO add validation later
    @PostMapping(produces = {"application/json"})
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO newEmployee = employeeService.createEmployee(employeeDTO);

        return new ResponseEntity<>(newEmployee, HttpStatus.CREATED);
    }

    @PutMapping(path = {"/{employeeId}/promote"}, produces = {"application/json"})
    public ResponseEntity<EmployeeDTO> promoteEmployee(@PathVariable("employeeId") Long employeeId,
                                                       @RequestBody PromotionDTO promotionDTO) {

        Optional<EmployeeDTO> employeeData = employeeService.promoteEmployee(employeeId, promotionDTO);

        return employeeData
                .map(employeeDTO -> new ResponseEntity<>(employeeDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<Object> handleConstraintViolationException(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new LinkedHashMap<>();

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        body.put("errors", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}

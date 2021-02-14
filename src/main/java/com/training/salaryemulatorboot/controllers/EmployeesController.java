package com.training.salaryemulatorboot.controllers;

import com.training.salaryemulatorboot.dto.EmployeeDto;
import com.training.salaryemulatorboot.dto.PromotionDto;
import com.training.salaryemulatorboot.entities.Employee;
import com.training.salaryemulatorboot.mappers.EmployeeMapper;
import com.training.salaryemulatorboot.services.EmployeeService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class EmployeesController {
    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    @GetMapping(produces = {"application/json"})
    public List<EmployeeDto> getAllEmployees() {
        return employeeMapper.toEmployeeDtos(employeeService.getAllEmployees());
    }

    @GetMapping(path = {"/{employeeId}"}, produces = {"application/json"})
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable("employeeId") Long employeeId) {
        Optional<Employee> employeeOptional = employeeService.findById(employeeId);

        return employeeOptional
                .map(emp -> new ResponseEntity<>(employeeMapper.toEmployeeDto(emp), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // TODO add validation later
    @PostMapping(produces = {"application/json"})
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeDto employeeDto) {
        Employee newEmployee = employeeService.createEmployee(employeeDto);

        return new ResponseEntity<>(employeeMapper.toEmployeeDto(newEmployee), HttpStatus.CREATED);
    }

    @PutMapping(path = {"/{employeeId}/promote"}, produces = {"application/json"})
    public ResponseEntity<EmployeeDto> promoteEmployee(@PathVariable("employeeId") Long employeeId,
                                                       @RequestBody PromotionDto promotionDTO) {

        Optional<Employee> employeeOptional = employeeService.promoteEmployee(employeeId, promotionDTO);

        return employeeOptional
                .map(emp -> new ResponseEntity<>(employeeMapper.toEmployeeDto(emp), HttpStatus.OK))
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

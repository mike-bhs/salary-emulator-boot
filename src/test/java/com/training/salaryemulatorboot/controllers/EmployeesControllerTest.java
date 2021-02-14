package com.training.salaryemulatorboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.salaryemulatorboot.dto.EmployeeDto;
import com.training.salaryemulatorboot.dto.PromotionDto;
import com.training.salaryemulatorboot.entities.Employee;
import com.training.salaryemulatorboot.mappers.EmployeeMapper;
import com.training.salaryemulatorboot.services.EmployeeService;
import helpers.Factory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeesController.class)
public class EmployeesControllerTest {

    @MockBean
    EmployeeService employeeService;

    @MockBean
    EmployeeMapper employeeMapper;

    @Autowired
    MockMvc mockMvc;

    Employee employee;
    PromotionDto promotionDto;
    EmployeeDto employeeDto;

    @BeforeEach
    void setUp() {
        employee = Factory.getEmployee();

        promotionDto = new PromotionDto();
        promotionDto.setNewPositionId(121L);
        promotionDto.setNewSalaryAmount(new BigDecimal("2700"));
        promotionDto.setPromotionDate(new Date());

        employeeDto = Factory.getEmployeeDto();
    }

    @AfterEach
    public void tearDown() {
        reset(employeeService);
        reset(employeeMapper);
    }

    @Test
    public void Should_Return_Employee_List() throws Exception {
        List<Employee> employeeList = Collections.singletonList(employee);

        when(employeeService.getAllEmployees()).thenReturn(employeeList);
        when(employeeMapper.toEmployeeDtos(employeeList)).thenReturn(Collections.singletonList(employeeDto));

        mockMvc.perform(get("/api/v1/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$.[0].id", is(employeeDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(employeeDto.getName())))
                .andExpect(jsonPath("$.[0].positionId", is(employeeDto.getPositionId()), Long.class))
                .andExpect(jsonPath("$.[0].salaryAmount", is(employeeDto.getSalaryAmount()), BigDecimal.class))
                .andExpect(jsonPath("$.[0].salaryCurrency", is(employeeDto.getSalaryCurrency())));

        verify(employeeService, times(1)).getAllEmployees();
        verify(employeeMapper, times(1)).toEmployeeDtos(employeeList);
    }

    @Test
    public void Should_Return_Employee_By_Id() throws Exception {
        when(employeeService.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(employeeMapper.toEmployeeDto(employee)).thenReturn(employeeDto);

        mockMvc.perform(get("/api/v1/employees/101"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(employeeDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(employeeDto.getName())))
                .andExpect(jsonPath("$.positionId", is(employeeDto.getPositionId()), Long.class))
                .andExpect(jsonPath("$.salaryAmount", is(employeeDto.getSalaryAmount()), BigDecimal.class))
                .andExpect(jsonPath("$.salaryCurrency", is(employeeDto.getSalaryCurrency())));

        verify(employeeService, times(1)).findById(employee.getId());
        verify(employeeMapper, times(1)).toEmployeeDto(employee);
    }

    @Test
    public void Should_Return_Empty_Payload() throws Exception {
        when(employeeService.findById(1L)).thenReturn(Optional.empty());

        String responseContent = mockMvc.perform(get("/api/v1/employees/1"))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertTrue(responseContent.isEmpty());

        verify(employeeService, times(1)).findById(1L);
    }

    @Test
    public void Should_Create_Employee() throws  Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String payloadJson = objectMapper.writeValueAsString(employeeDto);

        when(employeeService.createEmployee(employeeDto)).thenReturn(employee);
        when(employeeMapper.toEmployeeDto(employee)).thenReturn(employeeDto);

        mockMvc.perform(post("/api/v1/employees").contentType(MediaType.APPLICATION_JSON).content(payloadJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(employeeDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(employeeDto.getName())))
                .andExpect(jsonPath("$.positionId", is(employeeDto.getPositionId()), Long.class))
                .andExpect(jsonPath("$.salaryAmount", is(employeeDto.getSalaryAmount()), BigDecimal.class))
                .andExpect(jsonPath("$.salaryCurrency", is(employeeDto.getSalaryCurrency())));

        verify(employeeService, times(1)).createEmployee(employeeDto);
        verify(employeeMapper, times(1)).toEmployeeDto(employee);
    }

    @Test
    public void Should_Promote_Employee() throws Exception {
        Long employeeId = 20L;
        ObjectMapper objectMapper = new ObjectMapper();
        String payloadJson = objectMapper.writeValueAsString(promotionDto);

        when(employeeService.promoteEmployee(employeeId, promotionDto)).thenReturn(Optional.of(employee));
        when(employeeMapper.toEmployeeDto(employee)).thenReturn(employeeDto);

        mockMvc.perform(put("/api/v1/employees/20/promote").contentType(MediaType.APPLICATION_JSON).content(payloadJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(employeeDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(employeeDto.getName())))
                .andExpect(jsonPath("$.positionId", is(employeeDto.getPositionId()), Long.class))
                .andExpect(jsonPath("$.salaryAmount", is(employeeDto.getSalaryAmount()), BigDecimal.class))
                .andExpect(jsonPath("$.salaryCurrency", is(employeeDto.getSalaryCurrency())));

        verify(employeeMapper, times(1)).toEmployeeDto(employee);
        verify(employeeService, times(1)).promoteEmployee(employeeId, promotionDto);
    }
}

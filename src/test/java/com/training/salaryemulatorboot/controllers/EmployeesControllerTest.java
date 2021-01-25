package com.training.salaryemulatorboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.salaryemulatorboot.dto.EmployeeDTO;
import com.training.salaryemulatorboot.dto.PromotionDTO;
import com.training.salaryemulatorboot.repositories.EmployeeRepository;
import com.training.salaryemulatorboot.services.EmployeeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
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
    EmployeeRepository employeeRepository;

    @Autowired
    MockMvc mockMvc;

    EmployeeDTO employeeDTO;
    PromotionDTO promotionDTO;

    @BeforeEach
    void setUp() {
        employeeDTO = EmployeeDTO.builder()
                .id(100L)
                .name("John")
                .positionId(120L)
                .salaryAmount(new BigDecimal("2343.25"))
                .salaryCurrency("USD")
                .build();

        promotionDTO = new PromotionDTO();
        promotionDTO.setNewPositionId(121L);
        promotionDTO.setNewSalaryAmount(new BigDecimal("2700"));
        promotionDTO.setPromotionDate(new Date());
    }

    @AfterEach
    public void tearDown() {
        reset(employeeService);
        reset(employeeRepository);
    }

    @Test
    public void Should_Return_Employee_List() throws Exception {
        when(employeeService.getAllEmployees()).thenReturn(Arrays.asList(employeeDTO));

        mockMvc.perform(get("/api/v1/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$.[0].id", is(employeeDTO.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(employeeDTO.getName())))
                .andExpect(jsonPath("$.[0].positionId", is(employeeDTO.getPositionId()), Long.class))
                .andExpect(jsonPath("$.[0].salaryAmount", is(employeeDTO.getSalaryAmount()), BigDecimal.class))
                .andExpect(jsonPath("$.[0].salaryCurrency", is(employeeDTO.getSalaryCurrency())));

        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    public void Should_Return_Employee_By_Id() throws Exception {
        when(employeeService.findById(employeeDTO.getId())).thenReturn(Optional.of(employeeDTO));

        mockMvc.perform(get("/api/v1/employees/100"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(employeeDTO.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(employeeDTO.getName())))
                .andExpect(jsonPath("$.positionId", is(employeeDTO.getPositionId()), Long.class))
                .andExpect(jsonPath("$.salaryAmount", is(employeeDTO.getSalaryAmount()), BigDecimal.class))
                .andExpect(jsonPath("$.salaryCurrency", is(employeeDTO.getSalaryCurrency())));

        verify(employeeService, times(1)).findById(employeeDTO.getId());
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
        String payloadJson = objectMapper.writeValueAsString(employeeDTO);
        when(employeeService.createEmployee(employeeDTO)).thenReturn(employeeDTO);

        mockMvc.perform(post("/api/v1/employees").contentType(MediaType.APPLICATION_JSON).content(payloadJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(employeeDTO.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(employeeDTO.getName())))
                .andExpect(jsonPath("$.positionId", is(employeeDTO.getPositionId()), Long.class))
                .andExpect(jsonPath("$.salaryAmount", is(employeeDTO.getSalaryAmount()), BigDecimal.class))
                .andExpect(jsonPath("$.salaryCurrency", is(employeeDTO.getSalaryCurrency())));

        verify(employeeService, times(1)).createEmployee(employeeDTO);
    }

    @Test
    public void Should_Promote_Employee() throws Exception {
        Long employeeId = 20L;
        ObjectMapper objectMapper = new ObjectMapper();
        String payloadJson = objectMapper.writeValueAsString(promotionDTO);

        when(employeeService.promoteEmployee(employeeId, promotionDTO)).thenReturn(Optional.of(employeeDTO));

        mockMvc.perform(put("/api/v1/employees/20/promote").contentType(MediaType.APPLICATION_JSON).content(payloadJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(employeeDTO.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(employeeDTO.getName())))
                .andExpect(jsonPath("$.positionId", is(employeeDTO.getPositionId()), Long.class))
                .andExpect(jsonPath("$.salaryAmount", is(employeeDTO.getSalaryAmount()), BigDecimal.class))
                .andExpect(jsonPath("$.salaryCurrency", is(employeeDTO.getSalaryCurrency())));

        verify(employeeService, times(1)).promoteEmployee(employeeId, promotionDTO);
    }
}

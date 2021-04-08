package com.training.salaryemulatorboot.repositories;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class EmployeeAuditRepositoryIntegrationTest {
    @Autowired
    EmployeeAuditRepository repository;

    @SneakyThrows
    @Sql(scripts = {"classpath:seed_employee_audits.sql"})
    @Sql(scripts = {"classpath:cleanup_employee_audits.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void Should_Count_Audits_Correctly() {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long subordinatesCount = repository.countSubordinatesOnDate(10001L, sdf.parse("2021-01-16"));

        assertEquals(2, subordinatesCount);
    }
}
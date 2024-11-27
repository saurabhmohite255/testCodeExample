package com.codeBuSaurabh.TestCode.testCodeExample.repository;

import com.codeBuSaurabh.TestCode.testCodeExample.entities.EmployeeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class EmployeeRepositoryTest {
    @Autowired
    private EmployeeRepository employeeRepository;

    private EmployeeEntity employee;

    @BeforeEach
    void seUp() {
        employee = EmployeeEntity.builder()
                .id(1L)
                .email("saurabh@gmail")
                .name("Saurabh")
                .salary(100000.20)
                .build();
    }

    @Test
    void testFindByEmail_whenEmployeeIsFound_thenReturnEmployeeList() {
        //Arrange or Given
        employeeRepository.save(employee);

        //Act or When
        List<EmployeeEntity> employeeEntityList = employeeRepository.findByEmail(employee.getEmail());

        //Assert or Then
        assertThat(employeeEntityList).isNotNull();
        assertThat(employeeEntityList).isNotEmpty();
        assertThat(employeeEntityList.get(0).getEmail()).isEqualTo(employee.getEmail());
    }

    @Test
    void testFindByEmail_whenEmployeeISNOtFound_thenReturnEmptyList() {
        //arrange  or Given
        String email="notpresnt255@gamil.com";

        //Act or When
        List<EmployeeEntity> employeeEntityList=employeeRepository.findByEmail(employee.getEmail());

        //Assert or then
        assertThat(employeeEntityList).isNotNull();
        assertThat(employeeEntityList).isEmpty();

    }
}
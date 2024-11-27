package com.codeBuSaurabh.TestCode.testCodeExample.controller;

import com.codeBuSaurabh.TestCode.testCodeExample.dto.EmployeeDto;
import com.codeBuSaurabh.TestCode.testCodeExample.entities.EmployeeEntity;
import com.codeBuSaurabh.TestCode.testCodeExample.repository.EmployeeRepository;
import com.codeBuSaurabh.TestCode.testCodeExample.services.EmployeeServices;
import jakarta.annotation.Priority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "100000")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class EmployeeControllerTestIT {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private EmployeeRepository employeeRepository;

    private EmployeeEntity testemployeeEntity;

    private EmployeeDto testemployeeDto;

    @BeforeEach
    void setUp(){
        testemployeeEntity= EmployeeEntity.builder()
                .id(1L)
                .email("saurabh@gamil.com")
                .name("Saurabh")
                .salary(150000D)
                .build();
        testemployeeDto=EmployeeDto.builder()
                .id(1L)
                .email("saurabh@gamil.com")
                .name("Saurabh")
                .salary(150000D)
                .build();
        employeeRepository.deleteAll();

    }

    @Test
    void testGetEmployeeBuId_Success(){
        EmployeeEntity saveEmployee=employeeRepository.save(testemployeeEntity);
        webTestClient.get()
                .uri("/employee/{id}",saveEmployee.getId())
                .exchange()               //this method call api
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(saveEmployee.getId())
                .jsonPath("$.email").isEqualTo(saveEmployee.getEmail());


    }
    @Test
    void testEmployeeById_Failure(){
        webTestClient.get()
                .uri("/employee/1")
                .exchange()
                .expectStatus().isNotFound();
    }
    @Test
    void testCreateNewEmployee_WhenEmployeeAlreadyExist_ThenThrowException(){
        EmployeeEntity savedEmployee=employeeRepository.save(testemployeeEntity);
        webTestClient.post()
                .uri("/employee")
                .bodyValue(testemployeeDto)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void testCreateNewEmployee_WhenEmployeeDoesNotExist_ThenCreateNewEmployee(){
        //EmployeeEntity savedEmployee=employeeRepository.save(testemployeeEntity);
        webTestClient.post()
                .uri("/employee")
                .bodyValue(testemployeeDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.email").isEqualTo(testemployeeDto.getEmail())
                .jsonPath("$.name").isEqualTo(testemployeeDto.getName());
    }

    @Test
    void testUpdateEmployee_whenEmployeeIdDoesNotExist_thenThrowException(){
        webTestClient.put()
                .uri("/employee/233")
                .bodyValue(testemployeeDto)
                .exchange()
                .expectStatus().isNotFound();
    }
    @Test
    void testUpdateEmployee_WhenAttemptingWithExistingEmail(){
        EmployeeEntity saveEmployee=employeeRepository.save(testemployeeEntity);

        testemployeeDto.setName("sham");
        testemployeeDto.setEmail("sham@gamail.com");

        webTestClient.put()
                .uri("/employee/{id}",saveEmployee.getId())
                .bodyValue(testemployeeDto)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(EmployeeDto.class);
    }

    @Test
    void testUpdateEmployee_whenEmployeeExist_ThenUpdateEmployee(){
        EmployeeEntity saveEmployee=employeeRepository.save(testemployeeEntity);
        testemployeeDto.setName("Ram");
        testemployeeDto.setSalary(70000D);
        webTestClient.put()
                .uri("/employee/{id}",saveEmployee.getId())
                .bodyValue(testemployeeDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EmployeeDto.class)
                .isEqualTo(testemployeeDto);

    }
    @Test
    void testDeleteEmployee_WhenEmployeeDoesNotExist_ThenThrowException(){
        webTestClient.delete()
                .uri("/employee/123")
                .exchange()
                .expectStatus().isNotFound();
    }
    @Test
    void testDeleteEmployee_WhenEmployeeISValid_thenDeleteEmployee(){
        EmployeeEntity saveEmployee=employeeRepository.save(testemployeeEntity);
        webTestClient.delete()
                .uri("/employee/{id}",saveEmployee.getId())
                .exchange()
                .expectStatus().isNoContent()
                .expectBody(Void.class);
    }

}
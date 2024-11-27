package com.codeBuSaurabh.TestCode.testCodeExample.services.implimentation;

import com.codeBuSaurabh.TestCode.testCodeExample.dto.EmployeeDto;
import com.codeBuSaurabh.TestCode.testCodeExample.entities.EmployeeEntity;
import com.codeBuSaurabh.TestCode.testCodeExample.exceptions.ResourceNotFoundException;
import com.codeBuSaurabh.TestCode.testCodeExample.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
class EmployeeServiceImplementationTest {
    @Mock
    EmployeeRepository employeeRepository;

    @Spy
    ModelMapper modelMapper;

    @InjectMocks
    EmployeeServiceImplementation serviceImplementation;

   private EmployeeDto mockEmployeeDto;

   private EmployeeEntity mockEmployee;

    @BeforeEach
    void setUp(){
         mockEmployee=EmployeeEntity.builder()
                .id(1L)
                .email("saurabh@gamil.com")
                .name("Saurabh")
                .salary(150000D)
                .build();

         mockEmployeeDto=modelMapper.map(mockEmployee, EmployeeDto.class);
    }


    @Test
    void getEmployeeById_WhenIdIsValid_ReturnEmployeeDto(){
        //Arrange
        Long id=1L;

        when(employeeRepository.findById(id)).thenReturn(Optional.of(mockEmployee));

        //Act
        EmployeeDto employeeDto=serviceImplementation.getEmployeeById(id);

        //assert
        assertThat(employeeDto.getId()).isEqualTo(id);
        assertThat(employeeDto.getEmail()).isEqualTo(mockEmployee.getEmail());
        verify(employeeRepository,atLeast(1)).findById(1L);

    }

    @Test
    void getEmployeeById_WhenEmployeeNotFound_ThrowException(){
        //arrange or Given
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        //act and assert
        assertThatThrownBy(()->serviceImplementation.getEmployeeById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id :1");
        verify(employeeRepository).findById(1L);
    }

    @Test
    void testCreateNewEmployee_WhenEmployeeisCreated_ThenCreateNewEmployee(){
        //assign
        when(employeeRepository.findByEmail(any())).thenReturn(List.of());
        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(mockEmployee);

        //act
        EmployeeDto employeeDto=serviceImplementation.createNewEmployee(mockEmployeeDto);

        //assert

        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getEmail()).isEqualTo(mockEmployeeDto.getEmail());

        ArgumentCaptor<EmployeeEntity> argumentCaptor=ArgumentCaptor.forClass(EmployeeEntity.class);
        verify(employeeRepository).save(argumentCaptor.capture());

        EmployeeEntity capturedEmployee=argumentCaptor.getValue();
        assertThat(capturedEmployee.getEmail()).isEqualTo(mockEmployee.getEmail());

    }

    @Test
    void testCreateNewEmployee_WhenAttemptingToCreateEmployeeWithExistingEmail_ThenThrowException(){
        //arrange
        when(employeeRepository.findByEmail(mockEmployeeDto.getEmail())).thenReturn(List.of(mockEmployee));

        //act and Assert
        assertThatThrownBy(()->serviceImplementation.createNewEmployee(mockEmployeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Employee alredy exist with this email:"+mockEmployee.getEmail());
        verify(employeeRepository).findByEmail(mockEmployeeDto.getEmail());
        verify(employeeRepository,never()).save(any());
    }

    @Test
    void testUpdateEmployee_WhenEmployeeDoesNotExist_ThenThrowException(){
        //Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        //act and assert
        assertThatThrownBy(()->serviceImplementation.updateEmployee(1L,mockEmployeeDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee Not found with this id:1");
        verify(employeeRepository).findById(1L);
        verify(employeeRepository,never()).save(any());
    }

    @Test
    void testUpdateEmployee_WhenAttemptToUpdateEmail_ThenThrowException(){
        //arrange;
        when(employeeRepository.findById(mockEmployeeDto.getId())).thenReturn(Optional.of(mockEmployee));
        mockEmployee.setName("Sham");
        mockEmployee.setEmail("sham@gmail.com");

        //act And assert
        assertThatThrownBy(()->serviceImplementation.updateEmployee(mockEmployeeDto.getId(),mockEmployeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("The Email of the employee cannot be updated");
        verify(employeeRepository).findById(mockEmployeeDto.getId());
        verify(employeeRepository,never()).save(any());
    }

    @Test
    void updateEmployee_WhenEmployeeISUpdated_ReturnEmployeeDto(){
        //arrange
        when(employeeRepository.findById(mockEmployeeDto.getId())).thenReturn(Optional.of(mockEmployee));
        mockEmployeeDto.setName("Sham");
        mockEmployeeDto.setSalary(70000D);
        EmployeeEntity nweEmployee=modelMapper.map(mockEmployeeDto, EmployeeEntity.class);
        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(nweEmployee);

        //act
        EmployeeDto updatedEmployeeDto=serviceImplementation.updateEmployee(mockEmployeeDto.getId(),mockEmployeeDto);

        //assert
        assertThat(updatedEmployeeDto).isEqualTo(mockEmployeeDto);
        verify(employeeRepository).findById(1L);
        verify(employeeRepository).save(any());

    }

    @Test
    void testDeleteEmployee_IfEmployeeDoesNotExist_ThenThrowException(){
        //arrange
        when(employeeRepository.existsById(mockEmployeeDto.getId())).thenReturn(false);

        //act and assert
        assertThatThrownBy(()->serviceImplementation.deleateEmployee(mockEmployeeDto.getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with this id:"+mockEmployeeDto.getId());
        verify(employeeRepository,never()).deleteById(anyLong());
    }

    @Test
    void testDeleteEmployee_WhenEmployeeIsExisting_thenDeleteEmployee(){
        //arrange
        when(employeeRepository.existsById(mockEmployeeDto.getId())).thenReturn(true);

        //assert
        assertThatCode(()->serviceImplementation.deleateEmployee(mockEmployeeDto.getId()))
                .doesNotThrowAnyException();

        //verify
        verify(employeeRepository).deleteById(mockEmployeeDto.getId());
    }

}
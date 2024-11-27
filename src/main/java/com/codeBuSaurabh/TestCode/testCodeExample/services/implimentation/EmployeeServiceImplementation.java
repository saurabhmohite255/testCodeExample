package com.codeBuSaurabh.TestCode.testCodeExample.services.implimentation;

import com.codeBuSaurabh.TestCode.testCodeExample.dto.EmployeeDto;
import com.codeBuSaurabh.TestCode.testCodeExample.entities.EmployeeEntity;
import com.codeBuSaurabh.TestCode.testCodeExample.exceptions.ResourceNotFoundException;
import com.codeBuSaurabh.TestCode.testCodeExample.repository.EmployeeRepository;
import com.codeBuSaurabh.TestCode.testCodeExample.services.EmployeeServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImplementation implements EmployeeServices {

    private final EmployeeRepository employeeRepository;

    private final ModelMapper modelMapper;

    @Override
    public EmployeeDto getEmployeeById(Long id) {
        log.info("Fetching the employee by its id :"+id);
        EmployeeEntity employee=employeeRepository.findById(id)
                .orElseThrow(()->{
                    log.error("Employee not found with id: {}",id);
                    return new ResourceNotFoundException("Employee not found with id :"+id);
                });
        log.info("Sucessesfully fethed with id{}",id);
        return modelMapper.map(employee,EmployeeDto.class);
    }

    @Override
    public EmployeeDto createNewEmployee(EmployeeDto employeeDto) {
        log.info("Create new employee with email :{}",employeeDto.getEmail());
        List<EmployeeEntity> existingEmployee=employeeRepository.findByEmail(employeeDto.getEmail());
        if(!existingEmployee.isEmpty()){
            log.error("Employee is already existing with this email :{}",employeeDto.getEmail());
            throw  new  RuntimeException("Employee alredy exist with this email:"+employeeDto.getEmail());
        }
        EmployeeEntity newEmployee=modelMapper.map(employeeDto, EmployeeEntity.class);
        EmployeeEntity savedEmployee=employeeRepository.save(newEmployee);
        log.info("Successfully saved New Employee :{}",savedEmployee.getId());
        return modelMapper.map(savedEmployee,EmployeeDto.class);
    }

    @Override
    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) {
        log.info("Update the Employee with id :{}",id);
        EmployeeEntity employee=employeeRepository.findById(id)
                .orElseThrow(()->{
                   log.error("Employee not found with this id :{}",id);
                   return new ResourceNotFoundException("Employee Not found with this id:"+id);
                });
        if(!employee.getEmail().equals(employeeDto.getEmail())){
            log.error("Attempted to update email with id:",id);
            throw new RuntimeException("The Email of the employee cannot be updated");
        }
        modelMapper.map(employeeDto, employee);
        employeeDto.setId(id);
        EmployeeEntity saveEmployee=employeeRepository.save(employee);
        log.info("Employee updated with id :{}",id);
        return modelMapper.map(saveEmployee, EmployeeDto.class);
    }

    @Override
    public void deleateEmployee(Long id) {
        log.info("Delete Employee with this id :{}",id);
        boolean exist=employeeRepository.existsById(id);
        if(!exist){
            log.error("employee not found with this id ;{}",id);
            throw new ResourceNotFoundException("Employee not found with this id:"+id);
        }
        employeeRepository.deleteById(id);
        log.info("Employee deleted Successfully with id :{}",id);

    }
}

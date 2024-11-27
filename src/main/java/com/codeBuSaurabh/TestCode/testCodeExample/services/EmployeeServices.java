package com.codeBuSaurabh.TestCode.testCodeExample.services;

import com.codeBuSaurabh.TestCode.testCodeExample.dto.EmployeeDto;
import org.springframework.stereotype.Service;

@Service
public interface EmployeeServices {
    EmployeeDto getEmployeeById(Long id);
    EmployeeDto createNewEmployee(EmployeeDto employeeDto);
    EmployeeDto updateEmployee(Long id,EmployeeDto employeeDto);
    void deleateEmployee(Long id);

}

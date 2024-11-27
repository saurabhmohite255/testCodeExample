package com.codeBuSaurabh.TestCode.testCodeExample.controller;

import com.codeBuSaurabh.TestCode.testCodeExample.dto.EmployeeDto;
import com.codeBuSaurabh.TestCode.testCodeExample.services.EmployeeServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employee")
public class EmployeeController {
    private final EmployeeServices employeeServices;

    @GetMapping("{id}")
   public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long id){
        EmployeeDto employeeDto=employeeServices.getEmployeeById(id);
        return ResponseEntity.ok(employeeDto);
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> createNewEmployee(@RequestBody EmployeeDto employeeDto){
        EmployeeDto createdEmployee=employeeServices.createNewEmployee(employeeDto);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Long id,@RequestBody EmployeeDto employeeDto){
        EmployeeDto updatedEmployee=employeeServices.updateEmployee(id,employeeDto);
        return ResponseEntity.ok(employeeDto);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<EmployeeDto> deleteEmployee(@PathVariable Long id){
        employeeServices.deleateEmployee(id);
        return ResponseEntity.noContent().build();
    }
}

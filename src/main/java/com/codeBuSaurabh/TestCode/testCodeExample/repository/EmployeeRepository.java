package com.codeBuSaurabh.TestCode.testCodeExample.repository;

import com.codeBuSaurabh.TestCode.testCodeExample.entities.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity,Long> {

    List<EmployeeEntity> findByEmail(String email);
}

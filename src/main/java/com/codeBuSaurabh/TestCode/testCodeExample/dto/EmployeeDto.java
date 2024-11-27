package com.codeBuSaurabh.TestCode.testCodeExample.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EmployeeDto {
    private Long id;
    private String name;
    private String email;
    private Double salary;
}

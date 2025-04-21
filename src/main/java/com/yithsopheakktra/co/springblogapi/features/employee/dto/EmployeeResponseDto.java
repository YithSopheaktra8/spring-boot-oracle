package com.yithsopheakktra.co.springblogapi.features.employee.dto;

import com.yithsopheakktra.co.springblogapi.domain.Employee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EmployeeResponseDto{

    private List<Employee> employees;
    private String ExecuteTime;
}

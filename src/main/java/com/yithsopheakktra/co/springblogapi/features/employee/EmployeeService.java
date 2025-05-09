package com.yithsopheakktra.co.springblogapi.features.employee;

import com.yithsopheakktra.co.springblogapi.domain.Employee;
import com.yithsopheakktra.co.springblogapi.domain.ProcedureTask;
import com.yithsopheakktra.co.springblogapi.features.employee.dto.EmployeeResponseDto;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface EmployeeService {

    void getEmployeesByDepartment(Long deptId, Jwt jwt, ProcedureTask procedureTask);
}

package com.yithsopheakktra.co.springblogapi.features.employee;

import com.yithsopheakktra.co.springblogapi.domain.Employee;
import com.yithsopheakktra.co.springblogapi.features.employee.dto.EmployeeResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {

    private final EmployeeServiceImpl employeeServiceImpl;

    @GetMapping("/department/{deptId}")
    public EmployeeResponseDto getEmployeesByDepartment(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long deptId
    ) {

        EmployeeResponseDto employees = employeeServiceImpl.getEmployeesByDepartment(deptId, jwt);
        log.info("Employees: {}", employees);

        return employees;
    }

}

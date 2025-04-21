package com.yithsopheakktra.co.springblogapi.features.employee;

import com.yithsopheakktra.co.springblogapi.domain.Employee;
import com.yithsopheakktra.co.springblogapi.features.employee.dto.EmployeeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public EmployeeResponseDto getEmployeesByDepartment(Long deptId, Jwt jwt) {
        return employeeRepository.getEmployeesByDepartment(deptId, jwt);
    }
}


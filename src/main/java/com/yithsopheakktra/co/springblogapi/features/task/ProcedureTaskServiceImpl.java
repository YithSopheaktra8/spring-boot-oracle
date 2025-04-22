package com.yithsopheakktra.co.springblogapi.features.task;

import com.yithsopheakktra.co.springblogapi.domain.ProcedureTask;
import com.yithsopheakktra.co.springblogapi.features.employee.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProcedureTaskServiceImpl implements ProcedureTaskService{

    private final ProcedureTaskRepository procedureTaskRepository;
    private final EmployeeService employeeService;

}

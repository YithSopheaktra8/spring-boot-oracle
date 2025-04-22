package com.yithsopheakktra.co.springblogapi.features.employee;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.yithsopheakktra.co.springblogapi.domain.Employee;
import com.yithsopheakktra.co.springblogapi.domain.ProcedureTask;
import com.yithsopheakktra.co.springblogapi.features.employee.dto.EmployeeResponseDto;
import com.yithsopheakktra.co.springblogapi.features.task.ProcedureTaskRepository;
import com.yithsopheakktra.co.springblogapi.features.task.ProcedureTaskServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {

    private final EmployeeServiceImpl employeeServiceImpl;
    private final ProcedureTaskRepository procedureTaskRepository;
    private final ProcedureTaskServiceImpl procedureTaskServiceImpl;

    @PostMapping("/department/{deptId}/start-task")
    public ProcedureTask getEmployeesByDepartment(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long deptId
    ) {

        if(jwt == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized access, token not found"
            );
        }

        ProcedureTask task = ProcedureTask.builder()
                .taskId(UUID.randomUUID().toString())
                .userId(jwt.getClaimAsString("jti"))
                .procedureName("GET_ALL_EMPLOYEE_IN_DEPARTMENT")
                .parameters("deptId=" + deptId)
                .status("QUEUED")
                .progress(0.0)
                .build();

        procedureTaskRepository.save(task);

        employeeServiceImpl.getEmployeesByDepartment(deptId, jwt, task);

        return task;
    }

    @GetMapping("/tasks/{taskId}/result")
    public ResponseEntity<?> getTaskResult(@PathVariable String taskId) throws IOException {
        ProcedureTask task = procedureTaskRepository.findByTaskId(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!"COMPLETED".equals(task.getStatus())) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(task.getStatus()); // still running or failed
        }

        File jsonFile = new File(task.getResult());
        if (!jsonFile.exists()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Result file not found");
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        EmployeeResponseDto dto = mapper.readValue(jsonFile, EmployeeResponseDto.class);
        return ResponseEntity.ok(dto);
    }


}

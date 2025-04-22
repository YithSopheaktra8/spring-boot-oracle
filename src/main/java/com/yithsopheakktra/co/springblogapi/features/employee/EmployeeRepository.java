package com.yithsopheakktra.co.springblogapi.features.employee;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.yithsopheakktra.co.springblogapi.domain.Audit;
import com.yithsopheakktra.co.springblogapi.domain.Employee;
import com.yithsopheakktra.co.springblogapi.domain.ProcedureTask;
import com.yithsopheakktra.co.springblogapi.domain.User;
import com.yithsopheakktra.co.springblogapi.features.audit.AuditService;
import com.yithsopheakktra.co.springblogapi.features.employee.dto.EmployeeResponseDto;
import com.yithsopheakktra.co.springblogapi.features.task.ProcedureTaskRepository;
import com.yithsopheakktra.co.springblogapi.features.user.UserRepository;
import com.yithsopheakktra.co.springblogapi.utils.FormatExcecuteTime;
import com.yithsopheakktra.co.springblogapi.utils.enums.Status;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class EmployeeRepository {

    private final ProcedureTaskRepository procedureTaskRepository;
    @PersistenceContext
    private EntityManager em;

    private final AuditService auditService;
    private final UserRepository userRepository;

    @Async
    public void getEmployeesByDepartment(Long deptId, Jwt jwt, ProcedureTask procedureTask) {

        if(jwt == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized access, token not found"
            );
        }

        procedureTask.setStatus("RUNNING");
        procedureTask.setStartTime(LocalDateTime.now());
        procedureTaskRepository.save(procedureTask);

        User user = userRepository.findByUuid(jwt.getClaimAsString("jti"))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));


        StoredProcedureQuery query = em.createStoredProcedureQuery("GET_ALL_EMPLOYEE_IN_DEPARTMENT", Employee.class);
        query.registerStoredProcedureParameter("p_dept_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_cursor", void.class, ParameterMode.REF_CURSOR);
        query.registerStoredProcedureParameter("p_exec_time", Double.class, ParameterMode.OUT); // NEW

        query.setParameter("p_dept_id", deptId);

        // Execute the stored procedure
        List<Employee> employees = query.getResultList();

        // Get the execution time
        Double executionTimeInSeconds = (Double) query.getOutputParameterValue("p_exec_time");
        String exe = FormatExcecuteTime.formatExecutionTime(executionTimeInSeconds);
        // Log execution time if needed

        // Update audit

        EmployeeResponseDto dto =  EmployeeResponseDto.builder()
                .employees(employees)
                .ExecuteTime(exe)
                .build();
        // Serialize to JSON file
        String filePath = null;
        try {
            filePath = saveToJsonFile(procedureTask.getTaskId(), dto);
        } catch (Exception e) {
            log.info("Error while saving to JSON file: {}", e.getMessage());
            procedureTask.setStatus("FAILED");
            procedureTask.setEndTime(LocalDateTime.now());
            procedureTaskRepository.save(procedureTask);
            return;
        }
        procedureTask.setStatus("COMPLETED");
        procedureTask.setResult(filePath);
        procedureTask.setEndTime(LocalDateTime.now());
        procedureTaskRepository.save(procedureTask);

    }

    private String saveToJsonFile(String taskId, EmployeeResponseDto dto) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Register support for Java 8 time types
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Optional: to write readable date strings
        mapper.enable(SerializationFeature.INDENT_OUTPUT); // Optional: to pretty-print the JSON
        String fileName = "C:\\Users\\Yith Sopheaktra\\Desktop\\data\\" + taskId + ".json";
        mapper.writeValue(new File(fileName), dto);
        return fileName;
    }


}

package com.yithsopheakktra.co.springblogapi.features.employee;

import com.yithsopheakktra.co.springblogapi.domain.Audit;
import com.yithsopheakktra.co.springblogapi.domain.Employee;
import com.yithsopheakktra.co.springblogapi.domain.User;
import com.yithsopheakktra.co.springblogapi.features.audit.AuditService;
import com.yithsopheakktra.co.springblogapi.features.employee.dto.EmployeeResponseDto;
import com.yithsopheakktra.co.springblogapi.features.user.UserRepository;
import com.yithsopheakktra.co.springblogapi.utils.FormatExcecuteTime;
import com.yithsopheakktra.co.springblogapi.utils.enums.Status;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EmployeeRepository {

    @PersistenceContext
    private EntityManager em;

    private final AuditService auditService;
    private final UserRepository userRepository;

    public EmployeeResponseDto getEmployeesByDepartment(Long deptId, Jwt jwt) {

        if(jwt == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized access, token not found"
            );
        }

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
        System.out.println("Procedure execution time: " + exe);

        // Update audit
        auditService.updateAudit(audit.getRecordId());

        return EmployeeResponseDto.builder()
                .employees(employees)
                .ExecuteTime(exe)
                .build();
    }
}

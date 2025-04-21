package com.yithsopheakktra.co.springblogapi.features.audit;

import com.yithsopheakktra.co.springblogapi.domain.Audit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepository extends JpaRepository<Audit, Long> {
    // Custom query methods can be defined here if needed
    // For example, you can add methods to find audits by user or by action type
}

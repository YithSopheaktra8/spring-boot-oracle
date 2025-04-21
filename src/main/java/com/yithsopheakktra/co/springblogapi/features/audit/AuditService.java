package com.yithsopheakktra.co.springblogapi.features.audit;

import com.yithsopheakktra.co.springblogapi.domain.Audit;
import com.yithsopheakktra.co.springblogapi.domain.User;
import com.yithsopheakktra.co.springblogapi.utils.enums.Status;

import java.time.LocalDate;

public interface AuditService {

    Audit createAudit(String procedureName, User user, String events, Status status, LocalDate startDate, LocalDate endDate);

    void updateAudit(Long recordId);
}

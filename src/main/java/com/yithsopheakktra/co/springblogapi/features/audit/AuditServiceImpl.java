package com.yithsopheakktra.co.springblogapi.features.audit;

import com.yithsopheakktra.co.springblogapi.domain.Audit;
import com.yithsopheakktra.co.springblogapi.domain.User;
import com.yithsopheakktra.co.springblogapi.utils.enums.Status;
import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService{

    private final AuditRepository auditRepository;

    @Override
        public Audit createAudit(String procedureName, User user, String events, Status status, LocalDate startDate, LocalDate endDate) {
        Audit audit = new Audit();
        audit.setProcedureName(procedureName);
        audit.setUser(user);
        audit.setEvents(events);
        audit.setStatus(status);
        audit.setStartDate(startDate);
        audit.setEndDate(endDate);
        return auditRepository.save(audit);
    }

    @Override
    public void updateAudit(Long recordId) {
        Audit audit = auditRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Audit not found"));

        audit.setEndDate(LocalDate.now());
        audit.setStatus(Status.COMPLETED);

        auditRepository.save(audit);
    }
}

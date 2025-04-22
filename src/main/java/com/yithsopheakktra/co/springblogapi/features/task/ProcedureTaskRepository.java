package com.yithsopheakktra.co.springblogapi.features.task;

import com.yithsopheakktra.co.springblogapi.domain.ProcedureTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProcedureTaskRepository extends JpaRepository<ProcedureTask, Long> {
    Optional<ProcedureTask> findByTaskId(String taskId);
}


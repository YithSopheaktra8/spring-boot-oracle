package com.yithsopheakktra.co.springblogapi.features.task;

import com.yithsopheakktra.co.springblogapi.domain.ProcedureTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<ProcedureTask, Long> {
}

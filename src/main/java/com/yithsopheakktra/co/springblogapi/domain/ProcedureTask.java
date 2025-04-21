package com.yithsopheakktra.co.springblogapi.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "procedure_tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcedureTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String taskId;
    private String userId;
    private String procedureName;
    private String parameters;
    private String status; // QUEUED, RUNNING, COMPLETED, FAILED
    private Double progress; // Optional: 0-100%
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String result; // Could store resultId or summary
    private String errorMessage;
}
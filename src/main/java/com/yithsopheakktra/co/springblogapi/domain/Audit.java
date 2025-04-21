package com.yithsopheakktra.co.springblogapi.domain;

import com.yithsopheakktra.co.springblogapi.utils.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "APP_TD_AUDIT_TRAIL", schema = "HR")
@Setter
@Getter
@NoArgsConstructor
public class Audit {

    @Id
    @Column(name = "RECORD_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordId;

    @Column(name = "PROCEDURE_NAME")
    private String procedureName;

    @Column(name = "EVENTS")
    private String events;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private Status status;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @ManyToOne
    private User user;
}

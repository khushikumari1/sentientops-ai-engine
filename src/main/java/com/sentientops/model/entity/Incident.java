package com.sentientops.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "incidents")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String serviceName;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String logs;

    private Double cpuUsage;
    private Double memoryUsage;

    @Enumerated(EnumType.STRING)
    private Severity severity;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "resolved_at")
    private Instant resolvedAt;

    private boolean resolved;

    public enum Severity { LOW, MEDIUM, HIGH, CRITICAL }

    @PrePersist
    void prePersist() { this.createdAt = Instant.now(); }
}

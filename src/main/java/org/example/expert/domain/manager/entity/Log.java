package org.example.expert.domain.manager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "log")
public class Log {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "log_content")
    private String logContent;

    @Column(name = "log_createAt")
    private LocalDateTime logCreateAt;

    public Log(long todoId, Long managerUserId) {
        this.logContent = "todoId: " + todoId + "인 일정에 " + "managerId: " + managerUserId + "가 매니저 등록 요청";
        this.logCreateAt = LocalDateTime.now();
    }
}
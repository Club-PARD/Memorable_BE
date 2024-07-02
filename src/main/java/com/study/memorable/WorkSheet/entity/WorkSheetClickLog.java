package com.study.memorable.WorkSheet.entity;

import com.study.memorable.User.entity.User;
import com.study.memorable.WorkSheet.entity.WorkSheet;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkSheetClickLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "worksheet_id")
    private WorkSheet workSheet;

    private LocalDateTime clickTime;

    @PrePersist
    protected void onCreate() {
        this.clickTime = LocalDateTime.now();
    }
}


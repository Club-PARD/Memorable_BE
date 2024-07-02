package com.study.memorable.WrongSheet.repo;

import com.study.memorable.WorkSheet.entity.WorkSheetClickLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkSheetClickLogRepo extends JpaRepository<WorkSheetClickLog, Long> {
    Optional<WorkSheetClickLog> findTopByUserIdOrderByClickTimeDesc(Long userId);
}


package com.study.memorable.WorkSheet.repo;

import com.study.memorable.WorkSheet.entity.WorkSheet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkSheetRepo extends JpaRepository<WorkSheet, Long> {
}

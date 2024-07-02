package com.study.memorable.TestSheet.repo;

import com.study.memorable.TestSheet.entity.TestSheet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestSheetRepo extends JpaRepository<TestSheet, Long> {
}

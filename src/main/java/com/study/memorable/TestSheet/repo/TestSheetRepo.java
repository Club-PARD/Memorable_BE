package com.study.memorable.TestSheet.repo;

import com.study.memorable.TestSheet.entity.TestSheet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestSheetRepo extends JpaRepository<TestSheet, Long> {
    List<TestSheet> findByFile_User_Id(String userIdentifier);
}

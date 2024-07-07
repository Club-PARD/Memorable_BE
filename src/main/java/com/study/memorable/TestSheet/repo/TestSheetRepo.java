package com.study.memorable.TestSheet.repo;

import com.study.memorable.TestSheet.entity.TestSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TestSheetRepo extends JpaRepository<TestSheet, Long> {
    @Query("SELECT ts FROM TestSheet ts WHERE ts.file.user.id = :userId")
    List<TestSheet> findByFileUserId(@Param("userId") String userId);
}

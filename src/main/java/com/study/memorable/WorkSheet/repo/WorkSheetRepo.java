package com.study.memorable.WorkSheet.repo;

import com.study.memorable.WorkSheet.entity.WorkSheet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkSheetRepo extends JpaRepository<WorkSheet, Long> {

//    @Query("SELECT COALESCE(MAX(ws.id), 0) FROM WorkSheet ws WHERE ws.user = :user")
//    Long findMaxIdByUser(@Param("user") User user);
}

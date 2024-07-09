package com.study.memorable.WrongSheet.repo;

import com.study.memorable.File.entity.File;
import com.study.memorable.WrongSheet.entity.WrongSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WrongSheetRepo extends JpaRepository<WrongSheet, Long> {
    @Query("SELECT ws FROM WrongSheet ws WHERE ws.file.user.id = :userId")
    List<WrongSheet> findByUserId(@Param("userId") String userId);

    Optional<WrongSheet> findByFile(File file);
}

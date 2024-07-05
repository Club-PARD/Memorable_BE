package com.study.memorable.Questions.Repo;

import com.study.memorable.File.entity.File;
import com.study.memorable.Questions.entity.Questions;
import com.study.memorable.TestSheet.entity.TestSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionsRepo extends JpaRepository<Questions, Long> {
    @Query("SELECT q FROM Questions q WHERE q.file = :file")
    List<Questions> findByFile(@Param("file") File file);
}

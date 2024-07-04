package com.study.memorable.Questions.Repo;

import com.study.memorable.Questions.entity.Questions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionsRepo extends JpaRepository<Questions, Long> {
}

package com.study.memorable.File.repo;

import com.study.memorable.File.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepo extends JpaRepository<File, Long> {
}

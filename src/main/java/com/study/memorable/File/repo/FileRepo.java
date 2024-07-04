package com.study.memorable.File.repo;

import com.study.memorable.File.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepo extends JpaRepository<File, Long> {
    List<File> findByUser_Id(String userId);
}

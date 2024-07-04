package com.study.memorable.User.repo;

import com.study.memorable.User.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, String> {

}

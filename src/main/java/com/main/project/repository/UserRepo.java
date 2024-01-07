package com.main.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.project.entity.User;

public interface UserRepo extends JpaRepository<User, Integer>{

	public User findByEmail(String email);
}

package com.sparta.msa_exam.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.msa_exam.auth.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
}

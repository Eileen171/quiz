package com.example.quiz.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.quiz.entity.Quiz;

@Repository
public interface QuizDao extends JpaRepository<Quiz, Integer>{

	// 驗證 JPA 語法時，可以直接跑 Application，有錯就會顯示在 console
	public List<Quiz> findByNameContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqual //
	(String name, LocalDate start, LocalDate end);
	
	
}

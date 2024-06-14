package com.example.quiz.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.quiz.entity.Response;

@Repository
public interface ResponseDao extends JpaRepository<Response, Integer>{

	// 因只要確認存在沒有要對資料後續處理用 exists
	// 比對問券 id 跟電話 去確認此電話是否有在那張問券填過
	public boolean existsByQuizIdAndPhone(int quizId, String phone);
	
	public List<Response> findByQuizId(int quizId);

}

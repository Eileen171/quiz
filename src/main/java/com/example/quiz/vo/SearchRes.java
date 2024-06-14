package com.example.quiz.vo;

import java.util.List;

import com.example.quiz.entity.Quiz;

public class SearchRes extends BasicRes{

	private List<Quiz> quizList;

	public SearchRes() {
		super();
	}
	// 合併父類別的預設建構方法及自定義建構方法
	public SearchRes(int statusCode, String message, List<Quiz> quizList) {
		super(statusCode, message);
		this.quizList = quizList;
	}
	public List<Quiz> getQuizList() {
		return quizList;
	}
	public void setQuizList(List<Quiz> quizList) {
		this.quizList = quizList;
	}
	
//	public SearchRes(int statusCode, String message) {
//		super(statusCode, message);
//	}

//	public SearchRes(List<Quiz> quizList) {
//		super();
//		this.quizList = quizList;
//	}
	
	
	
}

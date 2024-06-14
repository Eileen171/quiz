package com.example.quiz.vo;

import com.fasterxml.jackson.annotation.JsonAlias;

public class FeedbackReq {

	@JsonAlias("quiz_id")
	private int quizId;

	public FeedbackReq() {
		super();
	}

	public FeedbackReq(int quizId) {
		super();
		this.quizId = quizId;
	}

	public int getQuizId() {
		return quizId;
	}
	
	
	
}

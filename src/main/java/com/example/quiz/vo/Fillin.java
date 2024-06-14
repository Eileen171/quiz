package com.example.quiz.vo;

import com.fasterxml.jackson.annotation.JsonAlias;

public class Fillin {
	
	// question_id
	@JsonAlias("question_id")
	private int qId;
	
	private String question;
	
	// 多個選項是用分號(;)串接
	private String options;
	
	// 多個答案是用分號(;)串接
	private String answer;
	
	private String type;
	
	private boolean necessary;

	
	public Fillin() {
		super();
	}
	public Fillin(int qId, String question, String options, String answer, String type, boolean necessary) {
		super();
		this.qId = qId;
		this.question = question;
		this.options = options;
		this.answer = answer;
		this.type = type;
		this.necessary = necessary;
	}


	public int getqId() {
		return qId;
	}
	public String getAnswer() {
		return answer;
	}
	public String getType() {
		return type;
	}
	public boolean isNecessary() {
		return necessary;
	}
	public String getQuestion() {
		return question;
	}
	public String getOptions() {
		return options;
	}
	

}

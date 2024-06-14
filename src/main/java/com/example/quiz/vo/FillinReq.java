package com.example.quiz.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;

public class FillinReq {

	@JsonAlias("quiz_id")
	private int quizId;
	
	private String name;
	
	private String phone;
	
	private String email;
	
	private int age;
	
	@JsonAlias("fillin_list")
	private List<Fillin> fillinList;
	
	// 一般 Req 不須建構方法，但因為 JUnitTest 測試時需要，所以還是要產生
	// 預設建構方法是給 SpringBoot 用的，自定義才是我們用的
	public FillinReq() {
		super();
	}
	public FillinReq(int quizId, String name, String phone, String email, int age, List<Fillin> fillinList) {
		super();
		this.quizId = quizId;
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.age = age;
		this.fillinList = fillinList;
	}
		
	public int getQuizId() {
		return quizId;
	}
	public String getName() {
		return name;
	}
	public String getPhone() {
		return phone;
	}
	public String getEmail() {
		return email;
	}
	public int getAge() {
		return age;
	}
	public List<Fillin> getFillinList() {
		return fillinList;
	}
	
	
}

	
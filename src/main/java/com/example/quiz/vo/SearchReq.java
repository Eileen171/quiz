package com.example.quiz.vo;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonAlias;

public class SearchReq {

	private String name;
	
	@JsonAlias("start_date")
	private LocalDate startDate;
	
	@JsonAlias("end_date")
	private LocalDate endDate;

	
	public String getName() {
		return name;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}
}

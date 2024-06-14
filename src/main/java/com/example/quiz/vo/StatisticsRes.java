package com.example.quiz.vo;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class StatisticsRes extends BasicRes { // Res 是很多題統計的集合

	private String quizName;

	private LocalDate startDate;

	private LocalDate endDate;

	private Map<Integer, Map<String, Integer>> countMap;

	private List<Statistics> statistics;

	public StatisticsRes() {
		super();
	}

	public StatisticsRes(int statusCode, String message) {
		super(statusCode, message);
	}

	public StatisticsRes(int statusCode, String message, String quizName, LocalDate startDate, LocalDate endDate,
			Map<Integer, Map<String, Integer>> countMap) {
		super(statusCode, message);
		this.quizName = quizName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.countMap = countMap;
	}

	public String getQuizName() {
		return quizName;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public List<Statistics> getStatistics() {
		return statistics;
	}

	public Map<Integer, Map<String, Integer>> getCountMap() {
		return countMap;
	}

}

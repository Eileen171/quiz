package com.example.quiz.vo;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;

// 通常 Req 只會有 get 用不到 set，因為是將外部資料導入
public class CreateOrUpdateReq {

	private int id;
	
	// 將 Entity問卷 的4個參數導入
	private String name;
	
	private String description;
	
	// 前端有可能用的命名方式，因為小駝峰是java用的，前端多用 "_" 連接2個單字
	@JsonAlias("start_date")
	private LocalDate startDate;
	
	@JsonAlias("end_date")
	private LocalDate endDate;
	
	// 將 Question問題 的5個參數導入
	// 因一個問券不只有一個問題，若如下方被註解區所寫，一個問券只會有一個問題
	// 為避免此狀況，也因4個參數包含在 Question內，所以將4個參數打包成物件導入
	@JsonAlias("question_list")
	private List<Question> questionList; 
//	@JsonAlias("question_id")
//	private int questionId; // 為避免跟 Entity 的 Question 搞混，建議重新命名
//	
//	private String content;
//	
//	private String type;
//	
//	@JsonAlias("is_necessary")
//	private boolean necessary;
	
	@JsonAlias("is_published") // 關係到儲存後要不要發布，所以要使用
	private boolean published;

	
	public CreateOrUpdateReq() {
		super();
	}
	public CreateOrUpdateReq(String name, String description, LocalDate startDate, LocalDate endDate,
			List<Question> questionList, boolean published) {
		super();
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.questionList = questionList;
		this.published = published;
	}
	public CreateOrUpdateReq(int id, String name, String description, LocalDate startDate, LocalDate endDate,
			List<Question> questionList, boolean published) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.questionList = questionList;
		this.published = published;
	}
	
	
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public List<Question> getQuestionList() {
		return questionList;
	}

	public boolean isPublished() {
		return published;
	}
	public int getId() {
		return id;
	}
	
}

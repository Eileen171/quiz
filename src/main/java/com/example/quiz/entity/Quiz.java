package com.example.quiz.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "quiz")
public class Quiz {

	// 當 PK主鍵 是無意義的序號或流水號時可用 AI
	// 因為 PK 是 AI (Auto Incremental)，所以要加上此 Annotation
	// strategy：指的是 AI 生成策略
	// GenerationType.IDENTITY；代表 PK 數字由資料庫來自動增加
	// Type.後有4種生成策略可參考PDF講義 SpringBoot01 基礎的 P26
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 若表有勾選"AI"，則需要此行
	@Id
	@Column( name = "id")
	private int id;
	
	@Column( name = "name")
	private String name;
	
	@Column( name = "description")
	private String description;
	
	@Column( name = "start_date")
	private LocalDate startDate;
	
//	private LocalTime time;
//	private LocalDateTime dateTime;
	
	@Column( name = "end_date")
	private LocalDate endDate;
	
	@Column( name = "questions")
	private String question;
	
	@Column( name = "published")
	private boolean published;

	
	public Quiz() {
		super();
	}
	// 自定義建構方法不需要勾id，因id會被AI自動產生
	public Quiz(String name, String description, LocalDate startDate, LocalDate endDate, String question,
			boolean published) {
		super();
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.question = question;
		this.published = published;
	}
	// 為避免指定時無法使用，另加一個有id的自定義建構方法
	public Quiz(int id, String name, String description, LocalDate startDate, LocalDate endDate, String question,
			boolean published) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.question = question;
		this.published = published;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public boolean isPublished() {
		return published;
	}
	public void setPublished(boolean published) {
		this.published = published;
	}	
}

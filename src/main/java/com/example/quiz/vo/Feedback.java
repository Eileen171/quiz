package com.example.quiz.vo;

import java.time.LocalDateTime;

public class Feedback {

	private String userName;

	private LocalDateTime fillinDateTime;

	private FeedbackDetail feedbackdetail;

	
	public Feedback() {
		super();
	}
	public Feedback(String userName, LocalDateTime fillinDateTime, FeedbackDetail feedbackdetail) {
		super();
		this.userName = userName;
		this.fillinDateTime = fillinDateTime;
		this.feedbackdetail = feedbackdetail;
	}

	
	public String getUserName() {
		return userName;
	}

	public LocalDateTime getFillinDateTime() {
		return fillinDateTime;
	}

	public FeedbackDetail getFeedbackdetail() {
		return feedbackdetail;
	}

}

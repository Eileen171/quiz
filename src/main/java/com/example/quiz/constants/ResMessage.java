package com.example.quiz.constants;

// enum 不能 new 
public enum ResMessage {
	
	// 列舉用全大寫
	SUCCESS(200, "Success!!"), //
	PARAM_QUIZ_NAME_ERROR(400, "Param quiz name error!!"), //
	PARAM_DESCRIPTION_ERROR(400, "Param description error!!"), //
	PARAM_START_DATE_ERROR(400, "Param start date error!!"), //
	PARAM_END_DATE_ERROR(400, "Param end date error!!"), //
	PARAM_QUESTION_LIST_NOT_FOUND(404, "Param question list not found!!"), // 找不到是404
	PARAM_QUESTION_ID_ERROR(400, "Param question id error!!"), //
	PARAM_TITLE_ERROR(400, "Param title error!!"), //
	PARAM_OPTIONS_ERROR(400, "Param options error!!"),
	PARAM_TYPE_ERROR(400, "Param typr error!!"), //
	JSON_PROCESSING_EXCEPTION(400, "Json Processing Exception!!"), //
	UPDATE_ID_NOT_FOUND(404, "Update id not found!!"), //
	PARAM_QUIZ_ID_ERROR(400, "Param quiz id error!!"), //
	PARAM_NAME_IS_REQUIRED(400, "Param name is required!!"), //
	PARAM_PHONE_IS_REQUIRED(400, "Param phone is required!!"), //
	PARAM_EMAIL_IS_REQUIRED(400, "Param email is required!!"), //
	PARAM_AGE_NOT_QUALIFIED(400, "Param age not qualified!!"), //
	QUIZ_NOT_FOUND(404, "Quiz not found!!"), //
	ANSWER_IS_REQUIRED(400, "Answer is required"), //
	ANSWER_OPTION_IS_NOT_MATCH(400,"Answer option is not match"), //
	ANSWER_OPTION_TYPE_IS_NOT_MATCH(400,"Answer option_type is not match"), //
	DUPLICATED_FILLIN(400,"Duplicated fillin!!");
	
	private int code; // 可不寫 => 上方就只需打 SUCCESS();

	private String message;

	// 建構方法需待 SUCCESS(200, "Success!!") 建立完成後再建構方法
	// 帶有所有屬性的私有建構方法
	private ResMessage(int code, String message) {
		this.code = code;
		this.message = message;
	}

	// enum 只會用到 get，不會用到 set
	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}

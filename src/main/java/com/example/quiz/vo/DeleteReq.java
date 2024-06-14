package com.example.quiz.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;

public class DeleteReq {

	@JsonAlias("id_list")
	private List<Integer> idList;

	
	public DeleteReq() {
		super();
	}
	public DeleteReq(List<Integer> idList) {
		super();
		this.idList = idList;
	}

	
	public List<Integer> getIdList() {
		return idList;
	}

}

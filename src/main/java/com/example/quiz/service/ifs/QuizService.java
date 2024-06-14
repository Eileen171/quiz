package com.example.quiz.service.ifs;

import com.example.quiz.vo.BasicRes;
import com.example.quiz.vo.CreateOrUpdateReq;
import com.example.quiz.vo.DeleteReq;
import com.example.quiz.vo.SearchReq;
import com.example.quiz.vo.SearchRes;

public interface QuizService {

	// 當沒有文件，不確定要不要回傳時，先用void
	public BasicRes createOrUpdate(CreateOrUpdateReq req); // 因()的參數很多，所以用打包放入，若參數不多使用資料型態+參數即可
	
	public SearchRes search(SearchReq req);
	
	public BasicRes delete(DeleteReq req);
	
}

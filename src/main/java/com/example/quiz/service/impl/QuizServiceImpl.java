package com.example.quiz.service.impl;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.quiz.constants.OptionType;
import com.example.quiz.constants.ResMessage;
import com.example.quiz.entity.Quiz;
import com.example.quiz.repository.QuizDao;
import com.example.quiz.service.ifs.QuizService;
import com.example.quiz.vo.BasicRes;
import com.example.quiz.vo.CreateOrUpdateReq;
import com.example.quiz.vo.DeleteReq;
import com.example.quiz.vo.Question;
import com.example.quiz.vo.SearchReq;
import com.example.quiz.vo.SearchRes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class QuizServiceImpl implements QuizService {

	@Autowired
	private QuizDao quizDao;

	@Override
	public BasicRes createOrUpdate(CreateOrUpdateReq req) {
		// 檢查參數
		BasicRes checkResult = checkParams(req);
		// checkResult == null，表示檢查參數都正確
		if (checkResult != null) {
			// 私有方法會回傳 BasicRes，再直接把回傳的訊息告知
			return checkResult;
		}
		// 因為 Quiz 中的 questions 的資料格式是String，所以要將 req 的List<Question> 轉成String，
		// 透過 ObjectMapper 可以把物件(類別)轉成 JSON 格式的字串
		ObjectMapper mapper = new ObjectMapper();
		try {
			// 紅bug用try/catch較方便，另一個定義要在多個檔案內新增
			String questionStr = mapper.writeValueAsString(req.getQuestionList());

//--		// 若 req 中的 id > 0，表示更新已存在的資料；若 id = 0，則表示要新增
//--		if(req.getId() > 0) {
				// 以下2種方法擇一；方法2 因為是透過 existsById 來判斷資料是否存在，
				// 方法1.透過 findById：若有資料，回傳整筆資料(可能資料量較大)
				// 方法2.透過 existsById：回傳的資料永遠只會是一個 bit 的值(0 or 1)，較節省程式碼且
				
//				// 方法1. 透過 findById：若有資料，回傳整筆資料
//				Optional<Quiz> op = quizDao.findById(req.getId());
//				// 判斷是否有資料
//				if(op.isEmpty() ) {
//					return new BasicRes(ResMessage.UPDATE_ID_NOT_FOUND.getCode(), //
//							ResMessage.UPDATE_ID_NOT_FOUND.getMessage());
//				}
//				Quiz quiz = op.get();
//				// 設定新值(值從 req 取出)
//				// 將 req 中的新值設定到舊的 quiz 中，不設定 id，因為 id 一樣，其他參數逐項更新
//				quiz.setName(req.getName());
//				quiz.setDescription(req.getDescription());
//				quiz.setStartDate(req.getStartDate());
//				quiz.setEndDate(req.getEndDate());
//				quiz.setQuestion(questionStr);
//				quiz.setPublished(req.isPublished());
				
/*--		// 方法2. 透過 existsById：回傳一個 bit 的值
			// 這邊要判斷從 req 帶進來的 id 是否真的有存在於 DB 中
			// 因為若 id 不存在，又不檢查，後續程式碼在呼叫 JPA 的 save 方法時，會變成新增
			boolean boo = quizDao.existsById(req.getId());
			if(!boo) { // 反向：表示資料不存在
				return new BasicRes(ResMessage.UPDATE_ID_NOT_FOUND.getCode(), //
						ResMessage.UPDATE_ID_NOT_FOUND.getMessage());
  			上方boo用匿名類別
			if(!quizDao.existsById(req.getId())) { // 反向：表示資料不存在
				return new BasicRes(ResMessage.UPDATE_ID_NOT_FOUND.getCode(), //
						ResMessage.UPDATE_ID_NOT_FOUND.getMessage());
--*/					
			// 將上述--區塊合併
			if(req.getId() > 0 && !quizDao.existsById(req.getId())) {
				return new BasicRes(ResMessage.UPDATE_ID_NOT_FOUND.getCode(), //
						ResMessage.UPDATE_ID_NOT_FOUND.getMessage());
			}	
			// 因為變數 quiz 只使用一次，因此可以使用匿名類別方式撰寫(不需要變數接)
			// new Quiz()中帶入 req.getId() 是PK，在呼叫 save 時，會先去 select 檢查 PK 是否有存在於 DB 中，
			// 若存在 --> 更新；不存在 --> 新增
			// req 中沒有該欄位時，預設是0，因為 id 的資料型態是 int
			quizDao.save(new Quiz(req.getId(), req.getName(), req.getDescription(), req.getStartDate(), //
					req.getEndDate(), questionStr, req.isPublished()));
			// <匿名類別>將下面2行合併成下方，其實 questionStr 也可一起合併，但因程式碼會變得超級長
			//Quiz quiz = new Quiz(req.getId(), req.getName(), req.getDescription(), req.getStartDate(), req.getEndDate(), //
			//		questionStr, req.isPublished());
			// quizDao.save(quiz); 
//			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return new BasicRes(ResMessage.SUCCESS.getCode(), //
					ResMessage.SUCCESS.getMessage());
		}

		return new BasicRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage());
		// =====若使用return SUCCESS的話=====
//		if(checkResult.getStatusCode() != 200) {
//			return checkResult;
//		}

	}

	// 將檢查參數拉成私有方法，參數帶入 Req 就不用重打8個參數
	// 抽方法使用時機：共用(重複區塊抽出來)、一段方法不影響使用邏輯時可以拆分出來
	// 拆開的好處：日後回來看時，不會花霧霧
	private BasicRes checkParams(CreateOrUpdateReq req) {
		// 檢查問卷參數
		// StringUtils.hasText若有值 => 回傳true
		// 前方+"!"是指沒有值，沒有輸入值的話 => 回傳true，代表空值 || 空白，印出警語
		if (!StringUtils.hasText(req.getName())) {
			return new BasicRes(ResMessage.PARAM_QUIZ_NAME_ERROR.getCode(), //
					ResMessage.PARAM_QUIZ_NAME_ERROR.getMessage());
		}
		if (!StringUtils.hasText(req.getDescription())) {
			return new BasicRes(ResMessage.PARAM_DESCRIPTION_ERROR.getCode(), //
					ResMessage.PARAM_DESCRIPTION_ERROR.getMessage());
		}

		// 開始時間不能 <= 當日
		// LocalDate.now(): 取得系統當前時間
		// req.getStartDate().isBefore(LocalDate.now()): 若 req 中的開始時間"早"於當前時間，會得到 true
		// req.getStartDate().isEqual(LocalDate.now()): 若 req 中的開始時間"等"於當前時間，會得到 true
		// 因為開始時間不能在今天(含)之前，所以上兩個比較若是任一個結果為 true，則表示開始時間要比當前(含)時間早
//		if (req.getStartDate() == null || req.getStartDate().isBefore(LocalDate.now()) //
//				req.getStartDate().isEqual(LocalDate.now())) {
//			return new BasicRes(ResMessage.PARAM_START_DATE_ERROR.getCode(), //
//					ResMessage.PARAM_START_DATE_ERROR.getMessage());
//		}
		// req.getStartDate().isAfter(LocalDate.now())：若 req 中的開始時間"晚"於當前時間，會得到 true
		// !req.getStartDate().isAfter(LocalDate.now())：前加驚嘆號得到反向，若開始時間"沒有"晚於當日則得到true，就會是error
		if (req.getStartDate() == null || !req.getStartDate().isAfter(LocalDate.now())) {
			return new BasicRes(ResMessage.PARAM_START_DATE_ERROR.getCode(), //
					ResMessage.PARAM_START_DATE_ERROR.getMessage());
		}
		// 因上一個if已經過濾掉開始日 < 當日 且結束日 >= 開始日
		// 所以此處就不用再過濾 結束日 > 開始日
		// 故此if僅要過濾3.
		// 2.結束時間不能 <= 當日 且 3.結束時間不能 < 開始日()
		if (req.getEndDate() == null || req.getEndDate().isBefore(req.getStartDate())) {
			return new BasicRes(ResMessage.PARAM_END_DATE_ERROR.getCode(), //
					ResMessage.PARAM_END_DATE_ERROR.getMessage());
		}

		// 檢查問題的參數
		if (CollectionUtils.isEmpty(req.getQuestionList())) {
			return new BasicRes(ResMessage.PARAM_QUESTION_LIST_NOT_FOUND.getCode(), //
					ResMessage.PARAM_QUESTION_LIST_NOT_FOUND.getMessage());
		}
		// 一張問券可能會有多個問題，所以要逐筆檢查每一提的參數，才會使用 for 迴圈
		for (Question item : req.getQuestionList()) {
			if (item.getId() <= 0) {
				return new BasicRes(ResMessage.PARAM_QUESTION_ID_ERROR.getCode(), //
						ResMessage.PARAM_QUESTION_ID_ERROR.getMessage());
			}
			if (!StringUtils.hasText(item.getTitle())) {
				return new BasicRes(ResMessage.PARAM_TITLE_ERROR.getCode(), //
						ResMessage.PARAM_TITLE_ERROR.getMessage());
			}
			if (!StringUtils.hasText(item.getType())) {
				return new BasicRes(ResMessage.PARAM_TYPE_ERROR.getCode(), //
						ResMessage.PARAM_TYPE_ERROR.getMessage());
			}
//			// 當 option_type 是單選或多選時，options 就不能是空字串
//			// 但 option_type 是文字時，options 允許是空字串，
//			if (item.getType().equals(OptionType.SINGLE_CHOICE.getType()) //
//					|| item.getType().equals(OptionType.MULTI_CHOICE.getType())) {
//				// 以下條件檢查：當 option_type 是單選或多選且 options 是空字串，返回錯誤
//				// 若以下條件放外面，會讓此問卷無法有文字回答
//				if (!StringUtils.hasText(item.getOptions())) {
//					return new BasicRes(ResMessage.PARAM_OPTIONS_ERROR.getCode(), //
//							ResMessage.PARAM_OPTIONS_ERROR.getMessage());
//				}
//			}
			// 以下為上述2個 if 的合併寫法：((條件1 ||條件2) && 條件3)
			// 						第一個 if 條件式(其1滿足) && 第二個 if 條件式
			// 條件1與條件2需有小誇號的原因是 && 的計算順序會 優先於 ||
			if((item.getType().equals(OptionType.SINGLE_CHOICE.getType()) //
					|| item.getType().equals(OptionType.MULTI_CHOICE.getType()))
					&& !StringUtils.hasText(item.getOptions())) {
				return new BasicRes(ResMessage.PARAM_OPTIONS_ERROR.getCode(), //
						ResMessage.PARAM_OPTIONS_ERROR.getMessage());
			}
		}
		return null;
//		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	@Override
	public SearchRes search(SearchReq req) {
		// 檢查
		String name = req.getName();
		LocalDate start = req.getStartDate();
		LocalDate end = req.getEndDate();
		// 防呆 null 跟全空白字串
		// 將上述視為沒有輸入條件值 => 取得全部資料
		// JPA 的 containing 方法，條件值是空字串時，會搜尋全部
		// 所以要把 name 的值是 null 跟全空白字串時，轉換成空字串
		if(!StringUtils.hasText(name)) { 
			name = "";
		}
		// 若開始日期跟結束日期 是空值，自動帶入以下參數
		if(start == null) {
			start = LocalDate.of(1970, 1, 1);
		}
		if(end == null) {
			end = LocalDate.of(2999, 12, 31);
		}
//		List<Quiz> res = quizDao.findByNameContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqual(name, start, end);
//		return new SearchRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), res);
		// 合併上述2行
		return new SearchRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage(), //
				quizDao.findByNameContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqual(name, start, end));

	}

	@Override
	public BasicRes delete(DeleteReq req) {
		// 檢查參數 list 用 CollectionUtils，字串用 StringUtils
		if(!CollectionUtils.isEmpty(req.getIdList())) { // 參數不為空時，刪除問卷
			// 刪除問卷
			try {
				quizDao.deleteAllById(req.getIdList());
			} catch (Exception e) { // 是所有子類別 Exception 的父類別 ex.RuntimeException, IOException...
				// 當 deleteAllById 方法中，id的值不存在時，JPA 會抱錯
				// 因為在刪除之前 JPA 會先搜尋帶入的 id 值，若沒結果就會抱錯
				// 但實際上也沒刪除任何資料，所以就不需要就這個 Exception 處理
			}
		}
		return new BasicRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage());
	}

}

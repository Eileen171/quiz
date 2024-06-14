package com.example.quiz.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.quiz.constants.OptionType;
import com.example.quiz.constants.ResMessage;
import com.example.quiz.entity.Quiz;
import com.example.quiz.entity.Response;
import com.example.quiz.repository.QuizDao;
import com.example.quiz.repository.ResponseDao;
import com.example.quiz.service.ifs.FillinService;
import com.example.quiz.vo.BasicRes;
import com.example.quiz.vo.Feedback;
import com.example.quiz.vo.FeedbackDetail;
import com.example.quiz.vo.FeedbackReq;
import com.example.quiz.vo.FeedbackRes;
import com.example.quiz.vo.Fillin;
import com.example.quiz.vo.FillinReq;
import com.example.quiz.vo.Question;
import com.example.quiz.vo.Statistics;
import com.example.quiz.vo.StatisticsRes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FillinServiceImpl implements FillinService {

	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	QuizDao quizDao;

	@Autowired
	ResponseDao responseDao;

	@Override
	public BasicRes fillin(FillinReq req) {
		// 檢查參數 用私有方法
		BasicRes checkResult = checkParams(req);
		if (checkResult != null) {
			return checkResult;
		}
		// 將同一電話號碼作為同一個人的辨識方法
		// 檢查同一個電話號碼是否重複填寫同一張問卷
		if (responseDao.existsByQuizIdAndPhone(req.getQuizId(), req.getPhone())) {
			return new BasicRes(ResMessage.DUPLICATED_FILLIN.getCode(), //
					ResMessage.DUPLICATED_FILLIN.getMessage());
		}
		// 檢查 quizId 是否存在 DB 中：用 findById
		// 因後續要比對 req 中的 answer 跟 necessary 是否符合
		Optional<Quiz> op = quizDao.findById(req.getQuizId());
		if (op.isEmpty()) {
			return new BasicRes(ResMessage.QUIZ_NOT_FOUND.getCode(), ResMessage.QUIZ_NOT_FOUND.getMessage());
		}
		Quiz quiz = op.get();
		// 從 quiz 中取出 questions 字串
		String questionsStr = quiz.getQuestion();
		// 使用 ObjectMapper 將 questionsStr 轉成 List<Question>
		// fillinStr 要給空字串，不然預設會是 null
		// 若 fillinStr = null，後續執行 fillinStr =
		// mapper.writeValueAsString(req.getqIdAnswerMap());
		// 把執行得到的結果塞回給 fillinStr 時，會報錯
		String fillinStr = "";
		try {
			List<Fillin> newFillinList = new ArrayList<>();
			// 建立已新增的 question_id list
			List<Integer> qIds = new ArrayList<>();
			List<Question> quList = mapper.readValue(questionsStr, new TypeReference<>() {
			});
			// 建立要存進 DB 的 fillin_list
			// 比對每一個 Question 與 fillin 中的答案
			for (Question item : quList) { // 1(必) 2(必) 3(選)
				// 建立正確的List<Fillin>
				List<Fillin> fillinList = req.getFillinList();
				for (Fillin fillin : fillinList) { // 1 3
					// 若 id 不一致，就跳過
					if (item.getId() != fillin.getqId()) {
						continue;
					}
					// 如果 qIds 已經有包含問題編號，表示已檢查過該題號
					// ex. quList:1 2 3 ; fillinList:1 2 3 3
					// 因無法判斷那一個為正確，所以取第一個進入迴圈的值
					// 此段為排除 req 有重複問題的編號
					if (qIds.contains(fillin.getqId())) {
						continue;
					}
					// 將已新增問題的題號加入
					qIds.add(fillin.getqId()); // 1 3
					// 新增相同題號的 fillin
					// 不直接把 fillin 加到 list 的原因是上面僅對 question_id 和 answer 做檢查
					// 有可能其餘的外部 value 不符合 key值 規定，
					// 直接使用 Question item 的值是因為這些都是從 DB 來，當初已檢查過才能寫入 DB 的
					// qId, question, options,
					// answer, type, necessary
					newFillinList.add(new Fillin(item.getId(), item.getTitle(), item.getOptions(), //
							fillin.getAnswer(), item.getType(), item.isNecessary()));
					// 用私有方法(checkOptionAnswer) 檢查選項與答案
					checkResult = checkOptionAnswer(item, fillin);
					if (checkResult != null) {
						return checkResult;
					}

				}
				// 正常的情況是：必填題有作答，每跑完一題，qIds 會包含必填題的題號
				// 為必填題 且 qIds 不包含必填題的題號，若為true => 未回答該必填題
				if (item.isNecessary() && !qIds.contains(item.getId())) {
					return new BasicRes(ResMessage.ANSWER_IS_REQUIRED.getCode(),
							ResMessage.ANSWER_IS_REQUIRED.getMessage());
				}
			}
			fillinStr = mapper.writeValueAsString(newFillinList);
		} catch (JsonProcessingException e) {
			return new BasicRes(ResMessage.JSON_PROCESSING_EXCEPTION.getCode(),
					ResMessage.JSON_PROCESSING_EXCEPTION.getMessage());
		}
		responseDao.save(new Response(req.getQuizId(), req.getName(), req.getPhone(), req.getEmail(), //
				req.getAge(), fillinStr));
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	// 私有方法
	private BasicRes checkParams(FillinReq req) {
		if (req.getQuizId() <= 0) {
			return new BasicRes(ResMessage.PARAM_QUIZ_ID_ERROR.getCode(), //
					ResMessage.PARAM_QUIZ_ID_ERROR.getMessage());
		}
		if (!StringUtils.hasText(req.getName())) {
			return new BasicRes(ResMessage.PARAM_NAME_IS_REQUIRED.getCode(), //
					ResMessage.PARAM_NAME_IS_REQUIRED.getMessage());
		}
		if (!StringUtils.hasText(req.getPhone())) {
			return new BasicRes(ResMessage.PARAM_PHONE_IS_REQUIRED.getCode(), //
					ResMessage.PARAM_PHONE_IS_REQUIRED.getMessage());
		}
		if (!StringUtils.hasText(req.getEmail())) {
			return new BasicRes(ResMessage.PARAM_EMAIL_IS_REQUIRED.getCode(), //
					ResMessage.PARAM_EMAIL_IS_REQUIRED.getMessage());
		}
		if (req.getAge() > 99 || req.getAge() <= 12) {
			return new BasicRes(ResMessage.PARAM_AGE_NOT_QUALIFIED.getCode(), //
					ResMessage.PARAM_AGE_NOT_QUALIFIED.getMessage());
		}
		return null;
	}

	// 私有方法
	private BasicRes checkOptionAnswer(Question item, Fillin fillin) {
		// 1.排除：必填題但沒值
		// fillin 中的答案沒有值，返回錯誤
		if (item.isNecessary() && !StringUtils.hasText(fillin.getAnswer())) {
			return new BasicRes(ResMessage.ANSWER_IS_REQUIRED.getCode(), ResMessage.ANSWER_IS_REQUIRED.getMessage());
		}
		// 2. 排除題型是單選 但 answerArray 的長度 > 1
		String answerStr = fillin.getAnswer();
		// 把 answerStr(答案) 切割成陣列
		String[] answerArray = answerStr.split(";");
		if (item.getType().equalsIgnoreCase(OptionType.SINGLE_CHOICE.getType()) //
				&& answerArray.length > 1) {
			return new BasicRes(ResMessage.ANSWER_OPTION_TYPE_IS_NOT_MATCH.getCode(),
					ResMessage.ANSWER_OPTION_TYPE_IS_NOT_MATCH.getMessage());
		}
		// 3. 排除簡答題；option type 是 text (因為下面要檢查選項與答案是否一致)
		if (item.getType().equalsIgnoreCase(OptionType.TEXT.getType())) {
			// 表示當次的檢查是成功的，原本因在 for迴圈 用 continue 跳過下面的檢查
			return null;
		}
		// 把 options 切成 Array
		String[] optionArray = item.getOptions().split(";");
		// 把 optionArray 轉成 List，因為要使用 List 中的 contains 方法
		List<String> optionList = List.of(optionArray);
		// 4. 檢查答案跟選項一致
		for (String str : answerArray) {
			// 假設 item.getOptions() 的值是: "AB;BC;C;D"
			// 轉成 List 後的 optionList = ["AB", "BC", "C", "D"]
			// 假設 answerArray = [AB, B]
			// for 迴圈中就是把 AB 和 B 比對是否被包含在 optionList 中
			// List 的 contains 方法是比較元素，所以範例中，AB是有包含，B是沒有
			// 排除以下:
			// 1. 必填 && 答案選項不一致
			if (item.isNecessary() && !optionList.contains(str)) {
				return new BasicRes(ResMessage.ANSWER_OPTION_IS_NOT_MATCH.getCode(),
						ResMessage.ANSWER_OPTION_IS_NOT_MATCH.getMessage());
			}
			// 2. 非必填 && 有答案 && 答案選項不一致
			if (!item.isNecessary() && StringUtils.hasText(str) && !optionList.contains(str)) {
				return new BasicRes(ResMessage.ANSWER_OPTION_IS_NOT_MATCH.getCode(),
						ResMessage.ANSWER_OPTION_IS_NOT_MATCH.getMessage());
			}
		}
		return null;
	}

	@Override
	public FeedbackRes feedback(FeedbackReq req) {
		Optional<Quiz> op = quizDao.findById(req.getQuizId());
		// 檢查
		if (op.isEmpty()) {
			return new FeedbackRes(ResMessage.QUIZ_NOT_FOUND.getCode(), //
					ResMessage.QUIZ_NOT_FOUND.getMessage());
		}
		Quiz quiz = op.get();
		List<Feedback> feedbackList = new ArrayList<>();
		try {
			// 撈取同一份問卷的回饋
			List<Response> resList = responseDao.findByQuizId(req.getQuizId());

			// 遍歷 resList
			for (Response resItem : resList) {
				List<Fillin> fillinList = mapper.readValue(resItem.getFillin(), new TypeReference<>() {
				});
				// 前4參數從 Quiz 取出，name/phone/email/age 從 Response 取出，
				// 最後 fillinList 從 Response 取出(原形 String )並轉換成 List
				FeedbackDetail detail = new FeedbackDetail(quiz.getName(), quiz.getDescription(), //
						quiz.getStartDate(), quiz.getEndDate(), resItem.getName(), resItem.getPhone(), //
						resItem.getEmail(), resItem.getAge(), fillinList);
				Feedback feedback = new Feedback(resItem.getName(), resItem.getFillinDateTime(), detail);
				feedbackList.add(feedback);
			}
		} catch (Exception e) {
			return new FeedbackRes(ResMessage.JSON_PROCESSING_EXCEPTION.getCode(),
					ResMessage.JSON_PROCESSING_EXCEPTION.getMessage());
		}
		// 要加 feedbackList 的原因是：如果只回 SUCCESS，並不會有資料顯示
		return new FeedbackRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), feedbackList);
	}

	@Override
	public StatisticsRes statistics(FeedbackReq req) {
		List<Response> responseList = responseDao.findByQuizId(req.getQuizId());
		// 計算所有回答之 題號 選項 次數
		//  qId(題號)      選項 出現的次數
		Map<Integer, Map<String, Integer>> countMap = new HashMap<>();
		for (Response item : responseList) {
			String fillinStr = item.getFillin();
			try {
				List<Fillin> fillinList = mapper.readValue(fillinStr, new TypeReference<>() {
				});
				for (Fillin fillinEach : fillinList) {
					Map<String, Integer> optionCountMap = new HashMap<>();
					// 排除簡答題：不列入統計
					if (fillinEach.getType().equalsIgnoreCase(OptionType.TEXT.getType())) {
						continue;
					}
					// 每個選項之間是用分號(";")串接
					String optionStr = fillinEach.getOptions();
					String[] optionArray = optionStr.split(";");
					// 答案
					String answer = fillinEach.getAnswer();
					answer = ";" + answer + ";"; // 理由同下
					for (String option : optionArray) {
						// 比對答案中每個選項出現的次數
						// 避免某個選項是另一個選項的其中一部份 ex. 紅茶, 紅茶拿鐵, 烏龍紅茶 都是選項，
						// 要找出紅茶出現的次數，紅茶拿鐵、烏龍紅茶皆不能算
						// 所以需要在每個選項"前後"再加上分號，會用分號是因為答案的串接使用分號
						// 後續要找出現次數時就會是用 ;紅茶; 來找
						String newOption = ";" + option + ";";
						// 透過將選項被空白取代，可以計算出減少的長度，需用原始answer
						String newAnswerStr = answer.replace(newOption, "");
						// 計算該選項出現的次數；
						// 原本字串的長度 - 被取代後字串的長度 再除以選項的長度才會是次數
						// 否則只是計算出少了幾個字元非次數
						int count = (answer.length() - newAnswerStr.length()) / newOption.length();
						
						// 紀錄每一題的統計
						// 取出
						optionCountMap = countMap.getOrDefault(fillinEach.getqId(), optionCountMap);
						// 取出 選項(key) 對應的次數
						// getOrDefault(option, 0)：map 中沒有 key 的話，就會返回 0
						int oldCount = optionCountMap.getOrDefault(option, 0);
						// 累加 oldCount + count
						optionCountMap.put(option, oldCount + count);
						// 把有累加次數的 optionCountMap 覆蓋回 countMap 中(相同的題號)
						countMap.put(fillinEach.getqId(), optionCountMap);
					}

				}
			} catch (JsonProcessingException e) {
				return new StatisticsRes(ResMessage.JSON_PROCESSING_EXCEPTION.getCode(),
						ResMessage.JSON_PROCESSING_EXCEPTION.getMessage());
			}
		}
		Optional<Quiz> op = quizDao.findById(req.getQuizId());
		if (op.isEmpty()) {
			return new StatisticsRes(ResMessage.QUIZ_NOT_FOUND.getCode(), ResMessage.QUIZ_NOT_FOUND.getMessage());
		}
		Quiz quiz = op.get();
		return new StatisticsRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), //
				quiz.getName(), quiz.getStartDate(), quiz.getEndDate(), countMap);
	}

}

package com.example.quiz;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import com.example.quiz.constants.OptionType;
import com.example.quiz.repository.QuizDao;
import com.example.quiz.service.ifs.QuizService;
import com.example.quiz.vo.BasicRes;
import com.example.quiz.vo.CreateOrUpdateReq;
import com.example.quiz.vo.Question;

@SpringBootTest // 跑測試會從main進去
public class QuizServiceTests {

	@Autowired
	private QuizService quizService;
	
	@Autowired
	private QuizDao quizDao;

	@Test
	public void createTest() {
		List<Question> questionList = new ArrayList<>();
		questionList.add(new Question(1, "健康餐?", "鮭魚; 松阪豬; 烤雞腿; 牛小排", //
				OptionType.SINGLE_CHOICE.getType(), true));
		questionList.add(new Question(2, "丹丹?", "1號餐; 2號餐; 3號餐; 4號餐", //
				OptionType.SINGLE_CHOICE.getType(), true));
		questionList.add(new Question(3, "麥當勞?", "雞塊餐; 炸雞餐; 牛肉堡; 雞腿堡", //
				OptionType.SINGLE_CHOICE.getType(), true));
		questionList.add(new Question(4, "炒飯?", "豬肉炒飯; 海鮮炒飯; 干貝馬鈴薯(虧); 香嫦炒飯", //
				OptionType.MULTI_CHOICE.getType(), true));
		CreateOrUpdateReq req = new CreateOrUpdateReq("午餐吃什麼?", "調查國人午餐選擇種類", LocalDate.of(2024, 06, 01), //
				LocalDate.of(2024, 06, 30), questionList, true);
		BasicRes res = quizService.createOrUpdate(req);
		Assert.isTrue(res.getStatusCode() == 200, "create test false!"); // 若結果不是200，會顯示後面的字串
		// 通常做完測試會 刪除測試資料 TODO
	}
	
	
	
	@Test
	public void createNameErrorTest() {
		List<Question> questionList = new ArrayList<>();
		questionList.add(new Question(1, "健康餐?", "鮭魚; 松阪豬; 烤雞腿; 牛小排", //
				"單選", true));
		questionList.add(new Question(2, "丹丹?", "1號餐; 2號餐; 3號餐; 4號餐", //
				OptionType.SINGLE_CHOICE.getType(), true));
		questionList.add(new Question(3, "麥當勞?", "雞塊餐; 炸雞餐; 牛肉堡; 雞腿堡", //
				OptionType.SINGLE_CHOICE.getType(), true));
		questionList.add(new Question(4, "炒飯?", "豬肉炒飯; 海鮮炒飯; 干貝馬鈴薯(虧); 香嫦炒飯", //
				OptionType.MULTI_CHOICE.getType(), true));
		CreateOrUpdateReq req = new CreateOrUpdateReq("", "調查國人午餐選擇種類", LocalDate.of(2024, 6, 1), //
				LocalDate.of(2024, 6, 30), questionList, true);
		BasicRes res = quizService.createOrUpdate(req);
		Assert.isTrue(res.getStatusCode() == 400, "create test false!"); // 若結果不是200，會顯示後面的字串
	}
	
	@Test
	public void createStartDateErrorTest() {
		List<Question> questionList = new ArrayList<>();
		questionList.add(new Question(1, "健康餐?", "鮭魚; 松阪豬; 烤雞腿; 牛小排", //
				OptionType.SINGLE_CHOICE.getType(), true));
		questionList.add(new Question(2, "丹丹?", "1號餐; 2號餐; 3號餐; 4號餐", //
				OptionType.SINGLE_CHOICE.getType(), true));
		questionList.add(new Question(3, "麥當勞?", "雞塊餐; 炸雞餐; 牛肉堡; 雞腿堡", //
				OptionType.SINGLE_CHOICE.getType(), true));
		questionList.add(new Question(4, "炒飯?", "豬肉炒飯; 海鮮炒飯; 干貝馬鈴薯(虧); 香嫦炒飯", //
				OptionType.SINGLE_CHOICE.getType(), true));
		CreateOrUpdateReq req = new CreateOrUpdateReq("午餐吃什麼?", "調查國人午餐選擇種類", LocalDate.of(2024, 6, 01), //
				LocalDate.of(2024, 6, 30), questionList, true);
		BasicRes res = quizService.createOrUpdate(req);
		Assert.isTrue(res.getMessage().equalsIgnoreCase("Param start date error!!"), "create test false!"); // 若結果不是200，會顯示後面的字串
	}
	
	// 測試合併 先測false 再測 success，業界多用此方法
	@Test
	public void createTest1() {
		List<Question> questionList = new ArrayList<>();
		questionList.add(new Question(1, "健康餐?", "鮭魚; 松阪豬; 烤雞腿; 牛小排", //
				OptionType.SINGLE_CHOICE.getType(), true));
		// test name error
		CreateOrUpdateReq req = new CreateOrUpdateReq("", "調查國人午餐選擇種類", LocalDate.of(2024, 6, 1), //
				LocalDate.of(2024, 6, 30), questionList, true);
		BasicRes res = quizService.createOrUpdate(req);
		Assert.isTrue(res.getMessage().equalsIgnoreCase("Param name error!!"), "create test false!");
		// test start date error 今天是5/30，開始日期不可以是當天或之前
		req = new CreateOrUpdateReq("午餐吃什麼?", "調查國人午餐選擇種類", LocalDate.of(2024, 5, 30), //
				LocalDate.of(2024, 6, 30), questionList, true);
		res = quizService.createOrUpdate(req);
		Assert.isTrue(res.getMessage().equalsIgnoreCase("Param start date error!!"), "create test false!");
		// test end date error 結束日期不可比開始日期早
		req = new CreateOrUpdateReq("午餐吃什麼?", "調查國人午餐選擇種類", LocalDate.of(2024, 6, 30), //
				LocalDate.of(2024, 6, 1), questionList, true);
		res = quizService.createOrUpdate(req);
		Assert.isTrue(res.getMessage().equalsIgnoreCase("Param end date error!!"), "create test false!");
		
		// 剩餘的邏輯全部判斷完之後，最後才會是測試成功
		req = new CreateOrUpdateReq("午餐吃什麼?", "調查國人午餐選擇種類", LocalDate.of(2024, 6, 1), //
				LocalDate.of(2024, 6, 2), questionList, true);
		res = quizService.createOrUpdate(req);
		Assert.isTrue(res.getStatusCode() == 200, "create test false!");
		
		// 通常做完測試會 刪除測試資料 TODO
	}
	
	@Test
	public void createOrUpdateTest1() {
		List<Question> questionList = new ArrayList<>();
		questionList.add(new Question(1, "健康餐?", "鮭魚; 松阪豬; 烤雞腿; 牛小排", //
				OptionType.SINGLE_CHOICE.getType(), true));
	}
}

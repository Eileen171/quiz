package com.example.quiz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
class QuizApplicationTests {

	@Test
	public void contextLoads() {

		String str = "A;B;C;D"; // 選項
		String ansStr = "A;E"; // 回答
		String[] ansArray = ansStr.split(";"); // 把回答切成陣列
		for (String item : ansArray) { // 一一比對
			System.out.println(str.contains(item));
		}
	}

	@Test
	public void test2() {
		List<String> list = List.of("A", "B", "C", "D", "E");
		String str = "AABBBCDDAEEEAACCD"; 
		Map<String, Integer> map = new HashMap<>();
		// 遍歷 list
		for(String item : list) {
			// 用空字串取代找的target
			String newStr = str.replace(item, "");
			// 原始長度 - 被取代後的長度 = target出現的次數
			// 放入 map<字元, 次數>
			map.put(item, str.length() - newStr.length());
		}
		System.out.println(map);
	}
}

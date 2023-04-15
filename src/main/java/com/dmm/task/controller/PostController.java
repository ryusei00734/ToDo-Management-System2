package com.dmm.task.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dmm.task.data.entity.Posts;
import com.dmm.task.form.PostForm;
import com.dmm.task.repository.PostsRepository;
import com.dmm.task.service.AccountUserDetails;

@Controller
@RequestMapping(value = "topPage")
public class PostController {

	@Autowired
	private ScheduleService scheduleService;

	@Autowired
	private User user;

	@RequestMapping(value = "getPage", method = RequestMethod.GET)
	public String getPage(PostForm form, Model model) {

		Calendar rightNow = Calendar.getInstance();
		int day = rightNow.get(Calendar.DATE);
		int year = rightNow.get(Calendar.YEAR);
		int month = rightNow.get(Calendar.MONTH);

/* 今月のはじまり */
		rightNow.set(year, month, 1);
		int startWeek = rightNow.get(Calendar.DAY_OF_WEEK);

/* 先月分の日数 */
		rightNow.set(year, month, 0);
		int beforeMonthlastDay = rightNow.get(Calendar.DATE);

		/* 今月分の日数 */
		rightNow.set(year, month + 1, 0);
		int thisMonthlastDay = rightNow.get(Calendar.DATE);

		int[] calendarDay = new int[42];		/* 最大で7日×6週 */
		int count = 0;

		for (int i = startWeek - 2; i >= 0; i--) {
			calendarDay[count++] = beforeMonthlastDay - i;
		}

		for (int i = 1; i <= thisMonthlastDay; i++) {
			calendarDay[count++] = i;
		}

		int nextMonthDay = 1;
		while (count % 7 != 0) {
			calendarDay[count++] = nextMonthDay++;
		}

		int weekCount = count / 7;

		for (int i = 0; i < weekCount; i++) {
			for (int j = i * 7; j < i * 7 + 7; j++) {
				if (calendarDay[j] < 10) {
					System.out.print(" " + calendarDay[j] + " ");
				} else {
					System.out.print(calendarDay[j] + " ");
				}
			
			System.out.println():
			}
		

	/**
	 * 投稿を作成.
	 * 
	 * @param postForm 送信データ
	 * @param user     ユーザー情報
	 * @return 遷移先
	 */
	@PostMapping("/posts/create")
	public String create(@Validated PostForm postForm, BindingResult bindingResult,
			@AuthenticationPrincipal AccountUserDetails user, Model model) {
		// バリデーションの結果、エラーがあるかどうかチェック
		if (bindingResult.hasErrors()) {
			// エラーがある場合は投稿登録画面を返す
			List<Posts> list = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
			model.addAttribute("posts", list);
			model.addAttribute("postForm", postForm);
			return "/posts";
		}

		Posts post = new Posts();
		post.setName(user.getName());
		post.setTitle(postForm.getTitle());
		post.setText(postForm.getText());
		post.setDate(LocalDateTime.now());

		repo.save(post);

		return "redirect:/posts";
	}

	/**
	 * 投稿を削除する
	 * 
	 * @param id 投稿ID
	 * @return 遷移先
	 */
	@PostMapping("/posts/delete/{id}")
	public String delete(@PathVariable Integer id) {
		repo.deleteById(id);
		return "redirect:/posts";
	}
}
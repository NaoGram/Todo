package com.example.Todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Todo.entity.User;
import com.example.Todo.entity.User.Authority;
import com.example.Todo.form.UserForm;
import com.example.Todo.repository.UserRepository;

@Controller
public class UsersController {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository repository;

	// 新しいユーザー登録画面を表示するエンドポイント
	@GetMapping(path = "/users/new")
	public String newUser(Model model) {
		model.addAttribute("form", new UserForm());
		return "users/new";
	}

	// ユーザーの新規作成を行うエンドポイント
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public String create(@Validated @ModelAttribute("form") UserForm form, BindingResult result, Model model,
			RedirectAttributes redirAttrs) {
		// フォームからユーザー情報を取得
		String name = form.getName();
		String email = form.getEmail();
		String password = form.getPassword();
		String passwordConfirmation = form.getPasswordConfirmation();

		// 重複したメールアドレスの場合はエラーメッセージを追加
		if (repository.findByUsername(email) != null) {
			FieldError fieldError = new FieldError(result.getObjectName(), "email", "その E メールはすでに使用されています。");
			result.addError(fieldError);
		}

		// パスワードとパスワード確認の一致検証を追加
		if (!password.equals(passwordConfirmation)) {
			FieldError fieldError = new FieldError(result.getObjectName(), "passwordConfirmation", "パスワードが一致しません。");
			result.addError(fieldError);
		}

		// バリデーションエラーがある場合はエラーメッセージを表示
		if (result.hasErrors()) {
			model.addAttribute("hasMessage", true);
			model.addAttribute("class", "alert-danger");
			model.addAttribute("message", "ユーザー登録に失敗しました。");
			return "users/new";
		}

		// パスワードをエンコードして新しいユーザーエンティティを作成しデータベースに保存
		User entity = new User(email, name, passwordEncoder.encode(password), Authority.ROLE_USER);
		repository.saveAndFlush(entity);

		// 登録完了メッセージを表示
		model.addAttribute("hasMessage", true);
		model.addAttribute("class", "alert-info");
		model.addAttribute("message", "ユーザー登録が完了しました。");

		return "layouts/complete";
	}
}

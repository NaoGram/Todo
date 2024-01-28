package com.example.Todo.controller;

import com.example.Todo.entity.Task;
import com.example.Todo.entity.TaskGroup;
import com.example.Todo.entity.User;
import com.example.Todo.form.TaskForm;
import com.example.Todo.form.UserForm;
import com.example.Todo.form.GroupForm;
import com.example.Todo.repository.TaskGroupRepository;
import com.example.Todo.repository.TaskRepository;
import com.example.Todo.repository.UserRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import java.util.Optional;

import org.springframework.context.MessageSource;

import java.util.Locale;

@Controller
public class TaskController {
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private TaskGroupRepository taskGroupRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	// ユーザーのタスク一覧を表示するエンドポイント
	@GetMapping("/Tasks")
	public String showTasks(Model model) {
		// 認証情報を取得
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByUsername(authentication.getName());

		// タスクグループ一覧を取得してHTMLページに渡す
		List<TaskGroup> groups = taskGroupRepository.findAll();
		model.addAttribute("groups", groups);

		// HTML拡張子は通常コメントアウトしない
		return "tasks/index";
	}
	
	

	// 新しいタスクのフォームを表示するエンドポイント
	@GetMapping("/new")
	public String showTaskForm(Model model) {
		model.addAttribute("taskForm", new TaskForm());
		List<TaskGroup> taskGroups = taskGroupRepository.findAll();
		model.addAttribute("taskGroups", taskGroups);
		return "tasks/new";
	}

	// 新しいタスクを作成するエンドポイント
	@RequestMapping(value = "/tasks2", method = RequestMethod.POST)
	public String createTask(@Valid @ModelAttribute("taskForm") TaskForm taskForm, BindingResult result, Model model,
			Locale locale) {
		System.out.println("ここオッケー");
		taskForm.printout();
		// バリデーションエラーがある場合は、フォームを再表示
//		if (result.hasErrors()) {
//			model.addAttribute("hasMessage", true);
//			model.addAttribute("class", "alert-danger");
//			// model.addAttribute("message", "投稿に失敗しました。");
//			model.addAttribute("message", messageSource.getMessage("テスト", new String[] {}, Locale.getDefault()));
//			List<TaskGroup> taskGroups = taskGroupRepository.findAll();
//			return "tasks/new"; // リダイレクトではなく、フォワードする
//		}

		// 認証情報を取得してタスクを作成しデータベースに保存
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByUsername(authentication.getName());
		taskForm.setTaskGroupId(1);
		System.out.println(taskForm);
		Task task = convertToTask(taskForm);
		task.setUser(user);
//		task.setTaskGroup(taskForm.getTaskGroupId());
		Optional<TaskGroup> taskGroupOptional = taskGroupRepository.findById(taskForm.getTaskGroupId());
		TaskGroup taskGroup = taskGroupOptional.get();
		
		System.out.println(taskGroup);
		task.setTaskGroup(taskGroup);
		taskRepository.saveAndFlush(task);

		return "redirect:/tasks";
	}

	// タスクをTaskFormに変換するヘルパーメソッド
	private TaskForm convertToTaskForm(Task task) {
		return modelMapper.map(task, TaskForm.class);
	}

	// TaskFormをタスクに変換するヘルパーメソッド
	private Task convertToTask(TaskForm taskForm) {
		return modelMapper.map(taskForm, Task.class);
	}

	// タスクを編集するフォームを表示するエンドポイント
	@GetMapping("/Tasks/edit/{taskId}")
	public String showEditTaskForm(@PathVariable int taskId, Model model) {
		Task task = taskRepository.findById(taskId).orElse(new Task());
		model.addAttribute("TaskForm", task);
		return "Tasks/edit";
	}

	// タスクが完了した時のエンドポイント
	@PostMapping("/Tasks/complete/{taskId}")
	public String completeTask(@PathVariable int taskId) {
		// タスクをデータベースから取得
		Optional<Task> optionalTask = taskRepository.findById(taskId);

		if (optionalTask.isPresent()) {
			// タスクが見つかった場合
			Task task = optionalTask.get();
			// タスクのステータスを完了に設定
			task.setStatus("completed");
			// タスクを保存
			taskRepository.save(task);
		}

		return "redirect:/Tasks";
	}

	// タスクを編集してデータベースに保存するエンドポイント
	@PostMapping("/Tasks/edit/{taskId}")
	public String editTask(@PathVariable int taskId, @ModelAttribute("TaskForm") Task task) {
		task.setTaskId(taskId);
		taskRepository.save(task);
		return "redirect:/Tasks";
	}

	// タスクを削除するエンドポイント
	@GetMapping("/Tasks/delete/{taskId}")
	public String deleteTask(@PathVariable int taskId) {
		taskRepository.deleteById(taskId);
		return "redirect:/tasks";
	}

	// ユーザー一覧を表示するエンドポイント
	@GetMapping("/users")
	public String showUsers(Model model) {
		List<User> users = userRepository.findAll();
		List<UserForm> userForms = users.stream().map(user -> modelMapper.map(user, UserForm.class))
				.collect(Collectors.toList());

		model.addAttribute("users", userForms);
		return "users/user-list";
	}

	// 新しいユーザーを作成するエンドポイント
	@PostMapping("/users")
	public String createUser(@Valid @ModelAttribute("userForm") UserForm userForm, BindingResult result) {
		// バリデーションエラーがある場合は、エラー表示
		if (result.hasErrors()) {
			return "users/new-user";
		}

		// UserFormをUserに変換してデータベースに保存
		User user = modelMapper.map(userForm, User.class);
		userRepository.save(user);

		return "redirect:/tasks/users";
	}

	// 新しいタスクグループのフォームを表示するエンドポイント
	@GetMapping("/groups/new")
	public String showGroupForm(Model model) {
		model.addAttribute("groupForm", new TaskGroup());
		return "groups/new";
	}

	// 新しいタスクグループを作成するエンドポイント
	@PostMapping("/groups")
	public String createGroup(@ModelAttribute("groupForm") TaskGroup taskGroup) {
		taskGroupRepository.save(taskGroup);
		return "redirect:/groups/list";
	}

	// タスクグループ一覧を表示するエンドポイント
	@GetMapping("/groups")
	public String showGroups(Model model) {
		List<TaskGroup> groups = taskGroupRepository.findAll();
		model.addAttribute("groups", groups);
		return "groups/list";
	}

	// タスクグループを編集するフォームを表示するエンドポイント
	@GetMapping("/groups/edit/{groupId}")
	public String showEditGroupForm(@PathVariable int groupId, Model model) {
		TaskGroup taskGroup = taskGroupRepository.findById(groupId).orElse(new TaskGroup());
		model.addAttribute("groupForm", taskGroup);
		return "groups/edit";
	}

	// タスクグループを編集してデータベースに保存するエンドポイント
	@PostMapping("/groups/edit/{groupId}")
	public String editGroup(@PathVariable int groupId, @ModelAttribute("groupForm") TaskGroup taskGroup) {
		taskGroup.setId(groupId);
		taskGroupRepository.save(taskGroup);
		return "redirect:/groups/list";
	}

	// タスクグループを削除するエンドポイント
	@GetMapping("/groups/delete/{groupId}")
	public String deleteGroup(@PathVariable int groupId) {
		taskGroupRepository.deleteById(groupId);
		return "redirect:/groups/list";
	}

}

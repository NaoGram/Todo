package com.example.Todo.controller;

import com.example.Todo.entity.Task;
import com.example.Todo.entity.TaskGroup;
import com.example.Todo.entity.User;
import com.example.Todo.form.TaskForm;
import com.example.Todo.form.UserForm;
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

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/Tasks")
public class TaskController {

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private TaskGroupRepository taskGroupRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	@GetMapping
	public String showTasks(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByUsername(authentication.getName());

		List<Task> tasks = taskRepository.findByUserAndStatusOrderByUpdatedAtDesc(user, "未完了");
		List<TaskForm> taskForms = tasks.stream().map(this::convertToTaskForm).collect(Collectors.toList());

		model.addAttribute("tasks", taskForms);
		return "tasks/index.html";
	}

	@GetMapping("/new")
	public String showTaskForm(Model model) {
		model.addAttribute("taskForm", new TaskForm());
		List<TaskGroup> taskGroups = taskGroupRepository.findAll();
		model.addAttribute("taskGroups", taskGroups);
		return "tasks/new";
	}

	@PostMapping
	public String createTask(@Valid @ModelAttribute("taskForm") TaskForm taskForm, BindingResult result) {
		if (result.hasErrors()) {
			List<TaskGroup> taskGroups = taskGroupRepository.findAll();
			return "redirect:/tasks/new";
		}

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByUsername(authentication.getName());

		Task task = convertToTask(taskForm);
		task.setUser(user);
		taskRepository.save(task);

		return "redirect:/tasks";
	}

	private TaskForm convertToTaskForm(Task task) {
		return modelMapper.map(task, TaskForm.class);
	}

	private Task convertToTask(TaskForm taskForm) {
		return modelMapper.map(taskForm, Task.class);
	}

	// Other methods for user-related functionalities

	@GetMapping("/users")
	public String showUsers(Model model) {
		List<User> users = userRepository.findAll();
		List<UserForm> userForms = users.stream().map(user -> modelMapper.map(user, UserForm.class))
				.collect(Collectors.toList());

		model.addAttribute("users", userForms);
		return "users/user-list";
	}

	@GetMapping("/users/new")
	public String showUserForm(Model model) {
		model.addAttribute("userForm", new UserForm());
		return "users/new-user";
	}

	@PostMapping("/users")
	public String createUser(@Valid @ModelAttribute("userForm") UserForm userForm, BindingResult result) {
		if (result.hasErrors()) {
			return "users/new-user";
		}

		User user = modelMapper.map(userForm, User.class);
		userRepository.save(user);

		return "redirect:/tasks/users";
	}
	
}
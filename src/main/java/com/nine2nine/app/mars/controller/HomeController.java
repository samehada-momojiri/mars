package com.nine2nine.app.mars.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nine2nine.app.mars.client.JupiterTaskClient;
import com.nine2nine.app.mars.client.JupiterTaskClient.CreateTaskRequest;
import com.nine2nine.app.mars.client.JupiterTaskClient.JupiterTask;

@Controller
public class HomeController {

	private final JupiterTaskClient jupiterTaskClient;

	public HomeController(JupiterTaskClient jupiterTaskClient) {
		this.jupiterTaskClient = jupiterTaskClient;
	}

	@GetMapping("/")
	public String init(Model model) {
		bindTasks(model);
		return "index";
	}

	@GetMapping("/explore")
	public String explore(Model model) {
		bindTasks(model);
		return "index";
	}

	@GetMapping("/tasks/new")
	public String newTask(Model model) {
		if (!model.containsAttribute("taskForm")) {
			model.addAttribute("taskForm", new TaskForm("", "", false));
		}
		return "task-form";
	}

	@PostMapping("/tasks")
	public String createTask(@ModelAttribute TaskForm taskForm, RedirectAttributes redirectAttributes) {
		if (isBlank(taskForm.title()) || isBlank(taskForm.description())) {
			redirectAttributes.addFlashAttribute("taskError", "タイトルと概要は必須です。");
			redirectAttributes.addFlashAttribute("taskForm", taskForm);
			return "redirect:/tasks/new";
		}

		try {
			jupiterTaskClient.createTask(new CreateTaskRequest(
				taskForm.title().trim(),
				taskForm.description().trim(),
				taskForm.completed()
			));
			return "redirect:/";
		} catch (RuntimeException ex) {
			redirectAttributes.addFlashAttribute("taskError", "タスク登録に失敗しました。Jupiter APIを確認してください。");
			redirectAttributes.addFlashAttribute("taskForm", taskForm);
			return "redirect:/tasks/new";
		}
	}

	private void bindTasks(Model model) {
		try {
			List<JupiterTask> tasks = jupiterTaskClient.findTasks();
			model.addAttribute("tasks", tasks);
			model.addAttribute("tasksError", null);
		} catch (RuntimeException ex) {
			model.addAttribute("tasks", List.of());
			model.addAttribute("tasksError", "Jupiter tasks APIに接続できませんでした。");
		}
	}

	private boolean isBlank(String value) {
		return value == null || value.isBlank();
	}

	public record TaskForm(
		String title,
		String description,
		boolean completed
	) {
	}
}

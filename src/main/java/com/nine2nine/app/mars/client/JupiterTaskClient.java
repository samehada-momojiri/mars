package com.nine2nine.app.mars.client;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/api/tasks")
public interface JupiterTaskClient {

	@GetExchange
	List<JupiterTask> findTasks();

	@PostExchange
	JupiterTask createTask(@RequestBody CreateTaskRequest request);

	record JupiterTask(
		long id,
		String title,
		String description,
		boolean completed
	) {
	}

	record CreateTaskRequest(
		String title,
		String description,
		boolean completed
	) {
	}
}

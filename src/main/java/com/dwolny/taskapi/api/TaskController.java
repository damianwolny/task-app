package com.dwolny.taskapi.api;

import com.dwolny.taskapi.model.Task;
import com.dwolny.taskapi.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

	private final TaskService taskService;

	@PostMapping
	public ResponseEntity<String> createTask(@RequestParam String input, @RequestParam String pattern) {
		Task task = taskService.createTask(input, pattern);
		taskService.calculateResult(task);
		return ResponseEntity.ok().body(task.getId());
	}

	@GetMapping
	public ResponseEntity<List<Task>> getAllTasks() {
		return ResponseEntity.ok().body(taskService.getAllTasks());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Task> getTask(@PathVariable String id) {
		return ResponseEntity.ok().body(taskService.getTask(id));
	}
}

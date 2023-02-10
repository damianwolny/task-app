package com.dwolny.taskapi.service;

import com.dwolny.taskapi.cache.TaskCacheStore;
import com.dwolny.taskapi.enums.TaskStatus;
import com.dwolny.taskapi.model.Task;
import com.dwolny.taskapi.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

	private final TaskRepository taskRepository;
	private final TaskCacheStore taskCacheStore;

	public Task createTask(String input,  String pattern) {
		Task task = new Task();
		String taskId = UUID.randomUUID().toString();
		task.setId(taskId);
		task.setStatus(TaskStatus.CREATED);
		task.setPattern(pattern);
		task.setInput(input);
		task.setProgressPercentage(0);

		log.debug("Creating task with id: '{}", taskId);

		taskRepository.save(task);
		taskCacheStore.update(taskId, task);

		return task;
	}

	public List<Task> getAllTasks() {
		return taskRepository.findAll();
	}

	public Task getTask(String taskId) {
		return taskCacheStore.getTask(taskId)
				.orElseGet(() -> taskRepository.findById(taskId).
						orElseThrow(() -> new IllegalStateException("No task found!")));
	}

	@Async
	public void calculateResult(Task task) {
		log.debug("Calculating task with id: '{}", task.getId());

		task.setStatus(TaskStatus.IN_PROGRESS);

		int position = 0;
		int typos = 0;
		int minTypos = Integer.MAX_VALUE;

		for (int i = 0; i <= task.getInput().length() - task.getPattern().length(); i++) {
			int currentTypos = 0;
			for (int j = 0; j < task.getPattern().length(); j++) {
				if (task.getInput().charAt(i + j) != task.getPattern().charAt(j)) {
					currentTypos++;
				}
			}

			if (currentTypos < minTypos) {
				minTypos = currentTypos;
				position = i;
				typos = currentTypos;
			}
			int progressPercentage = (i * 100) / (task.getInput().length() - task.getPattern().length());
			task.setProgressPercentage(progressPercentage);
			taskCacheStore.update(task.getId(), task);
		}

		task.setPosition(position);
		task.setTypos(typos);
		task.setStatus(TaskStatus.COMPLETED);
		taskCacheStore.update(task.getId(), task);
		taskRepository.save(task);
	}
}
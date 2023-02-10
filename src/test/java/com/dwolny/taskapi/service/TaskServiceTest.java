package com.dwolny.taskapi.service;

import com.dwolny.taskapi.cache.TaskCacheStore;
import com.dwolny.taskapi.enums.TaskStatus;
import com.dwolny.taskapi.model.Task;
import com.dwolny.taskapi.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskServiceTest {

	@Mock
	private TaskRepository taskRepository;

	@Mock
	private TaskCacheStore taskCacheStore;

	private TaskService taskService;

	@BeforeEach
	void setup() {
		taskService = new TaskService(taskRepository, taskCacheStore);
	}

	@Test
	void testCreateTask() {
		String input = "ABCDEFG";
		String pattern = "CFG";

		Task result = taskService.createTask(input, pattern);

		assertEquals(TaskStatus.CREATED, result.getStatus());
		assertEquals(pattern, result.getPattern());
		assertEquals(input, result.getInput());
		assertEquals(0, result.getProgressPercentage());

		verify(taskRepository, times(1)).save(result);
		verify(taskCacheStore, times(1)).update(result.getId(), result);
	}

	@Test
	void testGetAllTasks() {
		List<Task> tasks = Arrays.asList(new Task(), new Task(), new Task());
		when(taskRepository.findAll()).thenReturn(tasks);

		List<Task> result = taskService.getAllTasks();
		assertEquals(tasks, result);

		verify(taskRepository, times(1)).findAll();
		verifyNoMoreInteractions(taskRepository);
	}

	@Test
	void testGetTask_returnFromCacheStore() {
		String taskId = UUID.randomUUID().toString();
		Task task = new Task();
		Optional<Task> taskOptional = Optional.of(task);

		when(taskCacheStore.getTask(taskId)).thenReturn(taskOptional);

		Task result = taskService.getTask(taskId);
		assertEquals(task, result);

		verify(taskCacheStore, times(1)).getTask(taskId);
		verifyNoMoreInteractions(taskCacheStore);
	}

	@Test
	void testGetTask_returnFromTaskRepository() {
		String taskId = UUID.randomUUID().toString();
		Task task = new Task();
		Optional<Task> taskOptional = Optional.of(task);

		when(taskCacheStore.getTask(taskId)).thenReturn(Optional.empty());
		when(taskRepository.findById(taskId)).thenReturn(taskOptional);

		Task result = taskService.getTask(taskId);
		assertEquals(task, result);

		verify(taskCacheStore, times(1)).getTask(taskId);
		verify(taskRepository, times(1)).findById(taskId);
		verifyNoMoreInteractions(taskRepository);
	}

	@Test
	public void testGetTask_throwsException() {
		String taskId = UUID.randomUUID().toString();
		when(taskCacheStore.getTask(taskId)).thenReturn(Optional.empty());
		when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

		assertThrows(IllegalStateException.class, ()-> taskService.getTask(taskId));
	}
}
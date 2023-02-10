package com.dwolny.taskapi.service;

import com.dwolny.taskapi.enums.TaskStatus;
import com.dwolny.taskapi.model.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private TaskService taskService;

	@Test
	public void testCreateTask() throws Exception {
		String taskId = UUID.randomUUID().toString();
		String pattern = "abc";
		String input = "abcabcabc";
		Task task = new Task();
		task.setId(taskId);
		task.setStatus(TaskStatus.CREATED);
		task.setPattern(pattern);
		task.setInput(input);

		when(taskService.createTask(input, pattern)).thenReturn(task);

		mockMvc.perform(post("/api/tasks?input=abcabcabc&pattern=abc"))
				.andExpect(status().isOk())
				.andExpect(content().string(taskId));

		verify(taskService, times(1)).createTask(input, pattern);
		verify(taskService, times(1)).calculateResult(task);
	}

	@Test
	public void testGetAllTasks() throws Exception {
		Task task1 = new Task();
		task1.setId("1");
		task1.setStatus(TaskStatus.CREATED);
		task1.setPattern("abc");
		task1.setInput("abcabcabc");

		Task task2 = new Task();
		task2.setId("2");
		task2.setStatus(TaskStatus.IN_PROGRESS);
		task2.setPattern("def");
		task2.setInput("defdefdef");

		List<Task> tasks = Arrays.asList(task1, task2);
		when(taskService.getAllTasks()).thenReturn(tasks);

		mockMvc.perform(get("/api/tasks"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value("1"))
				.andExpect(jsonPath("$[0].status").value("CREATED"))
				.andExpect(jsonPath("$[0].pattern").value("abc"))
				.andExpect(jsonPath("$[0].input").value("abcabcabc"))
				.andExpect(jsonPath("$[1].id").value("2"))
				.andExpect(jsonPath("$[1].status").value("IN_PROGRESS"))
				.andExpect(jsonPath("$[1].pattern").value("def"))
				.andExpect(jsonPath("$[1].input").value("defdefdef"));

		verify(taskService, times(1)).getAllTasks();
	}

	@Test
	void testGetTask_ReturnsTask() throws Exception {
		String taskId = UUID.randomUUID().toString();
		Task task = new Task();
		task.setId(taskId);

		when(taskService.getTask(taskId)).thenReturn(task);
		MvcResult result = mockMvc.perform(get("/api/tasks/" + taskId))
				.andExpect(status().isOk())
				.andReturn();

		Task resultTask = objectMapper.readValue(result.getResponse().getContentAsString(), Task.class);
		assertEquals(task.getId(), resultTask.getId());
	}
}
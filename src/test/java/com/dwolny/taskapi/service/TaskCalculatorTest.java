package com.dwolny.taskapi.service;

import com.dwolny.taskapi.model.Task;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TaskCalculatorTest {

	@Autowired
	private TaskService taskService;

	//TODO 1. Think about moving calculation logic to another class
	//TODO 2. One business case - one test
	//TODO 3. ABCDEFG -> TDD - incorrect position. Confirm requirements are correct, if so investigate the algorithm
	@SneakyThrows
	@Test
	public void testCalculateResult() {

		//Business case no. 1

		Task task = taskService.createTask("ABCDEFG", "CFG");
		taskService.calculateResult(task);

		Thread.sleep(10);

		Task response = taskService.getTask(task.getId());
		assertEquals(4, response.getPosition());
		assertEquals(1, response.getTypos());

		//Business case no. 2

		task = taskService.createTask("ABCD", "BCD");
		taskService.calculateResult(task);

		Thread.sleep(10);

		response = taskService.getTask(task.getId());
		assertEquals(1, response.getPosition());
		assertEquals(0, response.getTypos());

		//Business case no. 3

		task = taskService.createTask("ABCD", "BWD");
		taskService.calculateResult(task);

		Thread.sleep(10);

		response = taskService.getTask(task.getId());
		assertEquals(1, response.getPosition());
		assertEquals(1, response.getTypos());

		//Business case no. 4

		task = taskService.createTask("ABCABC", "ABC");
		taskService.calculateResult(task);

		Thread.sleep(10);

		response = taskService.getTask(task.getId());
		assertEquals(0, response.getPosition());
		assertEquals(0, response.getTypos());

	}
}
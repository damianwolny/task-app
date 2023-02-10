package com.dwolny.taskapi.repository;

import com.dwolny.taskapi.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, String> { }
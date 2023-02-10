package com.dwolny.taskapi.model;

import com.dwolny.taskapi.enums.TaskStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Entity
public class Task {

	@Id
	private String id;
	private String input;
	private String pattern;
	private Integer position;
	private Integer typos;
	private Integer progressPercentage;
	private TaskStatus status;
}

package com.github.yatol.backend.entities;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ToDoTask extends AbstractEntity {

	private ToDoList toDoList;
	private String description;
	private boolean done;
}

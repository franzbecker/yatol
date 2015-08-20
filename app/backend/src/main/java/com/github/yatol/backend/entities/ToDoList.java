package com.github.yatol.backend.entities;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ToDoList extends AbstractEntity {

	private String name;
	private User owner;

}

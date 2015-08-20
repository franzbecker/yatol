package com.github.yatol.backend.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "YatolUser")
public class User extends AbstractEntity {

	private String username;
}

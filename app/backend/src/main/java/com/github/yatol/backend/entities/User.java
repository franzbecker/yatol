package com.github.yatol.backend.entities;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class User extends AbstractEntity {

  private String username;
}

package com.github.yatol.backend.entities;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter(value = AccessLevel.PROTECTED)
public class AbstractEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

}

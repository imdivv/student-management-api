
package com.example.student;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@JsonPropertyOrder({"id", "name", "age", "course"})
public class Student {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	int id;
	
	@NotBlank
	String name;
	
	@Min(18) @Max(25)
	int age;
	
	@NotBlank
	String course;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	public String getCourse() {
		return course;
	}
	
	public void setCourse(String course) {
		this.course = course;
	}
}
package com.example.student.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class StudentRequestDTO {
	
	@NotBlank(message="Name Cannont be Blank.")
	private String name;
	
	@Max(value = 25, message = "Maximum age is 25.")
	@Min(value = 18, message = "Minimum age is 18.")
	private int age;
	
	@NotBlank(message = "Course Cannot be Blank.")
	private String course;
	
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

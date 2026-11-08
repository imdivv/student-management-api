package com.example.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import com.example.student.dto.StudentRequestDTO;
import com.example.student.dto.StudentResponseDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import org.springframework.data.domain.Page;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@Tag(
	    name = "Student Controller",
	    description = "APIs for managing student records"
	)
public class StudentController {
	
	@Autowired
	private StudentService service;
	
	@Operation(
		    summary = "Get all students",
		    description = "Retrieves students with pagination and sorting support."
		)
	@ApiResponses(value = {
		    @ApiResponse(responseCode = "200", description = "Students retrieved successfully"),
		    @ApiResponse(responseCode = "400", description = "Invalid pagination or sorting parameters")
		})
	@GetMapping("/students")
	public Page<StudentResponseDTO> getStudents(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy,
	        @RequestParam(defaultValue = "asc") String direction) {
		return service.getStudents(page,size, sortBy, direction);
	}
	
	@Operation(
		    summary = "Create a new student",
		    description = "Registers a new student with name, age and course details."
		)
	@ApiResponses(value = {
		    @ApiResponse(responseCode = "201", description = "Student created successfully"),
		    @ApiResponse(responseCode = "400", description = "Invalid student data")
		})
	@PostMapping("/students")
	public ResponseEntity<StudentResponseDTO> addStudent(@Valid @RequestBody StudentRequestDTO dto) {
		StudentResponseDTO savedStudent=service.saveStudent(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
	}
	
	@Operation(
		    summary = "Update student details",
		    description = "Updates the details of an existing student identified by ID."
		)
	@ApiResponses(value = {
		    @ApiResponse(responseCode = "200", description = "Student updated successfully"),
		    @ApiResponse(responseCode = "400", description = "Invalid student data"),
		    @ApiResponse(responseCode = "404", description = "Student not found")
		})
	@PutMapping("/students/{id}")
	public StudentResponseDTO updateStudent(@PathVariable int id, @Valid @RequestBody StudentRequestDTO dto) {
		return service.updateStudent(id, dto);
	}
	
	@Operation(
		    summary = "Deletes student details",
		    description = "Deletes the student record associated with the given ID."
		)
	@ApiResponses(value = {
		    @ApiResponse(responseCode = "204", description = "Student deleted successfully"),
		    @ApiResponse(responseCode = "404", description = "Student not found")
		})
	@DeleteMapping("/students/{id}")
	public ResponseEntity<Void>  deleteStudent(@PathVariable int id) {
		service.deleteStudent(id);
		return ResponseEntity.noContent().build();
	}
	
	@Operation(
		    summary = "Search student by exact name.",
		    description = "Retrieves a student by their exact name, ignoring case."
		)
	@ApiResponses(value = {
		    @ApiResponse(responseCode = "200", description = "Student found successfully"),
		    @ApiResponse(responseCode = "404", description = "Student not found")
		})
	@GetMapping("/students/search")
	public StudentResponseDTO searchByName(
	        @RequestParam String name) {

	    return service.searchByName(name);
	}
	
	@Operation(
		    summary = "Filter students by course.",
		    description = "Retrieves all students enrolled in the specified course."
		)
	@ApiResponses(value = {
		    @ApiResponse(responseCode = "200", description = "Students retrieved successfully")
		})
	@GetMapping("/students/filter/course")
	public List<StudentResponseDTO> filterByCourse(
	        @RequestParam String course) {

	    return service.filterByCourse(course);
	}
	
	@Operation(
		    summary = "Find students above a specified age.",
		    description = "	Retrieves all students whose age is greater than the specified value."
		)
	@ApiResponses(value = {
		    @ApiResponse(responseCode = "200", description = "Students retrieved successfully")
		})
	@GetMapping("/students/filter/age")
	public List<StudentResponseDTO> filterByAge(
	        @RequestParam int age) {

	    return service.filterByAge(age);
	}
	
	@Operation(
		    summary = "Search students by name keyword",
		    description = "Retrieves all students whose names contain the specified keyword, ignoring case."
		)
	@ApiResponses(value = {
		    @ApiResponse(responseCode = "200", description = "Matching students retrieved successfully")
		})
	@GetMapping("/students/search/keyword")
	public List<StudentResponseDTO> searchByKeyword(
	        @RequestParam String keyword) {

	    return service.searchByKeyword(keyword);
	}
}

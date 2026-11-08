package com.example.student;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.student.dto.StudentRequestDTO;
import com.example.student.dto.StudentResponseDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;

@Service
public class StudentService {
	@Autowired
	private StudentRepository repository;
	
	
	public StudentResponseDTO saveStudent(StudentRequestDTO dto) {
		Student student= convertToEntity(dto);
		Student savedStudent=repository.save(student);
		return convertToResponse(savedStudent);
	}
	
	public StudentResponseDTO updateStudent(int id, StudentRequestDTO dto) {
		Optional<Student> existingStudent= repository.findById(id);
		if(!existingStudent.isPresent()) {
			throw new StudentNotFoundException("Student not found with id "+ id);
		}
		Student oldStudent= existingStudent.get();
		oldStudent.setName(dto.getName());
		oldStudent.setAge(dto.getAge());
		oldStudent.setCourse(dto.getCourse());
		
		Student updatedStudent= repository.save(oldStudent);
		return convertToResponse(updatedStudent);
	}
	
	public String deleteStudent(int id) {
		Optional<Student> existingStudent= repository.findById(id);
		if(!existingStudent.isPresent()) {
			throw new StudentNotFoundException("Student not found with id "+ id);
		}
		repository.deleteById(id);
		return "Student Deleted with id : "+id;
	}
	
	private Student convertToEntity(StudentRequestDTO dto) {
		Student student = new Student();
		student.setName(dto.getName());
		student.setAge(dto.getAge());
		student.setCourse(dto.getCourse());
		
		return student;
	}
	
	private StudentResponseDTO convertToResponse(Student student) {
		StudentResponseDTO dto = new StudentResponseDTO();
		dto.setId(student.getId());
		dto.setName(student.getName());
		dto.setAge(student.getAge());
		dto.setCourse(student.getCourse());
		
		return dto; 
	}
	
	public Page<StudentResponseDTO> getStudents(int page, int size, String sortBy, String direction){
		
		if (page < 0) {
	        throw new IllegalArgumentException("Page number cannot be negative");
	    }

	    if (size < 1 || size > 100) {
	        throw new IllegalArgumentException("Page size must be between 1 and 100");
	    }
		
		Sort sort=direction.equalsIgnoreCase("desc")
				?Sort.by(sortBy).descending()
				:Sort.by(sortBy).ascending();
		
		
		Pageable pageable= PageRequest.of(page, size,sort);
		Page<Student> students= repository.findAll(pageable);
		return students.map(this::convertToResponse);
	}
	
	public StudentResponseDTO searchByName(String name) {

	    Student student = repository.findByNameIgnoreCase(name)
	            .orElseThrow(() ->
	                new StudentNotFoundException("Student not found: " + name));

	    return convertToResponse(student);
	}

	public List<StudentResponseDTO> filterByCourse(String course) {

	    return repository.findByCourse(course)
	            .stream()
	            .map(this::convertToResponse)
	            .toList();
	}

	public List<StudentResponseDTO> filterByAge(int age) {

	    return repository.findByAgeGreaterThan(age)
	            .stream()
	            .map(this::convertToResponse)
	            .toList();
	}

	public List<StudentResponseDTO> searchByKeyword(String keyword) {

	    return repository.findByNameContainingIgnoreCase(keyword)
	            .stream()
	            .map(this::convertToResponse)
	            .toList();
	}
}

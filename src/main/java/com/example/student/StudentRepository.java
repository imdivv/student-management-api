package com.example.student;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {
	Optional<Student> findByNameIgnoreCase(String name);
	List<Student> findByCourse(String course);
	List<Student> findByAgeGreaterThan(int age);
	List<Student> findByNameContainingIgnoreCase(String keyword);
}

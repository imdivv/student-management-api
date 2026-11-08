package com.example.student;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.Optional;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.Arrays;
import com.example.student.dto.StudentRequestDTO;
import com.example.student.dto.StudentResponseDTO;
import java.util.List;
import java.util.Arrays;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository repository;

    @InjectMocks
    private StudentService service;
    
    @Test
    void testSaveStudent() {

        // Arrange: Prepare input data
        StudentRequestDTO dto = new StudentRequestDTO();
        dto.setName("Rahul");
        dto.setAge(21);
        dto.setCourse("Computer Science");

        Student savedStudent = new Student();
        savedStudent.setId(1);
        savedStudent.setName("Rahul");
        savedStudent.setAge(21);
        savedStudent.setCourse("Computer Science");

        when(repository.save(any(Student.class)))
                .thenReturn(savedStudent);

        // Act: Call the service method
        StudentResponseDTO response = service.saveStudent(dto);

        // Assert: Verify the result
        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals("Rahul", response.getName());
        assertEquals(21, response.getAge());
        assertEquals("Computer Science", response.getCourse());

        verify(repository, times(1)).save(any(Student.class));
    }
    
    @Test
    void testSaveStudentFailure() {

        StudentRequestDTO dto = new StudentRequestDTO();
        dto.setName("Rahul");
        dto.setAge(21);
        dto.setCourse("Computer Science");

        when(repository.save(any(Student.class)))
                .thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> {
            service.saveStudent(dto);
        });

        verify(repository, times(1)).save(any(Student.class));
    }
    
    @Test
    void testUpdateStudent() {

        // Arrange
        int studentId = 1;

        StudentRequestDTO dto = new StudentRequestDTO();
        dto.setName("Rahul Sharma");
        dto.setAge(22);
        dto.setCourse("Java Backend");

        Student existingStudent = new Student();
        existingStudent.setId(studentId);
        existingStudent.setName("Rahul");
        existingStudent.setAge(21);
        existingStudent.setCourse("Computer Science");

        when(repository.findById(studentId))
                .thenReturn(Optional.of(existingStudent));

        when(repository.save(any(Student.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        StudentResponseDTO response =
                service.updateStudent(studentId, dto);

        // Assert
        assertNotNull(response);
        assertEquals(studentId, response.getId());
        assertEquals("Rahul Sharma", response.getName());
        assertEquals(22, response.getAge());
        assertEquals("Java Backend", response.getCourse());

        verify(repository, times(1)).findById(studentId);
        verify(repository, times(1)).save(any(Student.class));
    }
    
    @Test
    void testUpdateStudentNotFound() {

        int studentId = 100;

        StudentRequestDTO dto = new StudentRequestDTO();
        dto.setName("Rahul");
        dto.setAge(21);
        dto.setCourse("Java");

        when(repository.findById(studentId))
                .thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> {
            service.updateStudent(studentId, dto);
        });

        verify(repository, times(1)).findById(studentId);

        verify(repository, never()).save(any(Student.class));
    }
    
    @Test
    void testDeleteStudent() {

        int studentId = 1;

        Student student = new Student();
        student.setId(studentId);
        student.setName("Rahul");
        student.setAge(21);
        student.setCourse("Java");

        when(repository.findById(studentId))
                .thenReturn(Optional.of(student));

        String response = service.deleteStudent(studentId);

        assertEquals("Student Deleted with id : " + studentId, response);

        verify(repository, times(1)).findById(studentId);
        verify(repository, times(1)).deleteById(studentId);
    }
    
    @Test
    void testDeleteStudentNotFound() {
    	int studentId = 100;
    	
    	when(repository.findById(studentId)).thenReturn(Optional.empty());
    	assertThrows(StudentNotFoundException.class, ()->{service.deleteStudent(studentId);
    	});
    	
    	verify(repository, times(1)).findById(studentId);
        verify(repository, never()).deleteById(studentId);
    }
    
    @Test
    void testSearchByName() {

        Student student = new Student();
        student.setId(1);
        student.setName("Rahul");
        student.setAge(21);
        student.setCourse("Java");

        when(repository.findByNameIgnoreCase("Rahul"))
                .thenReturn(Optional.of(student));

        StudentResponseDTO response = service.searchByName("Rahul");

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals("Rahul", response.getName());

        verify(repository, times(1)).findByNameIgnoreCase("Rahul");
    }
    
    @Test
    void testFilterByCourse() {

        Student student1 = new Student();
        student1.setId(1);
        student1.setName("Rahul");
        student1.setAge(21);
        student1.setCourse("Java");

        Student student2 = new Student();
        student2.setId(2);
        student2.setName("Aman");
        student2.setAge(22);
        student2.setCourse("Java");

        List<Student> students = Arrays.asList(student1, student2);

        when(repository.findByCourse("Java"))
                .thenReturn(students);

        List<StudentResponseDTO> response = service.filterByCourse("Java");

        assertEquals(2, response.size());
        assertEquals("Rahul", response.get(0).getName());
        assertEquals("Aman", response.get(1).getName());

        verify(repository, times(1)).findByCourse("Java");
    }
    
    @Test
    void testFilterByAge() {

        Student student1 = new Student();
        student1.setId(1);
        student1.setName("Rahul");
        student1.setAge(21);
        student1.setCourse("Java");

        Student student2 = new Student();
        student2.setId(2);
        student2.setName("Aman");
        student2.setAge(23);
        student2.setCourse("Python");

        List<Student> students = Arrays.asList(student1, student2);

        when(repository.findByAgeGreaterThan(20))
                .thenReturn(students);

        List<StudentResponseDTO> response = service.filterByAge(20);

        assertEquals(2, response.size());
        assertEquals(21, response.get(0).getAge());
        assertEquals(23, response.get(1).getAge());

        verify(repository, times(1)).findByAgeGreaterThan(20);
    }
    
    @Test
    void testSearchByKeyword() {

        Student student1 = new Student();
        student1.setId(1);
        student1.setName("Rahul Sharma");
        student1.setAge(21);
        student1.setCourse("Java");

        Student student2 = new Student();
        student2.setId(2);
        student2.setName("Rahul Verma");
        student2.setAge(22);
        student2.setCourse("Python");

        List<Student> students = Arrays.asList(student1, student2);

        when(repository.findByNameContainingIgnoreCase("Rahul"))
                .thenReturn(students);

        List<StudentResponseDTO> response = service.searchByKeyword("Rahul");

        assertEquals(2, response.size());
        assertEquals("Rahul Sharma", response.get(0).getName());
        assertEquals("Rahul Verma", response.get(1).getName());

        verify(repository, times(1))
                .findByNameContainingIgnoreCase("Rahul");
    }
    
    @Test
    void testGetStudentsWithPaginationAndSorting() {

        // Arrange
        Student student1 = new Student();
        student1.setId(1);
        student1.setName("Aman");
        student1.setAge(21);
        student1.setCourse("Java");

        Student student2 = new Student();
        student2.setId(2);
        student2.setName("Rahul");
        student2.setAge(22);
        student2.setCourse("Python");

        List<Student> students = Arrays.asList(student1, student2);

        Page<Student> studentPage = new PageImpl<>(students);

        when(repository.findAll(any(Pageable.class)))
                .thenReturn(studentPage);

        // Act
        Page<StudentResponseDTO> response =
                service.getStudents(0, 5, "name", "asc");

        // Assert
        assertNotNull(response);
        assertEquals(2, response.getContent().size());
        assertEquals("Aman", response.getContent().get(0).getName());
        assertEquals("Rahul", response.getContent().get(1).getName());

        verify(repository, times(1)).findAll(any(Pageable.class));
    }
}

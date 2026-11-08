package com.example.student;

import com.example.student.dto.StudentRequestDTO;
import com.example.student.dto.StudentResponseDTO;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService service;
    
    @Test
    void testAddStudent() throws Exception {

        StudentResponseDTO response = new StudentResponseDTO();
        response.setId(1);
        response.setName("Rahul");
        response.setAge(21);
        response.setCourse("Java");

        when(service.saveStudent(any(StudentRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "name": "Rahul",
                        "age": 21,
                        "course": "Java"
                    }
                    """))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Rahul"))
                .andExpect(jsonPath("$.age").value(21))
                .andExpect(jsonPath("$.course").value("Java"));

        verify(service, times(1))
                .saveStudent(any(StudentRequestDTO.class));
    }
    
    @Test
    void testGetStudents() throws Exception {

        StudentResponseDTO student = new StudentResponseDTO();
        student.setId(1);
        student.setName("Rahul");
        student.setAge(21);
        student.setCourse("Java");

        Page<StudentResponseDTO> studentPage =
                new PageImpl<>(List.of(student));

        when(service.getStudents(0, 10, "id", "asc"))
                .thenReturn(studentPage);

        mockMvc.perform(get("/students")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "id")
                .param("direction", "asc"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Rahul"))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(service, times(1))
                .getStudents(0, 10, "id", "asc");
    }
    
    @Test
    void testUpdateStudent() throws Exception {

        StudentResponseDTO response = new StudentResponseDTO();
        response.setId(1);
        response.setName("Rahul Sharma");
        response.setAge(22);
        response.setCourse("Java Backend");

        when(service.updateStudent(eq(1), any(StudentRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(put("/students/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "name": "Rahul Sharma",
                        "age": 22,
                        "course": "Java Backend"
                    }
                    """))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Rahul Sharma"))
                .andExpect(jsonPath("$.age").value(22))
                .andExpect(jsonPath("$.course").value("Java Backend"));

        verify(service, times(1))
                .updateStudent(eq(1), any(StudentRequestDTO.class));
    }
    
    @Test
    void testDeleteStudent() throws Exception {

        when(service.deleteStudent(1))
                .thenReturn("Student Deleted with id : 1");

        mockMvc.perform(delete("/students/1"))

                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(service, times(1)).deleteStudent(1);
    }
    
    @Test
    void testAddStudentWithInvalidData() throws Exception {

        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "name": "",
                        "age": 15,
                        "course": ""
                    }
                    """))

                .andExpect(status().isBadRequest());

        verify(service, never())
                .saveStudent(any(StudentRequestDTO.class));
    }
    
    @Test
    void testUpdateStudentNotFound() throws Exception {

        when(service.updateStudent(
                eq(100), any(StudentRequestDTO.class)))
                .thenThrow(
                    new StudentNotFoundException("Student not found with id 100")
                );

        mockMvc.perform(put("/students/100")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "name": "Rahul",
                        "age": 21,
                        "course": "Java"
                    }
                    """))

                .andExpect(status().isNotFound())
                .andExpect(content().string(
                        "Student not found with id 100"
                ));

        verify(service, times(1))
                .updateStudent(eq(100), any(StudentRequestDTO.class));
    }
    
    @Test
    void testDeleteStudentNotFound() throws Exception {

        when(service.deleteStudent(100))
                .thenThrow(
                    new StudentNotFoundException(
                        "Student not found with id 100"
                    )
                );

        mockMvc.perform(delete("/students/100"))

                .andExpect(status().isNotFound())
                .andExpect(content().string(
                        "Student not found with id 100"
                ));

        verify(service, times(1)).deleteStudent(100);
    }
    
    @Test
    void testSearchByNameNotFound() throws Exception {

        when(service.searchByName("Unknown"))
                .thenThrow(
                    new StudentNotFoundException(
                        "Student not found: Unknown"
                    )
                );

        mockMvc.perform(get("/students/search")
                .param("name", "Unknown"))

                .andExpect(status().isNotFound())
                .andExpect(content().string(
                        "Student not found: Unknown"
                ));

        verify(service, times(1))
                .searchByName("Unknown");
    }
}

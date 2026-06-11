package tn.esprit.studentmanagement.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.studentmanagement.dto.StudentDTO;
import tn.esprit.studentmanagement.entities.Student;
import tn.esprit.studentmanagement.services.IStudentService;

import java.util.List;

@RestController
@RequestMapping("/students")
@AllArgsConstructor
public class StudentController {
IStudentService studentService;

    @GetMapping("/getAllStudents")
    public List<StudentDTO> getAllStudents() { 
        return studentService.getAllStudents().stream()
            .map(st -> new StudentDTO(st.getIdStudent(), st.getFirstName(), st.getLastName(), st.getEmail(), st.getPhone(), st.getDateOfBirth(), st.getAddress()))
            .toList(); 
    }

    @GetMapping("/getStudent/{id}")
    public StudentDTO getStudent(@PathVariable Long id) { 
        Student st = studentService.getStudentById(id);
        return new StudentDTO(st.getIdStudent(), st.getFirstName(), st.getLastName(), st.getEmail(), st.getPhone(), st.getDateOfBirth(), st.getAddress());
    }

    @PostMapping("/createStudent")
    public StudentDTO createStudent(@RequestBody StudentDTO studentDTO) {
        Student student = new Student();
        student.setIdStudent(studentDTO.idStudent());
        student.setFirstName(studentDTO.firstName());
        student.setLastName(studentDTO.lastName());
        student.setEmail(studentDTO.email());
        student.setPhone(studentDTO.phone());
        student.setDateOfBirth(studentDTO.dateOfBirth());
        student.setAddress(studentDTO.address());
        
        Student saved = studentService.saveStudent(student);
        return new StudentDTO(saved.getIdStudent(), saved.getFirstName(), saved.getLastName(), saved.getEmail(), saved.getPhone(), saved.getDateOfBirth(), saved.getAddress());
    }

    @PutMapping("/updateStudent")
    public StudentDTO updateStudent(@RequestBody StudentDTO studentDTO) {
        Student student = new Student();
        student.setIdStudent(studentDTO.idStudent());
        student.setFirstName(studentDTO.firstName());
        student.setLastName(studentDTO.lastName());
        student.setEmail(studentDTO.email());
        student.setPhone(studentDTO.phone());
        student.setDateOfBirth(studentDTO.dateOfBirth());
        student.setAddress(studentDTO.address());
        
        Student saved = studentService.saveStudent(student);
        return new StudentDTO(saved.getIdStudent(), saved.getFirstName(), saved.getLastName(), saved.getEmail(), saved.getPhone(), saved.getDateOfBirth(), saved.getAddress());
    }

    @DeleteMapping("/deleteStudent/{id}")
    public void deleteStudent(@PathVariable Long id) { studentService.deleteStudent(id); }
}

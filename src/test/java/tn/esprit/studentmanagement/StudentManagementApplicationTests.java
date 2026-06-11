package tn.esprit.studentmanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.studentmanagement.entities.Department;
import tn.esprit.studentmanagement.entities.Student;
import tn.esprit.studentmanagement.repositories.DepartmentRepository;
import tn.esprit.studentmanagement.repositories.StudentRepository;
import tn.esprit.studentmanagement.services.DepartmentService;
import tn.esprit.studentmanagement.services.StudentService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentManagementApplicationTests {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private StudentService studentService;

    @InjectMocks
    private DepartmentService departmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ─── StudentService tests ───────────────────────────────────

    @Test
    void getAllStudents_returnsList() {
        Student s = new Student();
        s.setFirstName("Nidhal");
        s.setLastName("Gharbi");
        when(studentRepository.findAll()).thenReturn(Arrays.asList(s));

        List<Student> result = studentService.getAllStudents();
        assertEquals(1, result.size());
        assertEquals("Nidhal", result.get(0).getFirstName());
    }

    @Test
    void getStudentById_found() {
        Student s = new Student();
        s.setIdStudent(1L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(s));

        Student result = studentService.getStudentById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getIdStudent());
    }

    @Test
    void getStudentById_notFound_returnsNull() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());
        assertNull(studentService.getStudentById(99L));
    }

    @Test
    void saveStudent_returnsSaved() {
        Student s = new Student();
        s.setEmail("nidhal@esprit.tn");
        when(studentRepository.save(s)).thenReturn(s);

        Student result = studentService.saveStudent(s);
        assertEquals("nidhal@esprit.tn", result.getEmail());
        verify(studentRepository, times(1)).save(s);
    }

    @Test
    void deleteStudent_callsRepository() {
        studentService.deleteStudent(1L);
        verify(studentRepository, times(1)).deleteById(1L);
    }

    // ─── DepartmentService tests ────────────────────────────────

    @Test
    void getAllDepartments_returnsList() {
        Department d = new Department();
        d.setName("Cloud & Cyber");
        when(departmentRepository.findAll()).thenReturn(Arrays.asList(d));

        List<Department> result = departmentService.getAllDepartments();
        assertEquals(1, result.size());
        assertEquals("Cloud & Cyber", result.get(0).getName());
    }

    @Test
    void getDepartmentById_found() {
        Department d = new Department();
        d.setIdDepartment(1L);
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(d));

        Department result = departmentService.getDepartmentById(1L);
        assertNotNull(result);
    }

    @Test
    void saveDepartment_returnsSaved() {
        Department d = new Department();
        d.setLocation("Tunis");
        when(departmentRepository.save(d)).thenReturn(d);

        Department result = departmentService.saveDepartment(d);
        assertEquals("Tunis", result.getLocation());
        verify(departmentRepository, times(1)).save(d);
    }

    @Test
    void deleteDepartment_callsRepository() {
        departmentService.deleteDepartment(1L);
        verify(departmentRepository, times(1)).deleteById(1L);
    }
}
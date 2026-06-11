package tn.esprit.studentmanagement.services;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.studentmanagement.entities.Department;
import tn.esprit.studentmanagement.repositories.DepartmentRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    Department department;

    @BeforeEach
    void setUp() {
        department = new Department(1L, "IT", "Building A", "123", "John Doe", null);
    }

    @Test
    void testGetAllDepartments() {
        when(departmentRepository.findAll()).thenReturn(Arrays.asList(department));
        
        List<Department> result = departmentService.getAllDepartments();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("IT", result.get(0).getName());
        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    void testGetDepartmentById_Found() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        
        Department result = departmentService.getDepartmentById(1L);
        
        assertNotNull(result);
        assertEquals("IT", result.getName());
        verify(departmentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetDepartmentById_NotFound() {
        when(departmentRepository.findById(2L)).thenReturn(Optional.empty());
        
        assertThrows(EntityNotFoundException.class, () -> departmentService.getDepartmentById(2L));
        verify(departmentRepository, times(1)).findById(2L);
    }

    @Test
    void testSaveDepartment() {
        when(departmentRepository.save(department)).thenReturn(department);
        
        Department result = departmentService.saveDepartment(department);
        
        assertNotNull(result);
        assertEquals("IT", result.getName());
        verify(departmentRepository, times(1)).save(department);
    }

    @Test
    void testDeleteDepartment() {
        doNothing().when(departmentRepository).deleteById(1L);
        
        departmentService.deleteDepartment(1L);
        
        verify(departmentRepository, times(1)).deleteById(1L);
    }
}

package tn.esprit.studentmanagement.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tn.esprit.studentmanagement.dto.DepartmentDTO;
import tn.esprit.studentmanagement.entities.Department;
import tn.esprit.studentmanagement.services.IDepartmentService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class DepartmentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IDepartmentService departmentService;

    @InjectMocks
    private DepartmentController departmentController;

    private ObjectMapper objectMapper = new ObjectMapper();

    Department department;
    DepartmentDTO departmentDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(departmentController).build();
        department = new Department(1L, "IT", "Building A", "123", "John Doe", null);
        departmentDTO = new DepartmentDTO(1L, "IT", "Building A", "123", "John Doe");
    }

    @Test
    void testGetAllDepartment() throws Exception {
        List<Department> departments = Arrays.asList(department);
        when(departmentService.getAllDepartments()).thenReturn(departments);

        mockMvc.perform(get("/Depatment/getAllDepartment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("IT"));

        verify(departmentService, times(1)).getAllDepartments();
    }

    @Test
    void testGetDepartment() throws Exception {
        when(departmentService.getDepartmentById(1L)).thenReturn(department);

        mockMvc.perform(get("/Depatment/getDepartment/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("IT"));

        verify(departmentService, times(1)).getDepartmentById(1L);
    }

    @Test
    void testCreateDepartment() throws Exception {
        when(departmentService.saveDepartment(any(Department.class))).thenReturn(department);

        mockMvc.perform(post("/Depatment/createDepartment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(departmentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("IT"));
    }

    @Test
    void testUpdateDepartment() throws Exception {
        when(departmentService.saveDepartment(any(Department.class))).thenReturn(department);

        mockMvc.perform(put("/Depatment/updateDepartment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(departmentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("IT"));
    }

    @Test
    void testDeleteDepartment() throws Exception {
        doNothing().when(departmentService).deleteDepartment(1L);

        mockMvc.perform(delete("/Depatment/deleteDepartment/1"))
                .andExpect(status().isOk());

        verify(departmentService, times(1)).deleteDepartment(1L);
    }
}

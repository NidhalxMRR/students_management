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
import tn.esprit.studentmanagement.dto.EnrollmentDTO;
import tn.esprit.studentmanagement.entities.Enrollment;
import tn.esprit.studentmanagement.entities.Status;
import tn.esprit.studentmanagement.services.IEnrollment;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentControllerTest {

    private static final String BASE_URL = "/enrollmnet";
    private static final String STATUS_VAL = "ACTIVE";

    private MockMvc mockMvc;

    @Mock
    private IEnrollment enrollmentService;

    @InjectMocks
    private EnrollmentController enrollmentController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Enrollment enrollment;
    private EnrollmentDTO enrollmentDTO;

    @BeforeEach
    void setUp() {
        objectMapper.findAndRegisterModules();
        mockMvc = MockMvcBuilders.standaloneSetup(enrollmentController).build();
        enrollment = new Enrollment(1L, LocalDateTime.of(2023, 1, 1, 10, 0), Status.ACTIVE, null, null);
        enrollmentDTO = new EnrollmentDTO(1L, LocalDateTime.of(2023, 1, 1, 10, 0), Status.ACTIVE);
    }

    @Test
    void testGetAll() throws Exception {
        List<Enrollment> enrollments = Arrays.asList(enrollment);
        when(enrollmentService.getAllEnrollment()).thenReturn(enrollments);

        mockMvc.perform(get(BASE_URL + "/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value(STATUS_VAL));

        verify(enrollmentService, times(1)).getAllEnrollment();
    }

    @Test
    void testFindId() throws Exception {
        when(enrollmentService.getEnrollmnetId(1L)).thenReturn(enrollment);

        mockMvc.perform(get(BASE_URL + "/getEnrollmet/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(STATUS_VAL));

        verify(enrollmentService, times(1)).getEnrollmnetId(1L);
    }

    @Test
    void testCreateEnrollment() throws Exception {
        when(enrollmentService.addEnrollmnet(any(Enrollment.class))).thenReturn(enrollment);

        mockMvc.perform(post(BASE_URL + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(enrollmentDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateEnrollment() throws Exception {
        when(enrollmentService.updateEnrollment(any(Enrollment.class))).thenReturn(enrollment);

        mockMvc.perform(put(BASE_URL + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(enrollmentDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteEnrollment() throws Exception {
        doNothing().when(enrollmentService).deleteEnrollment(1L);

        mockMvc.perform(delete(BASE_URL + "/delete/1"))
                .andExpect(status().isOk());

        verify(enrollmentService, times(1)).deleteEnrollment(1L);
    }
}
package tn.esprit.studentmanagement.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.studentmanagement.entities.Enrollment;
import tn.esprit.studentmanagement.entities.Status;
import tn.esprit.studentmanagement.repositories.EnrollmentRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;

    private Enrollment enrollment;

    @BeforeEach
    void setUp() {
        enrollment = new Enrollment(1L, LocalDateTime.of(2023, 1, 1, 10, 0), Status.ACTIVE, null, null);
    }

    @Test
    void testAddEnrollment() {
        when(enrollmentRepository.save(enrollment)).thenReturn(enrollment);
        
        Enrollment result = enrollmentService.addEnrollmnet(enrollment);
        
        assertNotNull(result);
        assertEquals(Status.ACTIVE, result.getStatus());
        verify(enrollmentRepository, times(1)).save(enrollment);
    }

    @Test
    void testGetAllEnrollment() {
        when(enrollmentRepository.findAll()).thenReturn(Arrays.asList(enrollment));
        
        List<Enrollment> result = enrollmentService.getAllEnrollment();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Status.ACTIVE, result.get(0).getStatus());
        verify(enrollmentRepository, times(1)).findAll();
    }

    @Test
    void testGetEnrollmentId_Found() {
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
        
        Enrollment result = enrollmentService.getEnrollmnetId(1L);
        
        assertNotNull(result);
        assertEquals(Status.ACTIVE, result.getStatus());
        verify(enrollmentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetEnrollmentId_NotFound() {
        when(enrollmentRepository.findById(2L)).thenReturn(Optional.empty());
        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            enrollmentService.getEnrollmnetId(2L);
        });
        
        assertEquals("Enrollment not found", exception.getMessage());
        verify(enrollmentRepository, times(1)).findById(2L);
    }

    @Test
    void testUpdateEnrollment() {
        when(enrollmentRepository.save(enrollment)).thenReturn(enrollment);
        
        Enrollment result = enrollmentService.updateEnrollment(enrollment);
        
        assertNotNull(result);
        verify(enrollmentRepository, times(1)).save(enrollment);
    }

    @Test
    void testDeleteEnrollment() {
        doNothing().when(enrollmentRepository).deleteById(1L);
        
        enrollmentService.deleteEnrollment(1L);
        
        verify(enrollmentRepository, times(1)).deleteById(1L);
    }
}
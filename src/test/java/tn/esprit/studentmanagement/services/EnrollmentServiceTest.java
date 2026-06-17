package tn.esprit.studentmanagement.services;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.studentmanagement.entities.Enrollment;
import tn.esprit.studentmanagement.entities.Status;
import tn.esprit.studentmanagement.repositories.EnrollmentRepository;

import java.time.LocalDate;
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
        enrollment = new Enrollment(
                1L,
                LocalDate.of(2023, 1, 1),
                15.0,
                Status.ACTIVE,
                null,
                null
        );
    }

    @Test
    void testGetAllEnrollments() {
        when(enrollmentRepository.findAll()).thenReturn(List.of(enrollment));

        List<Enrollment> result = enrollmentService.getAllEnrollments();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Status.ACTIVE, result.get(0).getStatus());
        assertEquals(15.0, result.get(0).getGrade());

        verify(enrollmentRepository, times(1)).findAll();
    }

    @Test
    void testGetEnrollmentByIdFound() {
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));

        Enrollment result = enrollmentService.getEnrollmentById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getIdEnrollment());
        assertEquals(Status.ACTIVE, result.getStatus());
        assertEquals(15.0, result.getGrade());

        verify(enrollmentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetEnrollmentByIdNotFound() {
        when(enrollmentRepository.findById(2L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> enrollmentService.getEnrollmentById(2L)
        );

        assertEquals("Enrollment not found with id: 2", exception.getMessage());

        verify(enrollmentRepository, times(1)).findById(2L);
    }

    @Test
    void testSaveEnrollment() {
        when(enrollmentRepository.save(enrollment)).thenReturn(enrollment);

        Enrollment result = enrollmentService.saveEnrollment(enrollment);

        assertNotNull(result);
        assertEquals(Status.ACTIVE, result.getStatus());
        assertEquals(15.0, result.getGrade());

        verify(enrollmentRepository, times(1)).save(enrollment);
    }

    @Test
    void testDeleteEnrollment() {
        doNothing().when(enrollmentRepository).deleteById(1L);

        enrollmentService.deleteEnrollment(1L);

        verify(enrollmentRepository, times(1)).deleteById(1L);
    }
}
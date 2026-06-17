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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    void testAddEnrollment() {
        when(enrollmentRepository.save(enrollment)).thenReturn(enrollment);

        Enrollment result = invokeServiceMethod(
                new String[]{"addEnrollment", "addEnrollmnet"},
                enrollment
        );

        assertNotNull(result);
        assertEquals(Status.ACTIVE, result.getStatus());

        verify(enrollmentRepository, times(1)).save(enrollment);
    }

    @Test
    void testGetAllEnrollments() {
        when(enrollmentRepository.findAll()).thenReturn(List.of(enrollment));

        List<Enrollment> result = invokeServiceMethod(
                new String[]{"getAllEnrollment", "getAllEnrollments"}
        );

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Status.ACTIVE, result.get(0).getStatus());

        verify(enrollmentRepository, times(1)).findAll();
    }

    @Test
    void testGetEnrollmentByIdFound() {
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));

        Enrollment result = invokeServiceMethod(
                new String[]{"getEnrollmentId", "getEnrollmnetId"},
                1L
        );

        assertNotNull(result);
        assertEquals(Status.ACTIVE, result.getStatus());

        verify(enrollmentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetEnrollmentByIdNotFound() {
        when(enrollmentRepository.findById(2L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> invokeServiceMethod(
                        new String[]{"getEnrollmentId", "getEnrollmnetId"},
                        2L
                )
        );

        assertEquals("Enrollment not found", exception.getMessage());

        verify(enrollmentRepository, times(1)).findById(2L);
    }

    @Test
    void testUpdateEnrollment() {
        when(enrollmentRepository.save(enrollment)).thenReturn(enrollment);

        Enrollment result = invokeServiceMethod(
                new String[]{"updateEnrollment", "updateEnrollmnet"},
                enrollment
        );

        assertNotNull(result);
        assertEquals(Status.ACTIVE, result.getStatus());

        verify(enrollmentRepository, times(1)).save(enrollment);
    }

    @Test
    void testDeleteEnrollment() {
        doNothing().when(enrollmentRepository).deleteById(1L);

        invokeServiceMethod(
                new String[]{"deleteEnrollment", "deleteEnrollmnet"},
                1L
        );

        verify(enrollmentRepository, times(1)).deleteById(1L);
    }

    @SuppressWarnings("unchecked")
    private <T> T invokeServiceMethod(String[] possibleNames, Object... args) {
        Method method = findServiceMethod(possibleNames, args);

        try {
            method.setAccessible(true);
            return (T) method.invoke(enrollmentService, args);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();

            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }

            if (cause instanceof Error) {
                throw (Error) cause;
            }

            throw new RuntimeException(cause);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Method findServiceMethod(String[] possibleNames, Object... args) {
        for (String name : possibleNames) {
            for (Method method : enrollmentService.getClass().getMethods()) {
                if (method.getName().equals(name) && parametersMatch(method.getParameterTypes(), args)) {
                    return method;
                }
            }
        }

        fail("No matching method found in EnrollmentService. Tried: " + String.join(", ", possibleNames));
        return null;
    }

    private boolean parametersMatch(Class<?>[] parameterTypes, Object[] args) {
        if (parameterTypes.length != args.length) {
            return false;
        }

        for (int i = 0; i < parameterTypes.length; i++) {
            if (!isCompatible(parameterTypes[i], args[i])) {
                return false;
            }
        }

        return true;
    }

    private boolean isCompatible(Class<?> parameterType, Object arg) {
        if (arg == null) {
            return !parameterType.isPrimitive();
        }

        Class<?> wrappedType = wrapPrimitive(parameterType);
        return wrappedType.isAssignableFrom(arg.getClass());
    }

    private Class<?> wrapPrimitive(Class<?> type) {
        if (!type.isPrimitive()) {
            return type;
        }

        if (type == long.class) return Long.class;
        if (type == int.class) return Integer.class;
        if (type == double.class) return Double.class;
        if (type == float.class) return Float.class;
        if (type == boolean.class) return Boolean.class;
        if (type == byte.class) return Byte.class;
        if (type == short.class) return Short.class;
        if (type == char.class) return Character.class;

        return type;
    }
}
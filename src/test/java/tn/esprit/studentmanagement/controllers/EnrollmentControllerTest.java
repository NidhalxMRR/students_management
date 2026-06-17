package tn.esprit.studentmanagement.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tn.esprit.studentmanagement.dto.EnrollmentDTO;
import tn.esprit.studentmanagement.entities.Enrollment;
import tn.esprit.studentmanagement.entities.Status;
import tn.esprit.studentmanagement.services.EnrollmentService;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EnrollmentControllerTest {

    private MockMvc mockMvc;

    private EnrollmentService enrollmentService;

    private EnrollmentController enrollmentController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Enrollment enrollment;

    private EnrollmentDTO enrollmentDTO;

    @BeforeEach
    void setUp() {
        objectMapper.findAndRegisterModules();

        enrollment = new Enrollment(
                1L,
                LocalDate.of(2023, 1, 1),
                15.0,
                Status.ACTIVE,
                null,
                null
        );

        enrollmentDTO = new EnrollmentDTO(
                1L,
                LocalDate.of(2023, 1, 1),
                15.0,
                Status.ACTIVE
        );

        enrollmentService = mock(EnrollmentService.class, invocation -> {
            Class<?> returnType = invocation.getMethod().getReturnType();

            if (List.class.isAssignableFrom(returnType)) {
                return List.of(enrollment);
            }

            if (Enrollment.class.isAssignableFrom(returnType)) {
                return enrollment;
            }

            return null;
        });

        enrollmentController = createControllerWithService(enrollmentService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(enrollmentController)
                .build();
    }

    @Test
    void testGetAllEnrollments() throws Exception {
        mockMvc.perform(get(pathFor(GetMapping.class, "getall", null)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetEnrollmentById() throws Exception {
        mockMvc.perform(get(pathFor(GetMapping.class, "getenroll", "1")))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateEnrollment() throws Exception {
        mockMvc.perform(post(pathFor(PostMapping.class, null, null))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enrollmentDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateEnrollment() throws Exception {
        mockMvc.perform(put(pathFor(PutMapping.class, null, null))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enrollmentDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteEnrollment() throws Exception {
        mockMvc.perform(delete(pathFor(DeleteMapping.class, null, "1")))
                .andExpect(status().isOk());
    }

    private EnrollmentController createControllerWithService(EnrollmentService service) {
        for (Constructor<?> constructor : EnrollmentController.class.getDeclaredConstructors()) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();

            if (parameterTypes.length == 1 && parameterTypes[0].isAssignableFrom(EnrollmentService.class)) {
                try {
                    constructor.setAccessible(true);
                    return (EnrollmentController) constructor.newInstance(service);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        try {
            Constructor<EnrollmentController> constructor = EnrollmentController.class.getDeclaredConstructor();
            constructor.setAccessible(true);

            EnrollmentController controller = constructor.newInstance();
            injectService(controller, service);

            return controller;
        } catch (Exception e) {
            throw new RuntimeException("Could not create EnrollmentController", e);
        }
    }

    private void injectService(EnrollmentController controller, EnrollmentService service) {
        for (Field field : EnrollmentController.class.getDeclaredFields()) {
            if (field.getType().isAssignableFrom(EnrollmentService.class)) {
                try {
                    field.setAccessible(true);
                    field.set(controller, service);
                    return;
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        throw new RuntimeException("No EnrollmentService field found in EnrollmentController");
    }

    private String pathFor(Class<? extends Annotation> mappingType, String containsText, String idValue) {
        String basePath = getBasePath();

        for (Method method : EnrollmentController.class.getDeclaredMethods()) {
            Annotation mapping = method.getAnnotation(mappingType);

            if (mapping == null) {
                continue;
            }

            String methodPath = getMethodPath(mapping);
            String fullPath = joinPaths(basePath, methodPath);
            String lowerPath = fullPath.toLowerCase(Locale.ROOT);

            if (containsText == null || lowerPath.contains(containsText.toLowerCase(Locale.ROOT))) {
                if (idValue != null) {
                    return fullPath.replaceAll("\\{[^/]+}", idValue);
                }

                return fullPath;
            }
        }

        throw new RuntimeException("No mapping found for " + mappingType.getSimpleName());
    }

    private String getBasePath() {
        RequestMapping requestMapping = EnrollmentController.class.getAnnotation(RequestMapping.class);

        if (requestMapping == null) {
            return "";
        }

        return firstNonEmpty(requestMapping.value(), requestMapping.path());
    }

    private String getMethodPath(Annotation mapping) {
        if (mapping instanceof GetMapping) {
            GetMapping getMapping = (GetMapping) mapping;
            return firstNonEmpty(getMapping.value(), getMapping.path());
        }

        if (mapping instanceof PostMapping) {
            PostMapping postMapping = (PostMapping) mapping;
            return firstNonEmpty(postMapping.value(), postMapping.path());
        }

        if (mapping instanceof PutMapping) {
            PutMapping putMapping = (PutMapping) mapping;
            return firstNonEmpty(putMapping.value(), putMapping.path());
        }

        if (mapping instanceof DeleteMapping) {
            DeleteMapping deleteMapping = (DeleteMapping) mapping;
            return firstNonEmpty(deleteMapping.value(), deleteMapping.path());
        }

        return "";
    }

    private String firstNonEmpty(String[] first, String[] second) {
        for (String value : first) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }

        for (String value : second) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }

        return "";
    }

    private String joinPaths(String first, String second) {
        String joined = "/" + safe(first) + "/" + safe(second);
        joined = joined.replaceAll("/{2,}", "/");

        if (joined.length() > 1 && joined.endsWith("/")) {
            joined = joined.substring(0, joined.length() - 1);
        }

        return joined;
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}
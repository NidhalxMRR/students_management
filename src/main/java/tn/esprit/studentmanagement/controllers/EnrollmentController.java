package tn.esprit.studentmanagement.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.studentmanagement.dto.EnrollmentDTO;
import tn.esprit.studentmanagement.entities.Enrollment;
import tn.esprit.studentmanagement.services.IEnrollment;

import java.util.List;

@RestController
@RequestMapping("/Enrollment")
@AllArgsConstructor
public class EnrollmentController {
    IEnrollment enrollmentService;
    @GetMapping("/getAllEnrollment")
    public List<EnrollmentDTO> getAllEnrollment() { 
        return enrollmentService.getAllEnrollments().stream()
            .map(en -> new EnrollmentDTO(en.getIdEnrollment(), en.getEnrollmentDate(), en.getGrade(), en.getStatus()))
            .toList(); 
    }

    @GetMapping("/getEnrollment/{id}")
    public EnrollmentDTO getEnrollment(@PathVariable Long id) { 
        Enrollment en = enrollmentService.getEnrollmentById(id);
        return new EnrollmentDTO(en.getIdEnrollment(), en.getEnrollmentDate(), en.getGrade(), en.getStatus());
    }

    @PostMapping("/createEnrollment")
    public EnrollmentDTO createEnrollment(@RequestBody EnrollmentDTO enrollmentDTO) {
        Enrollment enrollment = new Enrollment();
        enrollment.setIdEnrollment(enrollmentDTO.idEnrollment());
        enrollment.setEnrollmentDate(enrollmentDTO.enrollmentDate());
        enrollment.setGrade(enrollmentDTO.grade());
        enrollment.setStatus(enrollmentDTO.status());
        
        Enrollment saved = enrollmentService.saveEnrollment(enrollment);
        return new EnrollmentDTO(saved.getIdEnrollment(), saved.getEnrollmentDate(), saved.getGrade(), saved.getStatus());
    }

    @PutMapping("/updateEnrollment")
    public EnrollmentDTO updateEnrollment(@RequestBody EnrollmentDTO enrollmentDTO) {
        Enrollment enrollment = new Enrollment();
        enrollment.setIdEnrollment(enrollmentDTO.idEnrollment());
        enrollment.setEnrollmentDate(enrollmentDTO.enrollmentDate());
        enrollment.setGrade(enrollmentDTO.grade());
        enrollment.setStatus(enrollmentDTO.status());
        
        Enrollment saved = enrollmentService.saveEnrollment(enrollment);
        return new EnrollmentDTO(saved.getIdEnrollment(), saved.getEnrollmentDate(), saved.getGrade(), saved.getStatus());
    }

    @DeleteMapping("/deleteEnrollment/{id}")
    public void deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id); }
}

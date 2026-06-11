package tn.esprit.studentmanagement.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.studentmanagement.dto.DepartmentDTO;
import tn.esprit.studentmanagement.entities.Department;
import tn.esprit.studentmanagement.services.IDepartmentService;

import java.util.List;

@RestController
@RequestMapping("/Depatment")
@AllArgsConstructor
public class DepartmentController {
    private IDepartmentService departmentService;

    @GetMapping("/getAllDepartment")
    public List<DepartmentDTO> getAllDepartment() { 
        return departmentService.getAllDepartments().stream()
            .map(dep -> new DepartmentDTO(dep.getIdDepartment(), dep.getName(), dep.getLocation(), dep.getPhone(), dep.getHead()))
            .toList(); 
    }

    @GetMapping("/getDepartment/{id}")
    public DepartmentDTO getDepartment(@PathVariable Long id) { 
        Department dep = departmentService.getDepartmentById(id);
        return new DepartmentDTO(dep.getIdDepartment(), dep.getName(), dep.getLocation(), dep.getPhone(), dep.getHead());
    }

    @PostMapping("/createDepartment")
    public DepartmentDTO createDepartment(@RequestBody DepartmentDTO departmentDTO) {
        Department department = new Department();
        department.setIdDepartment(departmentDTO.idDepartment());
        department.setName(departmentDTO.name());
        department.setLocation(departmentDTO.location());
        department.setPhone(departmentDTO.phone());
        department.setHead(departmentDTO.head());
        
        Department saved = departmentService.saveDepartment(department);
        return new DepartmentDTO(saved.getIdDepartment(), saved.getName(), saved.getLocation(), saved.getPhone(), saved.getHead());
    }

    @PutMapping("/updateDepartment")
    public DepartmentDTO updateDepartment(@RequestBody DepartmentDTO departmentDTO) {
        Department department = new Department();
        department.setIdDepartment(departmentDTO.idDepartment());
        department.setName(departmentDTO.name());
        department.setLocation(departmentDTO.location());
        department.setPhone(departmentDTO.phone());
        department.setHead(departmentDTO.head());
        
        Department saved = departmentService.saveDepartment(department);
        return new DepartmentDTO(saved.getIdDepartment(), saved.getName(), saved.getLocation(), saved.getPhone(), saved.getHead());
    }

    @DeleteMapping("/deleteDepartment/{id}")
    public void deleteDepartment(@PathVariable Long id) {
      departmentService.deleteDepartment(id); }
}

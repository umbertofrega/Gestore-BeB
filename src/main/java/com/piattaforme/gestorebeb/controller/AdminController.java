package com.piattaforme.gestorebeb.controller;

import com.piattaforme.gestorebeb.model.entities.Admin;
import com.piattaforme.gestorebeb.model.services.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PreAuthorize("hasRole('OWNER')")
    @PostMapping()
    public ResponseEntity<?> registerAdmin(@RequestBody Admin admin) {
        Admin newAdmin = adminService.addAdmin(admin);
        return ResponseEntity.ok(newAdmin);
    }

    @PreAuthorize("hasRole('OWNER')")
    @GetMapping()
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(adminService.getAll());
    }

}

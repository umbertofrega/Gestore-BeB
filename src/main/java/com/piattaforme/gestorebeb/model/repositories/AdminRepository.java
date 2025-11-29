package com.piattaforme.gestorebeb.model.repositories;

import com.piattaforme.gestorebeb.model.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminRepository extends JpaRepository<Admin,Integer> {
    List<Admin> findAdminByEmail(String email);
    boolean existsByEmail(String email);

    Admin searchById(int adminId);

    Admin getAdminById(int ownerId);
}

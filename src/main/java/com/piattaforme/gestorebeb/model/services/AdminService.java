package com.piattaforme.gestorebeb.model.services;

import com.piattaforme.gestorebeb.model.entities.Admin;
import com.piattaforme.gestorebeb.model.enums.AdminRole;
import com.piattaforme.gestorebeb.model.exceptions.conflict.EmailAlreadyExists;
import com.piattaforme.gestorebeb.model.exceptions.forbidden.InsufficientRoleException;
import com.piattaforme.gestorebeb.model.repositories.AdminRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Transactional
    public Admin addAdmin(Admin newAdmin, int ownerId){
        if(newAdmin == null)
            throw new IllegalArgumentException("The new admin can't be null");

        if(adminRepository.existsByEmail(newAdmin.getEmail()))
            throw new EmailAlreadyExists("Used email");
        Admin owner = adminRepository.getAdminById(ownerId);

        if (!owner.getRole().equals(AdminRole.OWNER)) {
            System.err.print("The admin " + owner + " tried to do an owner operation.");
            throw new InsufficientRoleException("Only the owner can do this operation, you are not the owner");
        }
        return adminRepository.save(newAdmin);
    }
}

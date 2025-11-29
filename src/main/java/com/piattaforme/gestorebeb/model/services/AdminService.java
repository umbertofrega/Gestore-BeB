package com.piattaforme.gestorebeb.model.services;

import com.piattaforme.gestorebeb.model.entities.Admin;
import com.piattaforme.gestorebeb.model.enums.AdminRole;
import com.piattaforme.gestorebeb.model.exceptions.conflict.EmailAlreadyExists;
import com.piattaforme.gestorebeb.model.exceptions.notFound.UserNotFoundException;
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
            throw new IllegalArgumentException();

        if(adminRepository.existsByEmail(newAdmin.getEmail()))
            throw new EmailAlreadyExists("Used email");
        Admin owner = adminRepository.getAdminById(ownerId);

        if(!owner.getRole().equals(AdminRole.OWNER))
            throw new IllegalArgumentException();

        return adminRepository.save(newAdmin);
    }

    public Admin changeRole(int adminId, AdminRole role){
        Admin admin = adminRepository.searchById(adminId);

        if(admin == null)
            throw new UserNotFoundException("This admin doesn't exist");

        admin.setRole(role);
        return adminRepository.save(admin);
    }
}

package com.piattaforme.gestorebeb.model.services;

import com.piattaforme.gestorebeb.model.entities.Admin;
import com.piattaforme.gestorebeb.model.enums.AdminRole;
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
    public Admin addAdmin(Admin newAdmin, Admin owner){
        if(newAdmin == null ||  owner == null || adminRepository.existsById(newAdmin.getId())) {
            throw new IllegalArgumentException();
        }
        if(!owner.getRole().equals(AdminRole.OWNER) || !adminRepository.existsById(owner.getId())){
            throw new IllegalArgumentException();
        }
        return adminRepository.save(newAdmin);
    }
}

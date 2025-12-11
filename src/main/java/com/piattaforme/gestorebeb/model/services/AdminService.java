package com.piattaforme.gestorebeb.model.services;

import com.piattaforme.gestorebeb.model.entities.Admin;
import com.piattaforme.gestorebeb.model.enums.AdminRole;
import com.piattaforme.gestorebeb.model.exceptions.conflict.EmailAlreadyExistsException;
import com.piattaforme.gestorebeb.model.exceptions.forbidden.InsufficientRoleException;
import com.piattaforme.gestorebeb.model.repositories.AdminRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Transactional
    public Admin addAdmin(Admin newAdmin) {
        if(newAdmin == null)
            throw new IllegalArgumentException("The new admin can't be null");

        if(adminRepository.existsByEmail(newAdmin.getEmail()))
            throw new EmailAlreadyExistsException("Used email");

        return adminRepository.save(newAdmin);
    }

    @Transactional(readOnly = true)
    public List<Admin> getAll() {
        if (isRealOwner())
            return adminRepository.findAll();
        throw new InsufficientRoleException("Only the owner can do this operation, you are not the owner");
    }

    private boolean isRealOwner() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String ownerKeycloakCode = ((Jwt) authentication.getPrincipal()).getSubject();
        Admin owner = adminRepository.findByCode(ownerKeycloakCode);

        if (!owner.getRole().equals(AdminRole.OWNER)) {
            System.err.print("The admin " + owner + " tried to do an owner operation.");
        }
        return true;
    }

    @Transactional(readOnly = true)
    public void removeAdmin(int id) {
        if (isRealOwner()) {
            this.adminRepository.deleteById(id);
        }
        throw new InsufficientRoleException("Only the owner can do this operation, you are not the owner");
    }
}

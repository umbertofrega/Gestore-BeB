package com.piattaforme.gestorebeb.model.entities;

import com.piattaforme.gestorebeb.model.enums.AdminRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

// Generarli senza chiamare la superclasse non va bene per l'ereditarietà che mi serve
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name="admins")
public class Admin extends User{

    @Enumerated(EnumType.STRING)
    @Column(name="role")
    private AdminRole role;
}

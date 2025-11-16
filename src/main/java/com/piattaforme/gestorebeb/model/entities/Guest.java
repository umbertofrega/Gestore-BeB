package com.piattaforme.gestorebeb.model.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

// Generarli senza chiamare la superclasse non va bene per l'ereditarietà che mi serve
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name="guests")
public class Guest extends User {
}

package com.example.tunihack.entities;

import com.example.tunihack.enumerations.StatusDemande;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class DemandeSang implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String ville;
    private String bloodType;
    private String hospital;
    private LocalDateTime dateTime;
    @Enumerated(EnumType.STRING)
    private StatusDemande statusDemande;
    private Boolean closed ;
    @ManyToOne
    private User user;
}

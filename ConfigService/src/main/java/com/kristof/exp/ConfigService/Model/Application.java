package com.kristof.exp.ConfigService.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "applications")
@Getter
@Setter
public class Application {
    @Id
    @Column(name = "application_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;
    @Column(name = "application_name")
    private String applicationName;
    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL)
    private Set<Property> applicationProperties;
}

package com.kristof.exp.ConfigService.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "environments")
@Getter
@Setter
public class Environment implements Serializable {
    @Id
    @Column(name = "environment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long environmentId;
    @Column(name = "environment_name")
    private String environmentName;
    @OneToMany(mappedBy = "environment", cascade = CascadeType.ALL)
    private Set<Property> environments;
}

package com.kristof.exp.ConfigService.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "applications", schema = "spotifychecker")
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
    @JsonIgnore
    private Set<Property> applicationProperties;
}

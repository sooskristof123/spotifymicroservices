package com.kristof.exp.ConfigService.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "configuration_properties")
@Getter
@Setter
@NoArgsConstructor
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "properties_id")
    private Long propertyId;
    @Column(name = "property_key")
    private String key;
    @Column(name = "value")
    private String value;
    @ManyToOne()
    @JoinColumn(name = "application_id")
    private Application application;
    @ManyToOne()
    @JoinColumn(name = "environment_id")
    private Environment environment;

    public Property(Application application, Environment environment, String key, String value) {
        this.key = key;
        this.value = value;
        this.application = application;
        this.environment = environment;
    }
}

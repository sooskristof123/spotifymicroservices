package com.kristof.exp.ConfigService.Configuration;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;

@Configuration
public class HibernateConfiguration {
    private final DataSource dataSource;
    @Autowired
    public HibernateConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    /**
     * Setting up Hibernate session factory
     * @return the session factory
     */
    @Bean(name = "entityManagerFactory")
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("com.kristof.exp.ConfigService.Model");
        return sessionFactory;
    }
}

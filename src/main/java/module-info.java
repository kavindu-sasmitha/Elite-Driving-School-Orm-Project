module Elite.Driving.School {
    // JavaFX dependencies


    // Hibernate and persistence dependencies
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.naming;


    // Other dependencies
    requires jbcrypt; // BCrypt for password hashing

    requires static lombok;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.controls;
    requires modelmapper; // Compile-time only
    requires javafx.base;

    // Open packages to Hibernate for entity scanning and proxy generation
    opens edu.lk.ijse.entity to org.hibernate.orm.core;

    // Open packages to JavaFX for FXML and data binding
    opens edu.lk.ijse.controller to javafx.fxml;
    opens edu.lk.ijse.dto to javafx.base; // For TableView and data binding
    opens edu.lk.ijse.tm to javafx.base;

    // Export packages for external access (e.g., for testing or other modules)
    exports edu.lk.ijse;
    exports edu.lk.ijse.controller;
    exports edu.lk.ijse.dto;
    exports edu.lk.ijse.bo;
    exports edu.lk.ijse.db;
    exports edu.lk.ijse.exception;
    exports edu.lk.ijse.entity to modelmapper;
    exports edu.lk.ijse.bo.custom;
    exports edu.lk.ijse.bo.custom.impl;


    // -------------------------------------------------------------
    // Add these lines for ModelMapper to work
}
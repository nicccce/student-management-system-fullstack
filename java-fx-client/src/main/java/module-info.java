module com.teach.javafxclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.swing;
    requires org.apache.pdfbox;
    requires java.logging;
    requires com.google.gson;
    requires java.net.http;
    requires spring.security.crypto;
    requires log4j;
    requires javafx.media;
    requires MaterialFX;
    requires fr.brouillard.oss.cssfx;


    opens com.teach.javafxclient to javafx.fxml;
    opens com.teach.javafxclient.model to javafx.base;
    exports com.teach.javafxclient;
    exports com.teach.javafxclient.controller;
    exports com.teach.javafxclient.controller.base;
    exports com.teach.javafxclient.request;
    opens com.teach.javafxclient.request to com.google.gson, javafx.fxml;
    exports com.teach.javafxclient.util;
    exports com.teach.javafxclient.controller.admin;
    exports com.teach.javafxclient.controller.demo;
    exports com.teach.javafxclient.controller.demo.controllers;
    exports com.teach.javafxclient.controller.demo.model;
    opens com.teach.javafxclient.util to com.google.gson, javafx.fxml;
    opens com.teach.javafxclient.controller.base to com.google.gson, javafx.fxml;
    opens com.teach.javafxclient.controller to com.google.gson, javafx.fxml;
    opens com.teach.javafxclient.controller.admin to com.google.gson, javafx.fxml;
    opens com.teach.javafxclient.controller.demo.controllers to com.google.gson, javafx.fxml;
    opens com.teach.javafxclient.controller.demo to com.google.gson, javafx.fxml;
    opens com.teach.javafxclient.controller.demo.model to com.google.gson, javafx.fxml;
}
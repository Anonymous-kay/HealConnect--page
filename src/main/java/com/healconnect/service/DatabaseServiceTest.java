package com.healconnect.service;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.SQLException;

@Testcontainers
class DatabaseServiceTest {
    @Container
    private static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0");

    private DatabaseService dbService;

    @BeforeAll
    static void setupContainer() {
        mysql.withDatabaseName("testdb")
                .withUsername("testuser")
                .withPassword("testpass")
                .withInitScript("init_test_db.sql");
    }

    @BeforeEach
    void setup() {
        dbService = new DatabaseService();
        dbService.setConnectionUrl(mysql.getJdbcUrl());
        dbService.setUsername(mysql.getUsername());
        dbService.setPassword(mysql.getPassword());
        dbService.connect();
    }

    @Test
    void testSessionPersistence() {
        Session testSession = new Session("test123", 1);
        dbService.saveSession(testSession);

        Session retrieved = dbService.getSession("test123");
        assertNotNull(retrieved);
        assertEquals(1, retrieved.getAppointmentId());
    }

    @AfterEach
    void cleanup() {
        dbService.disconnect();
    }
}

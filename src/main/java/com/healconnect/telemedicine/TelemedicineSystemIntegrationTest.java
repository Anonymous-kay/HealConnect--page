package com.healconnect;

import com.healconnect.service.*;
import com.healconnect.telemedicine.*;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class TelemedicineSystemIntegrationTest {
    private TelemedicineSystem system;
    private DatabaseService mockDb;
    private SignalingClient mockSignaling;

    @BeforeEach
    void setup() {
        mockDb = Mockito.mock(DatabaseService.class);
        mockSignaling = Mockito.mock(SignalingClient.class);

        system = new TelemedicineSystem() {
            @Override
            protected DatabaseService createDatabaseService() {
                return mockDb;
            }

            @Override
            protected SignalingClient createSignalingClient() {
                return mockSignaling;
            }
        };
    }

    @Test
    void testSystemInitialization() {
        system.initialize();
        assertTrue(system.isInitialized());
        verify(mockDb).connect();
        verify(mockSignaling).connect();
    }

    @Test
    void testSessionStartCreatesDatabaseRecord() {
        system.initialize();
        Session session = system.startNewSession(1); // appointmentId

        verify(mockDb).saveSession(session);
        assertNotNull(session.getId());
    }
}

package com.healconnect.telemedicine;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.webrtc.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class VideoCallManagerTest {
    private VideoCallManager videoCallManager;
    private PeerConnectionFactory mockFactory;
    private PeerConnection mockConnection;

    @BeforeEach
    void setup() {
        mockFactory = mock(PeerConnectionFactory.class);
        mockConnection = mock(PeerConnection.class);

        when(mockFactory.createPeerConnection(any(), any(), any()))
                .thenReturn(mockConnection);

        videoCallManager = new VideoCallManager();
        videoCallManager.setPeerConnectionFactory(mockFactory);
    }

    @Test
    void testInitializeCreatesPeerConnection() {
        videoCallManager.initialize();
        verify(mockFactory).createPeerConnection(any(), any(), any());
    }

    @Test
    void testStartCallCreatesOffer() {
        videoCallManager.initialize();
        videoCallManager.startCall();
        verify(mockConnection).createOffer(any(), any());
    }
}

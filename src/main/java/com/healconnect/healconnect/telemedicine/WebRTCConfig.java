package com.healconnect.healconnect.telemedicine; // Corrected package

import org.webrtc.PeerConnection; // Added import

import java.util.Arrays;
import java.util.List;

public class WebRTCConfig {
    // Public STUN servers
    public static final List<PeerConnection.IceServer> ICE_SERVERS = Arrays.asList(
            PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer(),
            PeerConnection.IceServer.builder("stun:stun1.l.google.com:19302").createIceServer()
            // Add your TURN servers here if you have them, e.g.:
            // PeerConnection.IceServer.builder("turn:your.turn.server.com:3478")
            //     .setUsername("username")
            //     .setPassword("password")
            //     .createIceServer()
    );

    public static final String SIGNALING_SERVER_URL = "wss://yoursignaling.server.com"; // Replace with your actual signaling server URL
}

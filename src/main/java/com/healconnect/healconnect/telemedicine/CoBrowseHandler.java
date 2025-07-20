package com.healconnect.healconnect.telemedicine; // Corrected package

import javafx.scene.web.WebView; // Added import
import org.webrtc.DataChannel;
import org.webrtc.DataChannel.Buffer;
import org.webrtc.DataChannel.Observer;
import org.webrtc.PeerConnection; // Added import
import java.nio.ByteBuffer; // Added import

public class CoBrowseHandler implements Observer {
    private DataChannel dataChannel;
    private WebView browserView;

    public CoBrowseHandler(WebView webView) {
        this.browserView = webView;
    }

    public void initialize(PeerConnection peerConnection) {
        if (peerConnection == null) {
            System.err.println("CoBrowseHandler: PeerConnection is null. Cannot initialize DataChannel.");
            return;
        }
        DataChannel.Init init = new DataChannel.Init();
        // Ensure the label is unique and descriptive
        dataChannel = peerConnection.createDataChannel("coBrowseChannel", init);
        if (dataChannel != null) {
            dataChannel.registerObserver(this);
            System.out.println("Co-browsing DataChannel initialized.");
        } else {
            System.err.println("Failed to create Co-browsing DataChannel.");
        }
    }

    @Override
    public void onBufferedAmountChange(long l) {
        // Handle buffered amount change if necessary
        System.out.println("Co-browsing channel buffered amount changed: " + l);
    }

    @Override
    public void onStateChange() {
        System.out.println("Co-browsing channel state: " + dataChannel.state());
        // Handle state changes (e.g., open, closed)
    }

    @Override
    public void onMessage(Buffer buffer) {
        // Ensure the buffer has data and is not binary
        if (buffer.data != null && !buffer.binary) {
            String message = new String(buffer.data.array(), java.nio.charset.StandardCharsets.UTF_8);
            System.out.println("Received co-browse message: " + message);
            // Handle browser navigation commands
            javafx.application.Platform.runLater(() -> {
                try {
                    browserView.getEngine().load(message);
                } catch (Exception e) {
                    System.err.println("Error loading URL in WebView: " + e.getMessage());
                }
            });
        }
    }

    public void sendNavigation(String url) {
        if (dataChannel != null && dataChannel.state() == DataChannel.State.OPEN) {
            ByteBuffer buffer = ByteBuffer.wrap(url.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            dataChannel.send(new Buffer(buffer, false)); // false for text data
            System.out.println("Sent co-browse navigation: " + url);
        } else {
            System.err.println("Co-browsing DataChannel is not open. Cannot send navigation.");
        }
    }
}

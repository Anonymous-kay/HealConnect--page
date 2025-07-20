package com.healconnect.healconnect.telemedicine; // Corrected package

import com.google.gson.Gson; // Added import
import org.webrtc.DataChannel;
import org.webrtc.DataChannel.Buffer; // Added import
import org.webrtc.DataChannel.Observer; // Added import
import org.webrtc.PeerConnection; // Added import
import java.nio.ByteBuffer; // Added import

public class WhiteboardHandler implements Observer { // Implemented Observer for DataChannel
    private DataChannel dataChannel;
    private Gson gson = new Gson();
    private AnnotationCanvas annotationCanvas; // Reference to the canvas to draw on

    public WhiteboardHandler(AnnotationCanvas annotationCanvas) {
        this.annotationCanvas = annotationCanvas;
    }

    public static class DrawingCommand {
         String type; // "start", "draw", "clear"
        double x;
        double y;
        String color; // e.g., "#FF0000" for red
        double lineWidth;
    }

    public void initialize(PeerConnection peerConnection) {
        if (peerConnection == null) {
            System.err.println("WhiteboardHandler: PeerConnection is null. Cannot initialize DataChannel.");
            return;
        }
        DataChannel.Init init = new DataChannel.Init();
        dataChannel = peerConnection.createDataChannel("whiteboardChannel", init);
        if (dataChannel != null) {
            dataChannel.registerObserver(this);
            System.out.println("Whiteboard DataChannel initialized.");
        } else {
            System.err.println("Failed to create Whiteboard DataChannel.");
        }
    }

    public void sendCommand(DrawingCommand cmd) {
        if (dataChannel != null && dataChannel.state() == DataChannel.State.OPEN) {
            String json = gson.toJson(cmd);
            ByteBuffer buffer = ByteBuffer.wrap(json.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            dataChannel.send(new Buffer(buffer, false)); // false for text data
            System.out.println("Sent whiteboard command: " + json);
        } else {
            System.err.println("Whiteboard DataChannel is not open. Cannot send command.");
        }
    }

    @Override
    public void onBufferedAmountChange(long l) {
        System.out.println("Whiteboard channel buffered amount changed: " + l);
    }

    @Override
    public void onStateChange() {
        System.out.println("Whiteboard channel state: " + dataChannel.state());
    }

    @Override
    public void onMessage(Buffer buffer) {
        if (buffer.data != null && !buffer.binary) {
            String json = new String(buffer.data.array(), java.nio.charset.StandardCharsets.UTF_8);
            System.out.println("Received whiteboard command: " + json);
            receiveCommand(json); // Process the received command
        }
    }

    public void receiveCommand(String json) {
        DrawingCommand cmd = gson.fromJson(json, DrawingCommand.class);
        // Forward to AnnotationCanvas on JavaFX Application Thread
        javafx.application.Platform.runLater(() -> {
            if (annotationCanvas != null) {
                GraphicsContext gc = annotationCanvas.getGraphicsContext2D();
                switch (cmd.type) {
                    case "start":
                        gc.setStroke(javafx.scene.paint.Color.web(cmd.color));
                        gc.setLineWidth(cmd.lineWidth);
                        gc.beginPath();
                        gc.moveTo(cmd.x, cmd.y);
                        break;
                    case "draw":
                        gc.lineTo(cmd.x, cmd.y);
                        gc.stroke();
                        break;
                    case "clear":
                        annotationCanvas.clearAnnotations();
                        break;
                    default:
                        System.err.println("Unknown drawing command type: " + cmd.type);
                }
            }
        });
    }
}

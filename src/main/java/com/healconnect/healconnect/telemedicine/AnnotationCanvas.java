package com.healconnect.healconnect.telemedicine; // Corrected package

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class AnnotationCanvas extends Canvas {
    private final GraphicsContext gc;
    private boolean isDrawing = false;

    public AnnotationCanvas(double width, double height) {
        super(width, height);
        this.gc = getGraphicsContext2D();

        // Set initial drawing properties
        gc.setStroke(Color.RED);
        gc.setLineWidth(2);

        setOnMousePressed(this::startDrawing);
        setOnMouseDragged(this::draw);
        setOnMouseReleased(this::stopDrawing);
    }

    private void stopDrawing(MouseEvent mouseEvent) {
    }

    private void startDrawing(MouseEvent e) {
        isDrawing = true;
        gc.beginPath();
        gc.moveTo(e.getX(), e.getY());
    }

    private void draw(MouseEvent e) {
        if (isDrawing) {
            gc.lineTo(e.getX(), e.getY());
            gc.stroke();
        }
    }

    private void stopDrawing() {
        isDrawing = false;
        gc.closePath(); // Close the current path
    }

    public void clearAnnotations() {
        gc.clearRect(0, 0, getWidth(), getHeight());
    }
}

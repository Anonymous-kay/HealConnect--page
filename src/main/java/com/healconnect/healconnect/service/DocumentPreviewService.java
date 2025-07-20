package com.healconnect.healconnect.service; // Corrected package

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import java.awt.image.BufferedImage; // Added import
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class DocumentPreviewService {

    public static Image generatePreview(byte[] fileData, String fileName) throws IOException {
        if (fileName == null || fileData == null || fileData.length == 0) {
            return null;
        }

        String lowerCaseFileName = fileName.toLowerCase();
        if (lowerCaseFileName.endsWith(".pdf")) {
            return generatePdfPreview(fileData);
        } else if (lowerCaseFileName.matches(".*\\.(png|jpg|jpeg|gif|bmp)")) { // Added more image types
            return new Image(new ByteArrayInputStream(fileData));
        }
        return null; // No preview for unsupported types
    }

    private static Image generatePdfPreview(byte[] pdfData) throws IOException {
        try (PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfData))) {
            PDFRenderer renderer = new PDFRenderer(document);
            // Render the first page at a reasonable resolution for preview
            BufferedImage bufferedImage = renderer.renderImageWithDPI(0, 96); // Corrected syntax
            return SwingFXUtils.toFXImage(bufferedImage, null);
        } catch (IOException e) {
            System.err.println("Error generating PDF preview: " + e.getMessage());
            throw e;
        }
    }
}

package com.healconnect.healconnect.service; // Corrected package

import com.healconnect.healconnect.util.DBConnection;
import java.sql.*;

public class AuditLogService {
    public static void logAction(int userId, String action, String details) {
        String sql = "INSERT INTO audit_logs (user_id, action, details, timestamp) VALUES (?, ?, ?, ?)"; // Added timestamp

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, action);
            pstmt.setString(3, details);
            pstmt.setTimestamp(4, Timestamp.valueOf(java.time.LocalDateTime.now())); // Set current timestamp
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to log audit action: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

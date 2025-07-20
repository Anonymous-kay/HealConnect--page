package com.healconnect.healconnect.service; // Corrected package

import com.healconnect.healconnect.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationService {

    public static void createNotification(int userId, String message) {
        String sql = "INSERT INTO notifications (user_id, message, created_at) VALUES (?, ?, ?)"; // Added created_at

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, message);
            pstmt.setTimestamp(3, Timestamp.valueOf(java.time.LocalDateTime.now())); // Set current timestamp
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error creating notification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<String> getUnreadNotifications(int userId) {
        List<String> notifications = new ArrayList<>();
        String sql = "SELECT message FROM notifications WHERE user_id = ? AND read_at IS NULL ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    notifications.add(rs.getString("message"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching notifications: " + e.getMessage());
            e.printStackTrace();
        }
        return notifications;
    }

    public static void markNotificationAsRead(int notificationId) {
        String sql = "UPDATE notifications SET read_at = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(java.time.LocalDateTime.now()));
            pstmt.setInt(2, notificationId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error marking notification as read: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

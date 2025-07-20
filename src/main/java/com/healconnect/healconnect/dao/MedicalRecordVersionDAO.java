package com.healconnect.healconnect.dao; // Corrected package

import com.healconnect.healconnect.model.MedicalRecord;
import com.healconnect.healconnect.util.DBConnection;
import java.sql.*;

public class MedicalRecordVersionDAO {

    public boolean createVersion(MedicalRecord record, int changedByUserId) {
        String sql = "INSERT INTO medical_record_versions " +
                "(record_id, version, description, file_path, changed_by, created_at) " + // Added created_at
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, record.getId());
            pstmt.setInt(2, record.getVersion());
            pstmt.setString(3, record.getDescription());
            pstmt.setString(4, record.getFilePath());
            pstmt.setInt(5, changedByUserId);
            pstmt.setTimestamp(6, Timestamp.valueOf(java.time.LocalDateTime.now())); // Set current timestamp

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error creating record version: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}

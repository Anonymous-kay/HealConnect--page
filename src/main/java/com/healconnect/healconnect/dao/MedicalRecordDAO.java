package com.healconnect.healconnect.dao; // Corrected package

import com.healconnect.healconnect.model.MedicalRecord;
import com.healconnect.healconnect.service.FileStorageService;
import com.healconnect.healconnect.util.AppConfig;
import com.healconnect.healconnect.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecordDAO {

    public List<MedicalRecord> getRecordsByPatient(int patientId) {
        List<MedicalRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM medical_records WHERE patient_id = ? ORDER BY record_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                records.add(new MedicalRecord(
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        rs.getDate("record_date").toLocalDate(),
                        rs.getString("record_type"),
                        rs.getString("description"),
                        rs.getString("file_path")
                        // Assuming versioning fields are not in this basic query
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching medical records: " + e.getMessage());
            e.printStackTrace();
        }
        return records;
    }

    public boolean addRecord(MedicalRecord record) {
        String sql = "INSERT INTO medical_records (patient_id, record_date, record_type, description, file_path) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, record.getPatientId());
            pstmt.setDate(2, Date.valueOf(record.getRecordDate()));
            pstmt.setString(3, record.getRecordType());
            pstmt.setString(4, record.getDescription());
            pstmt.setString(5, record.getFilePath());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        record.setId(rs.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding medical record: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateRecord(MedicalRecord record) {
        // This method should also handle versioning if implemented fully
        String sql = "UPDATE medical_records SET description = ?, file_path = ?, version = ?, last_updated = ?, updated_by = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, record.getDescription());
            pstmt.setString(2, record.getFilePath());
            pstmt.setInt(3, record.getVersion() + 1); // Increment version on update
            pstmt.setTimestamp(4, Timestamp.valueOf(java.time.LocalDateTime.now())); // Set last updated to now
            pstmt.setInt(5, record.getUpdatedBy()); // Set the user who updated it
            pstmt.setInt(6, record.getId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating medical record: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteRecord(int recordId) { // Removed uploadDir parameter, use AppConfig
        // First get the file reference if exists
        String filePath = null;
        String sqlSelect = "SELECT file_path FROM medical_records WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlSelect)) {

            pstmt.setInt(1, recordId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                filePath = rs.getString("file_path");
            }

            // Delete from database
            String sqlDelete = "DELETE FROM medical_records WHERE id = ?";
            try (PreparedStatement deleteStmt = conn.prepareStatement(sqlDelete)) {
                deleteStmt.setInt(1, recordId);
                int affectedRows = deleteStmt.executeUpdate();

                if (affectedRows > 0) {
                    if (filePath != null && !filePath.isEmpty()) {
                        // Delete the physical file
                        new FileStorageService(AppConfig.getUploadDir()).deleteFile(filePath);
                    }
                    return true;
                }
                return false; // No rows affected
            }
        } catch (Exception e) {
            System.err.println("Error deleting medical record: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}

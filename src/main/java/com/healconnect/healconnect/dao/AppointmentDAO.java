package com.healconnect.healconnect.dao; // Corrected package

import com.healconnect.healconnect.model.Appointment;
import com.healconnect.healconnect.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    public List<Appointment> getTodaysAppointments(int providerId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.*, p.first_name, p.last_name FROM appointments a " +
                "JOIN patients p ON a.patient_id = p.id " +
                "WHERE a.doctor_id = ? AND DATE(a.appointment_datetime) = CURDATE() " +
                "ORDER BY a.appointment_datetime";

//        try (Connection conn = DBConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setInt(1, providerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Appointment appointment = new Appointment(
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        providerId,
                        rs.getTimestamp("appointment_datetime").toLocalDateTime(),
                        rs.getString("reason"),
                        rs.getString("status")
                );
                // Assuming patient_name is a combined field or set after fetching
                appointment.setPatientName(rs.getString("first_name") + " " + rs.getString("last_name"));
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching today's appointments: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
        }
        return appointments;
    }

    // Placeholder for getTodaysAppointmentsCount
    public int getTodaysAppointmentsCount(int providerId) {
        // This would query the database for the count
        // For now, return a dummy value
        return getTodaysAppointments(providerId).size();
    }

    // Placeholder for createAppointment
    public boolean createAppointment(Appointment appointment) {
        // SQL INSERT statement
        // For now, return true
        System.out.println("Creating appointment: " + appointment);
        return true;
    }
}

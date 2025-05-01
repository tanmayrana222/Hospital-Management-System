package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.PatientModel;

public class Patient {
    private final Connection conn;

    public Patient(Connection conn) {
        this.conn = conn;
    }

    public void addPatient(PatientModel model) throws SQLException {
        String sql = "INSERT INTO patients (name, age, gender) VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, model.getName());
        ps.setInt(2, model.getAge());
        ps.setString(3, model.getGender());
        ps.executeUpdate();
    }

    public List<PatientModel> getAllPatients() throws SQLException {
        List<PatientModel> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            PatientModel p = new PatientModel(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("age"),
                rs.getString("gender")
            );
            patients.add(p);
        }
        return patients;
    }
}

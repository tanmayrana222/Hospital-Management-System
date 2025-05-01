package dao;

import model.DoctorModel;
import java.sql.*;
import java.util.*;

public class Doctor {
    private Connection conn;

    public Doctor(Connection conn) {
        this.conn = conn;
    }

    public void addDoctor(DoctorModel model) throws SQLException {
        String sql = "INSERT INTO doctors (name, specialization) VALUES (?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, model.getName());
        ps.setString(2, model.getSpecialization());
        ps.executeUpdate();
    }

    public List<DoctorModel> getAllDoctors() throws SQLException {
        List<DoctorModel> list = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM doctors");
        while (rs.next()) {
            list.add(new DoctorModel(rs.getInt("id"), rs.getString("name"), rs.getString("specialization")));
        }
        return list;
    }

    public List<DoctorModel> searchDoctors(String keyword) throws SQLException {
        List<DoctorModel> list = new ArrayList<>();
        String sql = "SELECT * FROM doctors WHERE name LIKE ? OR specialization LIKE ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "%" + keyword + "%");
        ps.setString(2, "%" + keyword + "%");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            list.add(new DoctorModel(rs.getInt("id"), rs.getString("name"), rs.getString("specialization")));
        }
        return list;
    }
}

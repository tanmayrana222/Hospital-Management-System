package ui;

import dao.DBConnection;
import dao.Doctor; // make sure this matches the class name in your dao package
import java.awt.*;
import java.sql.Connection;
import java.util.List;
import javax.swing.*;
import model.DoctorModel;

public class DoctorPanel extends JPanel {
    private JTextArea display;

    public DoctorPanel() {
        setLayout(new BorderLayout());

        JButton viewBtn = new JButton("View Doctors");
        display = new JTextArea();
        display.setEditable(false);

        add(viewBtn, BorderLayout.NORTH);
        add(new JScrollPane(display), BorderLayout.CENTER);

        viewBtn.addActionListener(e -> {
            try (Connection conn = DBConnection.getConnection()) {
                List<DoctorModel> list = new Doctor(conn).getAllDoctors(); // corrected here
                display.setText("");
                for (DoctorModel d : list) {
                    display.append(d.getId() + ": " + d.getName() + " (" + d.getSpecialization() + ")\n");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
